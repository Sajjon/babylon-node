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

package com.radixdlt.mempool;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.radixdlt.addressing.Addressing;
import com.radixdlt.consensus.bft.Self;
import com.radixdlt.crypto.ECDSASecp256k1PublicKey;
import com.radixdlt.environment.EventDispatcher;
import com.radixdlt.environment.Runners;
import com.radixdlt.environment.StartProcessorOnRunner;
import com.radixdlt.environment.rx.RemoteEvent;
import com.radixdlt.environment.rx.RxEnvironmentModule;
import com.radixdlt.environment.rx.RxRemoteEnvironment;
import com.radixdlt.ledger.LedgerProofBundle;
import com.radixdlt.ledger.MockedSelfValidatorInfoModule;
import com.radixdlt.ledger.StateComputerLedger.StateComputer;
import com.radixdlt.logger.EventLoggerConfig;
import com.radixdlt.logger.EventLoggerModule;
import com.radixdlt.modules.DispatcherModule;
import com.radixdlt.modules.MockedCryptoModule;
import com.radixdlt.modules.MockedKeyModule;
import com.radixdlt.modules.ModuleRunner;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.monitoring.MetricsInitializer;
import com.radixdlt.networks.Network;
import com.radixdlt.p2p.NodeId;
import com.radixdlt.transactions.RawNotarizedTransaction;
import com.radixdlt.utils.PrivateKeys;
import com.radixdlt.utils.TimeSupplier;
import io.reactivex.rxjava3.core.Flowable;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public final class MempoolRunnerTest {
  private static final Logger log = LogManager.getLogger();

  @Inject private Map<String, ModuleRunner> moduleRunners;
  @Inject private EventDispatcher<MempoolAdd> mempoolAddEventDispatcher;

  private StateComputer stateComputer = mock(StateComputer.class);

  @SuppressWarnings(
      "unchecked") // The mock method doesn't support type-safe generics due to type erasure
  public Module createModule() {
    return new AbstractModule() {
      @Override
      public void configure() {
        var key = PrivateKeys.ofNumeric(1).getPublicKey();
        bind(ECDSASecp256k1PublicKey.class).annotatedWith(Self.class).toInstance(key);
        bind(NodeId.class).annotatedWith(Self.class).toInstance(NodeId.fromPublicKey(key));
        bind(LedgerProofBundle.class).toInstance(mock(LedgerProofBundle.class));
        bind(StateComputer.class).toInstance(stateComputer);
        bind(Metrics.class).toInstance(new MetricsInitializer().initialize());
        bind(RxRemoteEnvironment.class)
            .toInstance(
                new RxRemoteEnvironment() {
                  @Override
                  public <T> Flowable<RemoteEvent<NodeId, T>> remoteEvents(Class<T> messageType) {
                    return Flowable.never();
                  }
                });
        bind(TimeSupplier.class).toInstance(System::currentTimeMillis);
        Multibinder.newSetBinder(binder(), StartProcessorOnRunner.class);
        install(MempoolReceiverConfig.of(10).asModule());
        install(new MockedSelfValidatorInfoModule());
        install(new MockedKeyModule());
        install(new MockedCryptoModule());
        install(new RxEnvironmentModule());
        install(new DispatcherModule());
        install(new MempoolReceiverModule());
        install(
            new EventLoggerModule(
                EventLoggerConfig.addressed(Addressing.ofNetwork(Network.INTEGRATIONTESTNET))));
      }
    };
  }

  @Test
  public void dispatched_mempool_add_arrives_at_state_computer() {
    Guice.createInjector(createModule()).injectMembers(this);
    moduleRunners.get(Runners.MEMPOOL).start(error -> log.error("Uncaught runner error", error));

    MempoolAdd mempoolAdd = MempoolAdd.create(RawNotarizedTransaction.create(new byte[0]));
    mempoolAddEventDispatcher.dispatch(mempoolAdd);

    verify(stateComputer, timeout(1000).times(1)).addToMempool(eq(mempoolAdd), isNull());
  }
}
