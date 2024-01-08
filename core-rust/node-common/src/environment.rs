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

use tokio::runtime::Runtime;
use tracing::{Level, Subscriber};
use tracing_subscriber::layer::SubscriberExt;
use tracing_subscriber::registry::LookupSpan;
use tracing_subscriber::util::SubscriberInitExt;
use tracing_subscriber::Layer;

pub fn setup_tracing(runtime: &Runtime, jaeger_agent_endpoint: Option<String>, log_level: Level) {
    runtime.spawn(async move {
        let opentelemetry = jaeger_agent_endpoint.map(create_opentelemetry_layer);

        // Try to initialize a global logger here, and carry on if this fails.
        // Note: a common "failure" occurs during tests, where multiple "environments" are set up
        // (consecutively) in a single process.
        let _ = tracing_subscriber::registry()
            .with(tracing_subscriber::filter::LevelFilter::from_level(
                log_level,
            ))
            .with(opentelemetry)
            .with(tracing_subscriber::fmt::layer())
            .try_init();
    });
}

fn create_opentelemetry_layer<S: Subscriber + for<'a> LookupSpan<'a>>(
    jaeger_agent_endpoint: impl Into<String>,
) -> impl Layer<S> {
    // TODO: increasing this or leaving [`opentelemetry_jaeger`] with the default value will not
    // work for MacOS (by default, max UDP datagram size is 9216). Since this is not (yet) used
    // in production, minimum value that works on most systems is used (for local testing out of
    // the box). This needs a way of figuring this value at runtime (cross-platform and
    // preferrably nicer than binary searching it) and/or pass through a configuration parameter.
    let max_udp_packet_size = 9216;
    let tracer = opentelemetry_jaeger::new_agent_pipeline()
        .with_endpoint(jaeger_agent_endpoint.into())
        // default value can be bigger than the supported one (i.e. MacOS)
        .with_max_packet_size(max_udp_packet_size)
        .with_auto_split_batch(true)
        .with_service_name("babylon-node")
        .install_batch(opentelemetry::runtime::Tokio)
        .unwrap();
    tracing_opentelemetry::layer().with_tracer(tracer)
}
