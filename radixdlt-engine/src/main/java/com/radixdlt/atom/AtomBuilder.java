/*
 * (C) Copyright 2021 Radix DLT Ltd
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
 *
 */

package com.radixdlt.atom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import com.radixdlt.DefaultSerialization;
import com.radixdlt.constraintmachine.CMMicroInstruction;
import com.radixdlt.constraintmachine.Particle;
import com.radixdlt.crypto.ECDSASignature;
import com.radixdlt.crypto.HashUtils;
import com.radixdlt.identifiers.EUID;
import com.radixdlt.serialization.DsonOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Builder class for atom creation
 */
public final class AtomBuilder {
	private String message;
	private final ImmutableList.Builder<CMMicroInstruction> instructions = ImmutableList.builder();
	private final Map<EUID, ECDSASignature> signatures = new HashMap<>();
	private final Map<HashCode, Particle> localUpParticles = new HashMap<>();

	AtomBuilder() {
	}

	public AtomBuilder message(String message) {
		this.message = message;
		return this;
	}

	public Stream<Particle> localUpParticles() {
		return localUpParticles.values().stream();
	}

	public AtomBuilder spinUp(Particle particle) {
		Objects.requireNonNull(particle, "particle is required");
		this.instructions.add(CMMicroInstruction.spinUp(particle));
		var dson = DefaultSerialization.getInstance().toDson(particle, DsonOutput.Output.ALL);
		var particleHash = HashUtils.sha256(dson);
		localUpParticles.put(particleHash, particle);
		return this;
	}

	public AtomBuilder virtualSpinDown(Particle particle) {
		Objects.requireNonNull(particle, "particle is required");
		this.instructions.add(CMMicroInstruction.virtualSpinDown(particle));
		return this;
	}

	public AtomBuilder spinDown(Particle particle) {
		Objects.requireNonNull(particle, "particle is required");
		var dson = DefaultSerialization.getInstance().toDson(particle, DsonOutput.Output.ALL);
		var particleHash = HashUtils.sha256(dson);
		return spinDown(particleHash);
	}

	public AtomBuilder spinDown(HashCode particleHash) {
		Objects.requireNonNull(particleHash, "particleHash is required");
		this.instructions.add(CMMicroInstruction.spinDown(particleHash));
		localUpParticles.remove(particleHash);
		return this;
	}

	public AtomBuilder particleGroup() {
		this.instructions.add(CMMicroInstruction.particleGroup());
		return this;
	}

	public HashCode computeHashToSign() {
		return Atom.computeHashToSign(instructions.build());
	}

	public Atom buildAtom() {
		return Atom.create(
			instructions.build(),
			ImmutableMap.copyOf(this.signatures),
			this.message
		);
	}

	public void setSignature(EUID id, ECDSASignature signature) {
		this.signatures.put(id, signature);
	}
}