/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.retry.backoff

import debop4k.core.retry.RetryContext

/**
 * Backoff
 * @author debop sunghyouk.bae@gmail.com
 */
interface Backoff {

  fun delayMillis(context: RetryContext): Long

  fun withUniformJitter(): Backoff
      = UniformRandomBackoff(this)

  fun withUniformJitter(range: Long): Backoff
      = UniformRandomBackoff(this, range)


  fun withProportionalJitter(): Backoff
      = ProportionalRandomBackoff(this)

  fun withProportionalJitter(multiplier: Double): Backoff
      = ProportionalRandomBackoff(this, multiplier)


  fun withMinDelay(): Backoff
      = BoundedMinBackoff(this)

  fun withMinDelay(minDelayMillis: Long): Backoff
      = BoundedMinBackoff(this, minDelayMillis)

  fun withMaxDelay(): Backoff
      = BoundedMaxBackoff(this)

  fun withMaxDelay(maxDelayMillis: Long): Backoff
      = BoundedMaxBackoff(this, maxDelayMillis)

  fun withFirstRetryNoDelay(): Backoff
      = FirstRetryNoDelayBackoff(this)

  fun withExponentialDelay(initialDelayMillis: Long, multiplier: Double): Backoff
      = ExponentialDelayBackoff(initialDelayMillis, multiplier)

  fun withFixedInterval(): Backoff
      = FixedIntervalBackoff()

  fun withFixedInterval(fixedInterval: Long): Backoff
      = FixedIntervalBackoff(fixedInterval)
}