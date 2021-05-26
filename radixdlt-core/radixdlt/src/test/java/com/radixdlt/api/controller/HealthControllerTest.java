/*
 * (C) Copyright 2021 Radix DLT Ltd
 *
 * Radix DLT Ltd licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the
 * License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.radixdlt.api.controller;

import org.junit.Test;

import com.radixdlt.api.service.NetworkInfoService;

import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.HeaderMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.radixdlt.api.data.NodeStatus.BOOTING;
import static com.radixdlt.api.data.NodeStatus.STALL;
import static com.radixdlt.api.data.NodeStatus.SYNCING;
import static com.radixdlt.api.data.NodeStatus.UP;

public class HealthControllerTest {
	private final NetworkInfoService networkInfoService = mock(NetworkInfoService.class);
	private final HealthController controller = new HealthController(networkInfoService);

	@Test
	public void routesAreConfigured() {
		var handler = mock(RoutingHandler.class);

		controller.configureRoutes(handler);

		verify(handler).get(eq("/health"), any());
		verify(handler).get(eq("/health/"), any());
	}

	@Test
	public void healthStatusIsReturned() {
		var exchange = mock(HttpServerExchange.class);
		var sender = mock(Sender.class);

		when(exchange.getResponseHeaders()).thenReturn(new HeaderMap());
		when(exchange.getResponseSender()).thenReturn(sender);
		when(networkInfoService.nodeStatus()).thenReturn(BOOTING, SYNCING, UP, STALL);

		controller.handleHealthRequest(exchange);
		verify(sender).send("{\"status\":\"BOOTING\"}");

		controller.handleHealthRequest(exchange);
		verify(sender).send("{\"status\":\"SYNCING\"}");

		controller.handleHealthRequest(exchange);
		verify(sender).send("{\"status\":\"UP\"}");

		controller.handleHealthRequest(exchange);
		verify(sender).send("{\"status\":\"STALL\"}");
	}
}