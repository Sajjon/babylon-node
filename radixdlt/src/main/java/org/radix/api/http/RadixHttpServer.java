package org.radix.api.http;

import com.radixdlt.middleware2.converters.AtomToBinaryConverter;
import com.radixdlt.middleware2.processing.RadixEngineAtomProcessor;
import com.radixdlt.serialization.Serialization;
import com.radixdlt.store.LedgerEntryStore;
import com.radixdlt.universe.Universe;
import com.stijndewitt.undertow.cors.AllowAll;
import com.stijndewitt.undertow.cors.Filter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import io.undertow.websockets.core.WebSocketChannel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.radix.api.AtomSchemas;
import org.radix.api.jsonrpc.RadixJsonRpcPeer;
import org.radix.api.jsonrpc.RadixJsonRpcServer;
import org.radix.api.services.AdminService;
import org.radix.api.services.AtomsService;
import org.radix.api.services.GraphService;
import org.radix.api.services.InternalService;
import org.radix.api.services.NetworkService;
import org.radix.api.services.TestService;
import org.radix.api.services.UniverseService;
import org.radix.logging.Logger;
import org.radix.logging.Logging;
import org.radix.modules.Modules;
import org.radix.properties.RuntimeProperties;
import org.radix.shards.ShardSpace;
import org.radix.time.Time;
import org.radix.universe.system.LocalSystem;

import java.io.IOException;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: Document me!
 */
public final class RadixHttpServer {
    public static final int DEFAULT_PORT = 8080;
    public static final String CONTENT_TYPE_JSON = "application/json";

    private static final Logger logger = Logging.getLogger("api");

	private final ConcurrentHashMap<RadixJsonRpcPeer, WebSocketChannel> peers;
	private final AtomsService atomsService;
    private final RadixJsonRpcServer jsonRpcServer;
    private final InternalService internalService;

	public RadixHttpServer(LedgerEntryStore store, RadixEngineAtomProcessor radixEngineAtomProcessor, AtomToBinaryConverter atomToBinaryConverter) {
		this.peers = new ConcurrentHashMap<>();
		this.atomsService = new AtomsService(store, radixEngineAtomProcessor, atomToBinaryConverter);
		this.jsonRpcServer = new RadixJsonRpcServer(
				Modules.get(Serialization.class),
				store,
				atomsService,
				AtomSchemas.get()
		);
		this.internalService = InternalService.getInstance(store, radixEngineAtomProcessor);
	}

    private Undertow server;

    /**
     * Get the set of currently connected peers
     *
     * @return The currently connected peers
     */
    public final Set<RadixJsonRpcPeer> getPeers() {
        return Collections.unmodifiableSet(peers.keySet());
    }

    public final void start() {
        RoutingHandler handler = Handlers.routing(true); // add path params to query params with this flag

        // add all REST routes
        addRestRoutesTo(handler);

        // handle POST requests
        addPostRoutesTo(handler);

        // handle websocket requests (which also uses GET method)
        handler.add(
        	Methods.GET,
			"/rpc",
			Handlers.websocket(new RadixHttpWebsocketHandler( this, jsonRpcServer, peers, atomsService))
		);

        // add appropriate error handlers for meaningful error messages (undertow is silent by default)
        handler.setFallbackHandler(exchange ->
        {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
            exchange.getResponseSender().send("No matching path found for " + exchange.getRequestMethod() + " " + exchange.getRequestPath());
        });
        handler.setInvalidMethodHandler(exchange -> {
            exchange.setStatusCode(StatusCodes.NOT_ACCEPTABLE);
            exchange.getResponseSender().send("Invalid method, path exists for " + exchange.getRequestMethod() + " " + exchange.getRequestPath());
        });

        // if we are in a development universe, add the dev only routes (e.g. for spamathons)
        if (Modules.get(Universe.class).isDevelopment()) {
            addDevelopmentOnlyRoutesTo(handler);
        }
        if (Modules.get(Universe.class).isDevelopment() || Modules.get(Universe.class).isTest()) {
        	addTestRoutesTo(handler);
        }

        Integer port = Modules.get(RuntimeProperties.class).get("cp.port", DEFAULT_PORT);
        Filter corsFilter = new Filter(handler);
        // Disable INFO logging for CORS filter, as it's a bit distracting
        java.util.logging.Logger.getLogger(corsFilter.getClass().getName()).setLevel(java.util.logging.Level.WARNING);
        corsFilter.setPolicyClass(AllowAll.class.getName());
        corsFilter.setUrlPattern("^.*$");
        server = Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setHandler(corsFilter)
                .build();
        server.start();
    }

    public final void stop() {
        server.stop();
    }

