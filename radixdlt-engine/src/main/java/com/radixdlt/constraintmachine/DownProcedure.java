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

package com.radixdlt.constraintmachine;

import com.radixdlt.store.ReadableAddrs;

import java.util.function.Function;

public class DownProcedure<D extends Particle, S extends ReducerState> implements MethodProcedure {
	private final Class<D> downClass;
	private final Class<S> reducerStateClass;
	private final DownReducer<D, S> downReducer;
	private final Function<SubstateWithArg<D>, DownAuthorization> authorization;

	public DownProcedure(
		Class<D> downClass, Class<S> reducerStateClass,
		Function<SubstateWithArg<D>, DownAuthorization> authorization,
		DownReducer<D, S> downReducer
	) {
		this.downClass = downClass;
		this.reducerStateClass = reducerStateClass;
		this.downReducer = downReducer;
		this.authorization = authorization;
	}

	public ProcedureKey getDownProcedureKey() {
		return ProcedureKey.of(downClass, reducerStateClass);
	}

	@Override
	public PermissionLevel permissionLevel(Object o) {
		return authorization.apply((SubstateWithArg<D>) o).permissionLevel();
	}

	@Override
	public void verifyAuthorization(Object o, ReadableAddrs readableAddrs, ExecutionContext context) throws AuthorizationException {
		authorization.apply((SubstateWithArg<D>) o).authorizer().verify(readableAddrs, context);
	}

	@Override
	public ReducerResult call(Object o, ReducerState reducerState, ReadableAddrs readableAddrs) throws ProcedureException {
		return downReducer.reduce((SubstateWithArg<D>) o, (S) reducerState, readableAddrs);
	}
}
