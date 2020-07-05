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

package com.radixdlt.consensus.deterministic.tests.bft.synchronous;

import com.radixdlt.consensus.Proposal;
import com.radixdlt.consensus.View;
import com.radixdlt.consensus.deterministic.DeterministicTest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import org.junit.Test;

public class OneProposalDropperResponsiveTest {

	private final Random random = new Random(123456);

	private void runOneProposalDropperResponsiveTest(int numNodes, Function<View, Integer> nodeToDropFunction) {
		final Map<View, Integer> proposalToDrop = new HashMap<>();
		final Map<View, Integer> proposalCount = new HashMap<>();

		final DeterministicTest test = new DeterministicTest(numNodes, true, () -> true);
		test.start();
		for (int step = 0; step < 100000; step++) {
			test.processNextMsg(random, (receiverId, msg) -> {
				if (msg instanceof Proposal) {
					final Proposal proposal = (Proposal) msg;
					final View view = proposal.getVertex().getView();
					final Integer nodeToDrop = proposalToDrop.computeIfAbsent(view, nodeToDropFunction);
					if (proposalCount.merge(view, 1, Integer::sum).equals(numNodes)) {
						proposalToDrop.remove(view);
						proposalCount.remove(view);
					}

					return !receiverId.equals(nodeToDrop);
				}

				return true;
			});
		}
	}

	@Test
	public void when_run_4_correct_nodes_with_random_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(4, v -> random.nextInt(4));
	}

	@Test
	public void when_run_5_correct_nodes_with_random_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(5, v -> random.nextInt(5));
	}

	@Test
	public void when_run_10_correct_nodes_with_random_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(10, v -> random.nextInt(10));
	}

	@Test
	public void when_run_50_correct_nodes_with_random_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(50, v -> random.nextInt(50));
	}

	@Test
	public void when_run_100_correct_nodes_with_random_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(100, v -> random.nextInt(100));
	}

	@Test
	public void when_run_4_correct_nodes_with_single_node_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(4, v -> 0);
	}

	@Test
	public void when_run_5_correct_nodes_with_single_node_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(5, v -> 0);
	}

	@Test
	public void when_run_10_correct_nodes_with_single_node_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(10, v -> 0);
	}

	@Test
	public void when_run_50_correct_nodes_with_single_node_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(50, v -> 0);
	}

	@Test
	public void when_run_100_correct_nodes_with_single_node_proposal_dropper_and_timeouts_disabled__then_bft_should_be_responsive() {
		this.runOneProposalDropperResponsiveTest(100, v -> 0);
	}
}
