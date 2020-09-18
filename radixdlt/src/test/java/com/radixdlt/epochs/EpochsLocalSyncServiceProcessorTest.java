/*
 * (C) Copyright 2020 Radix DLT Ltd
 *
 * Radix DLT Ltd licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.radixdlt.epochs;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.radixdlt.consensus.BFTConfiguration;
import com.radixdlt.consensus.VerifiedLedgerHeaderAndProof;
import com.radixdlt.consensus.bft.BFTNode;
import com.radixdlt.consensus.epoch.EpochChange;
import com.radixdlt.ledger.AccumulatorState;
import com.radixdlt.sync.LocalSyncRequest;
import com.radixdlt.sync.LocalSyncServiceAccumulatorProcessor;
import com.radixdlt.sync.LocalSyncServiceAccumulatorProcessor.SyncInProgress;
import com.radixdlt.sync.StateSyncNetwork;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;

public class EpochsLocalSyncServiceProcessorTest {
	private EpochsLocalSyncServiceProcessor processor;
	private LocalSyncServiceAccumulatorProcessor initialProcessor;
	private EpochChange initialEpoch;
	private VerifiedLedgerHeaderAndProof initialHeader;
	private Function<BFTConfiguration, LocalSyncServiceAccumulatorProcessor> localSyncFactory;
	private StateSyncNetwork stateSyncNetwork;
	private SyncedEpochSender syncedEpochSender;

	@Before
	public void setup() {
		this.initialProcessor = mock(LocalSyncServiceAccumulatorProcessor.class);
		this.initialEpoch = mock(EpochChange.class);
		this.initialHeader = mock(VerifiedLedgerHeaderAndProof.class);
		this.localSyncFactory = mock(Function.class);
		this.stateSyncNetwork = mock(StateSyncNetwork.class);
		this.syncedEpochSender = mock(SyncedEpochSender.class);
		this.processor = new EpochsLocalSyncServiceProcessor(
			this.initialProcessor,
			this.initialEpoch,
			this.initialHeader,
			this.localSyncFactory,
			this.stateSyncNetwork,
			this.syncedEpochSender
		);
	}

	@Test
	public void given_no_updates__when_process_timeout__then_should_forward_to_initial_processor() {
		SyncInProgress syncInProgress = mock(SyncInProgress.class);
		this.processor.processSyncTimeout(syncInProgress);

		verify(this.initialProcessor, times(1)).processSyncTimeout(eq(syncInProgress));
	}

	@Test
	public void given_current_epoch_1__and_request_for_epoch_1_with_different_accumulator__then_should_forward_to_processor() {
		when(initialEpoch.getEpoch()).thenReturn(1L);

		LocalSyncRequest request = mock(LocalSyncRequest.class);
		VerifiedLedgerHeaderAndProof header = mock(VerifiedLedgerHeaderAndProof.class);
		when(header.getAccumulatorState()).thenReturn(mock(AccumulatorState.class));
		when(header.getEpoch()).thenReturn(1L);
		when(request.getTarget()).thenReturn(header);
		processor.processLocalSyncRequest(request);

		verify(stateSyncNetwork, never()).sendSyncRequest(any(), any());
		verify(syncedEpochSender, never()).sendSyncedEpoch(any());
		verify(initialProcessor, times(1)).processLocalSyncRequest(eq(request));
	}

	@Test
	public void given_current_epoch_1__and_request_for_epoch_2__then_should_send_epoch_sync_request() {
		when(initialEpoch.getEpoch()).thenReturn(1L);
		when(initialEpoch.getProof()).thenReturn(mock(VerifiedLedgerHeaderAndProof.class));

		LocalSyncRequest request = mock(LocalSyncRequest.class);
		when(request.getTargetNodes()).thenReturn(ImmutableList.of(mock(BFTNode.class)));
		VerifiedLedgerHeaderAndProof header = mock(VerifiedLedgerHeaderAndProof.class);
		when(header.getEpoch()).thenReturn(2L);
		when(request.getTarget()).thenReturn(header);
		processor.processLocalSyncRequest(request);

		verify(stateSyncNetwork, times(1)).sendSyncRequest(any(), any());
		verify(syncedEpochSender, never()).sendSyncedEpoch(any());
		verify(initialProcessor, never()).processLocalSyncRequest(any());
	}
}