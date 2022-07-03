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

package com.radixdlt.application.system;

import static com.radixdlt.substate.TxAction.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.radixdlt.application.system.construction.CreateSystemConstructorV2;
import com.radixdlt.application.system.construction.NextEpochConstructorV3;
import com.radixdlt.application.system.construction.NextRoundConstructorV3;
import com.radixdlt.application.system.scrypt.EpochUpdateConstraintScrypt;
import com.radixdlt.application.system.scrypt.RoundUpdateConstraintScrypt;
import com.radixdlt.application.system.scrypt.SystemConstraintScrypt;
import com.radixdlt.application.tokens.Amount;
import com.radixdlt.application.tokens.construction.CreateMutableTokenConstructor;
import com.radixdlt.application.tokens.construction.MintTokenConstructor;
import com.radixdlt.application.tokens.construction.StakeTokensConstructorV3;
import com.radixdlt.application.tokens.scrypt.StakingConstraintScryptV4;
import com.radixdlt.application.tokens.scrypt.TokensConstraintScryptV3;
import com.radixdlt.application.tokens.state.PreparedStake;
import com.radixdlt.application.unique.scrypt.MutexConstraintScrypt;
import com.radixdlt.application.validators.construction.RegisterValidatorConstructor;
import com.radixdlt.application.validators.scrypt.ValidatorConstraintScryptV2;
import com.radixdlt.application.validators.scrypt.ValidatorRegisterConstraintScrypt;
import com.radixdlt.application.validators.scrypt.ValidatorUpdateOwnerConstraintScrypt;
import com.radixdlt.application.validators.scrypt.ValidatorUpdateRakeConstraintScrypt;
import com.radixdlt.cmos.ConstraintMachineOS;
import com.radixdlt.cmos.ConstraintScrypt;
import com.radixdlt.constraintmachine.ConstraintMachine;
import com.radixdlt.constraintmachine.PermissionLevel;
import com.radixdlt.constraintmachine.REEvent;
import com.radixdlt.engine.RadixEngine;
import com.radixdlt.engine.parser.REParser;
import com.radixdlt.identifiers.REAddr;
import com.radixdlt.store.EngineStore;
import com.radixdlt.store.InMemoryEngineStore;
import com.radixdlt.substate.REConstructor;
import com.radixdlt.substate.TxnConstructionRequest;
import com.radixdlt.utils.PrivateKeys;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class NextEpochV2Test {
  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return List.of(
        new Object[][] {
          {
            List.of(
                new RoundUpdateConstraintScrypt(10),
                new EpochUpdateConstraintScrypt(10, Amount.ofTokens(10).toSubunits(), 9800, 1, 10),
                new StakingConstraintScryptV4(Amount.ofTokens(10).toSubunits()),
                new TokensConstraintScryptV3(Set.of(), Pattern.compile("[a-z0-9]+")),
                new ValidatorConstraintScryptV2(),
                new ValidatorUpdateRakeConstraintScrypt(2),
                new ValidatorRegisterConstraintScrypt(),
                new ValidatorUpdateOwnerConstraintScrypt()),
            REConstructor.newBuilder()
                .put(NextRound.class, new NextRoundConstructorV3())
                .put(
                    NextEpoch.class,
                    new NextEpochConstructorV3(Amount.ofTokens(10).toSubunits(), 9800, 1, 10))
                .put(CreateSystem.class, new CreateSystemConstructorV2())
                .put(
                    CreateMutableToken.class,
                    new CreateMutableTokenConstructor(SystemConstraintScrypt.MAX_SYMBOL_LENGTH))
                .put(MintToken.class, new MintTokenConstructor())
                .put(
                    StakeTokens.class,
                    new StakeTokensConstructorV3(Amount.ofTokens(10).toSubunits()))
                .put(RegisterValidator.class, new RegisterValidatorConstructor())
                .build()
          }
        });
  }

  private RadixEngine<Void> sut;
  private EngineStore<Void> store;
  private REParser parser;
  private final List<ConstraintScrypt> scrypts;
  private final REConstructor constructors;

  public NextEpochV2Test(List<ConstraintScrypt> scrypts, REConstructor constructors) {
    this.scrypts = scrypts;
    this.constructors = constructors;
  }

  @Before
  public void setup() {
    var cmOS = new ConstraintMachineOS();
    cmOS.load(new SystemConstraintScrypt());
    scrypts.forEach(cmOS::load);
    cmOS.load(new MutexConstraintScrypt()); // For v1 start
    var cm =
        new ConstraintMachine(
            cmOS.getProcedures(),
            cmOS.buildSubstateDeserialization(),
            cmOS.buildVirtualSubstateDeserialization());
    this.parser = new REParser(cmOS.buildSubstateDeserialization());
    var serialization = cmOS.buildSubstateSerialization();
    this.store = new InMemoryEngineStore<>();
    this.sut = new RadixEngine<>(parser, serialization, constructors, cm, store);
  }

  @Test
  public void prepared_stake_should_disappear_on_next_epoch() throws Exception {
    // Arrange
    var key = PrivateKeys.ofNumeric(1).getPublicKey();
    var accountAddr = REAddr.ofPubKeyAccount(key);
    var start =
        sut.construct(
                TxnConstructionRequest.create()
                    .action(new CreateSystem(0))
                    .action(
                        new CreateMutableToken(
                            REAddr.ofNativeToken(), "xrd", "xrd", "", "", "", null))
                    .action(
                        new MintToken(
                            REAddr.ofNativeToken(), accountAddr, Amount.ofTokens(10).toSubunits()))
                    .action(new StakeTokens(accountAddr, key, Amount.ofTokens(10).toSubunits())))
            .buildWithoutSignature();
    sut.execute(List.of(start), null, PermissionLevel.SYSTEM);

    var request = TxnConstructionRequest.create().action(new NextEpoch(1));

    // Act
    var txn = sut.construct(request).buildWithoutSignature();
    this.sut.execute(List.of(txn), null, PermissionLevel.SUPER_USER);

    // Assert
    var map =
        sut.read(reader -> reader.reduceResources(PreparedStake.class, PreparedStake::delegateKey));
    assertThat(map).isEmpty();
  }

  @Test
  public void registered_validator_with_no_stake_should_not_be_in_validatorset() throws Exception {
    // Arrange
    var key = PrivateKeys.ofNumeric(1).getPublicKey();
    var start =
        sut.construct(
                TxnConstructionRequest.create()
                    .action(new CreateSystem(0))
                    .action(
                        new CreateMutableToken(
                            REAddr.ofNativeToken(), "xrd", "xrd", "", "", "", null))
                    .action(new RegisterValidator(key)))
            .buildWithoutSignature();
    sut.execute(List.of(start), null, PermissionLevel.SYSTEM);

    var request = TxnConstructionRequest.create().action(new NextEpoch(1));

    // Act
    var txn = sut.construct(request).buildWithoutSignature();
    var result = this.sut.execute(List.of(txn), null, PermissionLevel.SUPER_USER);
    var nextValidatorSet =
        result.getProcessedTxn().getEvents().stream()
            .filter(REEvent.NextValidatorSetEvent.class::isInstance)
            .map(REEvent.NextValidatorSetEvent.class::cast)
            .findFirst()
            .orElseThrow();
    assertThat(nextValidatorSet.nextValidators()).isEmpty();
  }
}
