/* Copyright 2021 Radix Publishing Ltd incorporated in Jersey (Channel Islands).
 *
 * Licensed under the Radix License, Version 1.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at:
 *
 * radixfoundation.org/licenses/LICENSE-v1
 *
 * The Licensor hereby grants permission for the Canonical version of the Work to be
 * published, distributed and used under or by reference to the Licensor’s trademark
 * Radix ® and use of any unregistered trade names, logos or get-up.
 *
 * The Licensor provides the Work (and each Contributor provides its Contributions) on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
 * including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT,
 * MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Whilst the Work is capable of being deployed, used and adopted (instantiated) to create
 * a distributed ledger it is your responsibility to test and validate the code, together
 * with all logic and performance of that code under all foreseeable scenarios.
 *
 * The Licensor does not make or purport to make and hereby excludes liability for all
 * and any representation, warranty or undertaking in any form whatsoever, whether express
 * or implied, to any entity or person, including any representation, warranty or
 * undertaking, as to the functionality security use, value or other characteristics of
 * any distributed ledger nor in respect the functioning or value of any tokens which may
 * be created stored or transferred using the Work. The Licensor does not warrant that the
 * Work or any use of the Work complies with any law or regulation in any territory where
 * it may be implemented or used or that it will be appropriate for any specific purpose.
 *
 * Neither the licensor nor any current or former employees, officers, directors, partners,
 * trustees, representatives, agents, advisors, contractors, or volunteers of the Licensor
 * shall be liable for any direct or indirect, special, incidental, consequential or other
 * losses of any kind, in tort, contract or otherwise (including but not limited to loss
 * of revenue, income or profits, or loss of use or data, or loss of reputation, or loss
 * of any economic or other opportunity of whatsoever nature or howsoever arising), arising
 * out of or in connection with (without limitation of any use, misuse, of any ledger system
 * or use made or its functionality or any performance or operation of any code or protocol
 * caused by bugs or programming or logic errors or otherwise);
 *
 * A. any offer, purchase, holding, use, sale, exchange or transmission of any
 * cryptographic keys, tokens or assets created, exchanged, stored or arising from any
 * interaction with the Work;
 *
 * B. any failure in a transmission or loss of any token or assets keys or other digital
 * artefacts due to errors in transmission;
 *
 * C. bugs, hacks, logic errors or faults in the Work or any communication;
 *
 * D. system software or apparatus including but not limited to losses caused by errors
 * in holding or transmitting tokens by any third-party;
 *
 * E. breaches or failure of security including hacker attacks, loss or disclosure of
 * password, loss of private key, unauthorised use or misuse of such passwords or keys;
 *
 * F. any losses including loss of anticipated savings or other benefits resulting from
 * use of the Work or any changes to the Work (however implemented).
 *
 * You are solely responsible for; testing, validating and evaluation of all operation
 * logic, functionality, security and appropriateness of using the Work for any commercial
 * or non-commercial purpose and for any reproduction or redistribution by You of the
 * Work. You assume all risks associated with Your use of the Work and the exercise of
 * permissions under this License.
 */

package com.radixdlt.harness.simulation.network;

import static java.util.function.Predicate.not;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.radixdlt.consensus.BFTConfiguration;
import com.radixdlt.consensus.HashSigner;
import com.radixdlt.consensus.bft.Self;
import com.radixdlt.consensus.epoch.EpochChange;
import com.radixdlt.crypto.ECDSASecp256k1PublicKey;
import com.radixdlt.crypto.ECKeyPair;
import com.radixdlt.environment.Environment;
import com.radixdlt.environment.EventDispatcher;
import com.radixdlt.harness.simulation.ModuleRunnerStopper;
import com.radixdlt.harness.simulation.NodeNetworkMessagesModule;
import com.radixdlt.keys.LocalSigner;
import com.radixdlt.ledger.LedgerUpdate;
import com.radixdlt.modules.ModuleRunner;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.p2p.NodeId;
import com.radixdlt.utils.Pair;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** A multi-node bft test network where the network and latencies of each message is simulated. */
public class SimulationNodes {
  private final ImmutableList<ECKeyPair> initialNodes;
  private final SimulationNetwork underlyingNetwork;
  private final Module baseModule;
  private final ImmutableMultimap<ECDSASecp256k1PublicKey, Module> overrideModules;

