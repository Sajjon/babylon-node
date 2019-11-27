package com.radixdlt.examples.tictactoe;

import com.radixdlt.common.AID;
import com.radixdlt.common.Atom;
import com.radixdlt.common.EUID;
import com.radixdlt.constraintmachine.CMMicroInstruction;
import com.radixdlt.constraintmachine.CMMicroInstruction.CMMicroOp;
import com.radixdlt.constraintmachine.Particle;
import com.radixdlt.constraintmachine.Spin;
import com.radixdlt.engine.RadixEngineAtom;
import com.radixdlt.middleware.RadixEngineUtils;
import com.radixdlt.store.EngineStore;
import com.radixdlt.store.SpinStateMachine;
import com.radixdlt.utils.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A non-persistent engine store. Useful for testing.
 *
 * TODO: Move into mainline code after audit
 */
public class InMemoryEngineStore implements EngineStore {
	private Map<Particle, Pair<Spin, Atom>> storedParticles = new HashMap<>();

	@Override
	public boolean supports(Set<EUID> destinations) {
		return true;
	}

	@Override
	public Spin getSpin(Particle particle) {
		Pair<Spin, Atom> stored = storedParticles.get(particle);
		return stored == null ? Spin.NEUTRAL : stored.getFirst();
	}

	@Override
	public void getAtomContaining(Particle particle, boolean isInput, Consumer<Atom> callback) {
		callback.accept(storedParticles.get(particle).getSecond());
	}

	@Override
	public void storeAtom(Atom atom) {
		RadixEngineAtom radixEngineAtom = null;
		try {
			radixEngineAtom = RadixEngineUtils.toCMAtom(atom);
		} catch (RadixEngineUtils.CMAtomConversionException e) {
			e.printStackTrace();
		}
		for (CMMicroInstruction microInstruction : radixEngineAtom.getCMInstruction().getMicroInstructions()) {
			if (microInstruction.getMicroOp() == CMMicroOp.PUSH) {
				storedParticles.put(
					microInstruction.getParticle(),
					Pair.of(
						SpinStateMachine.next(getSpin(microInstruction.getParticle())),
						atom
					)
				);
			}
		}
	}

	@Override
	public void deleteAtom(AID atom) {
		throw new UnsupportedOperationException("Deleting is not supported by this engine store.");
	}
}