    private void addDevelopmentOnlyRoutesTo(RoutingHandler handler) {
	    addGetRoute("/api/internal/spamathon", exchange -> {
            String iterations = getParameter(exchange, "iterations").orElse(null);
            String batching = getParameter(exchange, "batching").orElse(null);
            String rate = getParameter(exchange, "rate").orElse(null);

            respond(internalService.spamathon(iterations, batching, rate), exchange);
        }, handler);

		addGetRoute("/api/test/newpeer", exchange -> {
			String key = getParameter(exchange, "key").orElse(null);
			String anchor = getParameter(exchange, "anchor").orElse("0");
			String high = getParameter(exchange, "high").orElse(String.valueOf(ShardSpace.SHARD_CHUNK_RANGE - 1));
			String low = getParameter(exchange, "low").orElse(String.valueOf(-ShardSpace.SHARD_CHUNK_RANGE));
			String ip = getParameter(exchange, "ip").orElse(null);
			String port = getParameter(exchange, "port").orElse("-1"); // Defaults to universe port
			respond(TestService.getInstance().newPeer(key, anchor, high, low, ip, port), exchange);
		}, handler);
	}

    private void addTestRoutesTo(RoutingHandler handler) {

    }

    private void addPostRoutesTo(RoutingHandler handler) {
        HttpHandler rpcPostHandler = new HttpHandler() {
            @Override
            public void handleRequest(HttpServerExchange exchange) {
                // we need to be in another thread to do blocking io work, which is needed to extract the entire message body
                if (exchange.isInIoThread()) {
                    exchange.dispatch(this);
                    return;
                }

                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, CONTENT_TYPE_JSON);
	            try {
		            exchange.getResponseSender().send(jsonRpcServer.handleChecked(exchange));
	            } catch (IOException e) {
		            exchange.setStatusCode(400);
		            exchange.getResponseSender().send("Invalid request: " + e.getMessage());
	            }
            }
        };
        handler.add(Methods.POST, "/rpc", rpcPostHandler);
        handler.add(Methods.POST, "/rpc/", rpcPostHandler); // handle both /rpc and /rpc/ for usability
    }

    private void addRestRoutesTo(RoutingHandler handler) {
        // TODO: organize routes in a nicer way
        // System routes
        addRestSystemRoutesTo(handler);

        // Network routes
        addRestNetworkRoutesTo(handler);

        // Graph routes
        addRestGraphRoutesTo(handler);

        // Atom Model JSON schema
        addGetRoute("/schemas/atom.schema.json", exchange -> {
			respond(AtomSchemas.getJsonSchemaString(4), exchange);
        }, handler);

        addGetRoute("/api/events", exchange -> {
			JSONObject eventCount = new JSONObject();
			atomsService.getAtomEventCount().forEach((k, v) -> eventCount.put(k.name(), v));
			respond(eventCount, exchange);
        }, handler);

        addGetRoute("/api/universe", exchange
                -> respond(UniverseService.getInstance().getUniverse(), exchange), handler);

        addGetRoute("/api/system/modules/api/tasks-waiting", exchange
                -> {
            JSONObject waiting = new JSONObject();
            waiting.put("count", atomsService.getWaitingCount());

            respond(waiting, exchange);
        }, handler);

		addGetRoute("/api/system/modules/api/websockets", exchange -> {
			JSONObject count = new JSONObject();
			count.put("count", getPeers().size());
			respond(count, exchange);
		}, handler);

        // delete method to disconnect all peers
        addRoute("/api/system/modules/api/websockets", Methods.DELETE_STRING, exchange -> {
            JSONObject result = this.disconnectAllPeers();
            respond(result, exchange);
        }, handler);

        addGetRoute("/api/latest-events", exchange -> {
        	JSONArray events = new JSONArray();
        	atomsService.getEvents().forEach(events::put);
        	respond(events, exchange);
		}, handler);

		// keep-alive
        addGetRoute("/api/ping", exchange
            -> respond(
                new JSONObject().put("response", "pong").put("timestamp", System.currentTimeMillis()),
                exchange),
            handler);
    }

    private void addRestGraphRoutesTo(RoutingHandler handler) {

        addGetRoute("/api/graph/route", exchange -> {
        	String timestamp = getParameter(exchange, "timestamp").orElseGet(() -> String.valueOf(Time.currentTimestamp()));
			respond(GraphService.getInstance().getRoutingTable(LocalSystem.getInstance().getNID().toString(), timestamp), exchange);
		}, handler);
    }

    private void addRestNetworkRoutesTo(RoutingHandler handler) {
        addGetRoute("/api/network", exchange -> {
            respond(NetworkService.getInstance().getNetwork(), exchange);
        }, handler);
        addGetRoute("/api/network/peers/live", exchange
                -> respond(NetworkService.getInstance().getLivePeers().toString(), exchange), handler);
        addGetRoute("/api/network/nids/live", exchange -> {
        	String planck = getParameter(exchange, "planck").orElse(null);
			if (planck == null)
				respond(NetworkService.getInstance().getLiveNIDS(), exchange);
			else
				respond(NetworkService.getInstance().getLiveNIDS(planck), exchange);
		}, handler);
        addGetRoute("/api/network/peers", exchange
                -> respond(NetworkService.getInstance().getPeers().toString(), exchange), handler);
        addGetRoute("/api/network/peers/{id}", exchange
                -> respond(NetworkService.getInstance().getPeer(getParameter(exchange, "id").orElse(null)), exchange), handler);

    }

    private void addRestSystemRoutesTo(RoutingHandler handler) {
        addGetRoute("/api/system", exchange
                -> respond(AdminService.getInstance().getSystem(), exchange), handler);
        addGetRoute("/api/system/profiler", exchange
                -> respond(AdminService.getInstance().getProfiler(), exchange), handler);
        addGetRoute("/api/system/modules", exchange
                -> respond(AdminService.getInstance().getModules(), exchange), handler);
        addGetRoute("/api/system/modules/atom-syncer", exchange
                -> respond(AdminService.getInstance().getModules(), exchange), handler);
    }

    // helper methods for responding to an exchange with various objects for readability
    private void respond(String object, HttpServerExchange exchange) {
        exchange.getResponseSender().send(object);
    }

    private void respond(JSONObject object, HttpServerExchange exchange) {
        exchange.getResponseSender().send(object.toString());
    }

    private void respond(JSONArray object, HttpServerExchange exchange) {
        exchange.getResponseSender().send(object.toString());
    }

    /**
     * Close and remove a certain peer
     *
     * @param peer The peer to remove
     */
    /*package*/ void closeAndRemovePeer(RadixJsonRpcPeer peer) {
        peers.remove(peer);
        peer.close();
    }

    /**
     * Disconnect all currently connected peers
     *
     * @return Json object containing disconnect information
     */
	public final JSONObject disconnectAllPeers() {
		JSONObject result = new JSONObject();
		JSONArray closed = new JSONArray();
		result.put("closed", closed);
		HashMap<RadixJsonRpcPeer, WebSocketChannel> peersCopy = new HashMap<>(peers);

		peersCopy.forEach((peer, ws) -> {
			JSONObject closedPeer = new JSONObject();
			closedPeer.put("isOpen", ws.isOpen());
			closedPeer.put("closedReason", ws.getCloseReason());
			closedPeer.put("closedCode", ws.getCloseCode());
			closed.put(closedPeer);

			closeAndRemovePeer(peer);
			try {
				ws.close();
			} catch (IOException e) {
				logger.error("Error while closing web socket: " + e, e);
			}
		});

		result.put("closedCount", peersCopy.size());

		return result;
	}

    /**
     * Add a GET method route with JSON content a certain path and consumer to the given handler
     *
     * @param prefixPath       The prefix path
     * @param responseFunction The consumer that processes incoming exchanges
     * @param routingHandler   The routing handler to add the route to
     */
    private static void addGetRoute(String prefixPath, ManagedHttpExchangeConsumer responseFunction, RoutingHandler routingHandler) {
        addRoute(prefixPath, Methods.GET_STRING, responseFunction, routingHandler);
    }

    /**
     * Add a route with JSON content and a certain path, method, and consumer to the given handler
     *
     * @param prefixPath       The prefix path
     * @param method           The HTTP method
     * @param responseFunction The consumer that processes incoming exchanges
     * @param routingHandler   The routing handler to add the route to
     */
    private static void addRoute(String prefixPath, String method, ManagedHttpExchangeConsumer responseFunction, RoutingHandler routingHandler) {
        addRoute(prefixPath, method, CONTENT_TYPE_JSON, responseFunction::accept, routingHandler);
    }

    /**
     * Add a route with a certain path, method, content type and consumer to the given handler
     *
     * @param prefixPath       The prefix path
     * @param method           The HTTP method
     * @param contentType      The MIME type
     * @param responseFunction The consumer that processes incoming exchanges
     * @param routingHandler   The routing handler to add the route to
     */
    private static void addRoute(String prefixPath, String method, String contentType, ManagedHttpExchangeConsumer responseFunction, RoutingHandler routingHandler) {
        routingHandler.add(method, prefixPath, exchange -> {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, contentType);
            responseFunction.accept(exchange);
        });
    }

    /**
     * Get a parameter from either path or query parameters from an http exchange.
     * Note that path parameters are prioritised over query parameters in the event of a conflict.
     *
     * @param exchange The exchange to get the parameter from
     * @param name     The name of the parameter
     * @return The parameter with the given name from the path or query parameters, or empty if it doesn't exist
     */
    private static Optional<String> getParameter(HttpServerExchange exchange, String name) {
        // our routing handler puts path params into query params by default so we don't need to include them manually
        return Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst);
    }
}