  /**
   * Create a BFT test network with an underlying simulated network.
   *
   * @param initialNodes The initial nodes on the network
   * @param underlyingNetwork the network simulator
   */
  public SimulationNodes(
      ImmutableList<ECKeyPair> initialNodes,
      SimulationNetwork underlyingNetwork,
      Module baseModule,
      ImmutableMultimap<ECDSASecp256k1PublicKey, Module> overrideModules) {
    this.initialNodes = initialNodes;
    this.baseModule = baseModule;
    this.overrideModules = overrideModules;
    this.underlyingNetwork = Objects.requireNonNull(underlyingNetwork);
  }

  private Module createBFTModule(ECKeyPair self) {
    Module module =
        Modules.combine(
            new AbstractModule() {
              @Provides
              @Self
              private NodeId selfNodeId() {
                return NodeId.fromPublicKey(self.getPublicKey());
              }

              @Provides
              @Self
              private ECDSASecp256k1PublicKey key() {
                return self.getPublicKey();
              }

              @Provides
              private ECKeyPair keyPair() {
                return self;
              }

              @Provides
              @LocalSigner
              HashSigner hashSigner() {
                return self::sign;
              }
            },
            new NodeNetworkMessagesModule(underlyingNetwork),
            baseModule);

    // Override modules can be used to prove that certain adversaries
    // can break network behavior if incorrect modules are used
    if (overrideModules.containsKey(self.getPublicKey())) {
      final var nodeOverrideModules = overrideModules.get(self.getPublicKey());
      module = Modules.override(module).with(nodeOverrideModules);
    }

    return module;
  }

  // TODO: Add support for epoch changes
  public interface RunningNetwork {
    ImmutableSet<NodeId> getNodes();

    BFTConfiguration bftConfiguration();

    Observable<EpochChange> latestEpochChanges();

    Observable<Pair<NodeId, LedgerUpdate>> ledgerUpdates();

    <T> EventDispatcher<T> getDispatcher(Class<T> eventClass, NodeId node);

    Injector getNodeInjector(NodeId node);

    <T> T getInstance(Class<T> clazz, NodeId node);

    <T> T getInstance(Key<T> clazz, NodeId node);

    SimulationNetwork getUnderlyingNetwork();

    Map<NodeId, Metrics> getMetrics();

    void runModule(NodeId node, String name);

    void stop();
  }

  public RunningNetwork start(ImmutableMap<NodeId, ImmutableSet<String>> disabledModuleRunners) {
    return new RunningNetworkImpl(disabledModuleRunners);
  }

  private class RunningNetworkImpl implements RunningNetwork {
    private static final Logger log = LogManager.getLogger();

    private final ImmutableMap<NodeId, ImmutableSet<String>> disabledModuleRunners;
    private final Map<NodeId, Injector> nodes;

    private final Scheduler testScheduler;
    private final ReplaySubject<Observable<Pair<NodeId, EpochChange>>> epochChangeObservables;
    private final ReplaySubject<Observable<Pair<NodeId, LedgerUpdate>>> ledgerUpdateObservables;
    private final Observable<Pair<NodeId, EpochChange>> epochChanges;
    private final Observable<Pair<NodeId, LedgerUpdate>> ledgerUpdates;
    private final CompositeDisposable epochAndLedgerUpdatesDisposable;

    RunningNetworkImpl(ImmutableMap<NodeId, ImmutableSet<String>> disabledModuleRunners) {
      this.disabledModuleRunners = disabledModuleRunners;

      nodes =
          initialNodes.stream()
              .map(
                  key -> {
                    final var module = createBFTModule(key);
                    return Pair.of(key, Guice.createInjector(module));
                  })
              .collect(
                  Collectors.toMap(
                      p -> NodeId.fromPublicKey(p.getFirst().getPublicKey()), Pair::getSecond));

      /* Using ReplaySubject so that the initial events that are
      send in between the module runners are started and rxjava subscriber is started (in awaitCompletion)
      are not lost. */
      epochChangeObservables = ReplaySubject.createWithSize(nodes.size());
      ledgerUpdateObservables = ReplaySubject.createWithSize(nodes.size());
      testScheduler = Schedulers.from(Executors.newSingleThreadExecutor());
      epochChanges =
          Observable.merge(epochChangeObservables)
              .replay(1024)
              .autoConnect()
              .observeOn(testScheduler);
      ledgerUpdates =
          Observable.merge(ledgerUpdateObservables)
              .replay(1024)
              .autoConnect()
              .observeOn(testScheduler);
      epochAndLedgerUpdatesDisposable =
          new CompositeDisposable(epochChanges.subscribe(), ledgerUpdates.subscribe());

      nodes.forEach(this::addObservables);
      nodes.forEach(this::startRunners);
    }

    private void startRunners(NodeId node, Injector injector) {
      final var nodeDisabledModuleRunners =
          disabledModuleRunners.getOrDefault(node, ImmutableSet.of());

      injector
          .getInstance(Key.get(new TypeLiteral<Map<String, ModuleRunner>>() {}))
          .entrySet()
          .stream()
          .filter(not(e -> nodeDisabledModuleRunners.contains(e.getKey())))
          .forEach(e -> e.getValue().start(error -> this.onModuleError(e.getKey(), error)));
    }

    private void onModuleError(String module, Throwable error) {
      log.error("Uncaught error from module {}; stopping the network", module, error);
      this.stop();
    }

    private void addObservables(NodeId node, Injector injector) {
      final var ledgerUpdateObservable =
          injector.getInstance(Key.get(new TypeLiteral<Observable<LedgerUpdate>>() {}));

      ledgerUpdateObservables.onNext(
          ledgerUpdateObservable.map(ledgerUpdate -> Pair.of(node, ledgerUpdate)));

      final var epochChangeObservable =
          ledgerUpdateObservable.flatMapMaybe(
              ledgerUpdate -> {
                final var e = ledgerUpdate.epochChange();
                return e.isEmpty() ? Maybe.empty() : Maybe.just(Pair.of(node, e.orElseThrow()));
              });
      epochChangeObservables.onNext(epochChangeObservable);
    }

    @Override
    public ImmutableSet<NodeId> getNodes() {
      return ImmutableSet.copyOf(nodes.keySet());
    }

    @Override
    public BFTConfiguration bftConfiguration() {
      return nodes.values().stream().findAny().orElseThrow().getInstance(BFTConfiguration.class);
    }

    @Override
    public Observable<EpochChange> latestEpochChanges() {
      // Just do first instance for now
      final var initialEpoch =
          nodes.values().stream().findAny().orElseThrow().getInstance(EpochChange.class);

      return Observable.just(initialEpoch)
          .concatWith(
              epochChanges
                  .map(Pair::getSecond)
                  .scan((cur, next) -> next.nextEpoch() > cur.nextEpoch() ? next : cur)
                  .distinctUntilChanged());
    }

    @Override
    public Observable<Pair<NodeId, LedgerUpdate>> ledgerUpdates() {
      return ledgerUpdates;
    }

    @Override
    public <T> EventDispatcher<T> getDispatcher(Class<T> eventClass, NodeId node) {
      return getInstance(Environment.class, node).getDispatcher(eventClass);
    }

    @Override
    public Injector getNodeInjector(NodeId node) {
      return nodes.get(node);
    }

    @Override
    public <T> T getInstance(Class<T> clazz, NodeId node) {
      return nodes.get(node).getInstance(clazz);
    }

    @Override
    public <T> T getInstance(Key<T> clazz, NodeId node) {
      return nodes.get(node).getInstance(clazz);
    }

    @Override
    public SimulationNetwork getUnderlyingNetwork() {
      return underlyingNetwork;
    }

    @Override
    public Map<NodeId, Metrics> getMetrics() {
      return nodes.entrySet().stream()
          .collect(
              Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getInstance(Metrics.class)));
    }

    @Override
    public void runModule(NodeId node, String name) {
      nodes
          .get(node)
          .getInstance(Key.get(new TypeLiteral<Map<String, ModuleRunner>>() {}))
          .get(name)
          .start(
              error -> {
                log.error(
                    "uncaught error in individually run {}; stopping the network", name, error);
                this.stop();
              });
    }

    @Override
    public void stop() {
      this.nodes.values().forEach(this::stopNode);
      epochAndLedgerUpdatesDisposable.dispose();
      testScheduler.shutdown();
    }

    private void stopNode(Injector injector) {
      injector.getInstance(ModuleRunnerStopper.class).stop();
    }
  }
}
