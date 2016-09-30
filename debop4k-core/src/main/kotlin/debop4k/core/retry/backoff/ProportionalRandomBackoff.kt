/*
 * Copyright (c) 2016. KESTI co, ltd
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

import java.util.*
import java.util.concurrent.*

/**
 * ProportionalRandomBackoff
 * @author debop sunghyouk.bae@gmail.com
 */
class ProportionalRandomBackoff(target: Backoff,
                                val multiplier: Double = DEFAULT_MULTIPLIER,
                                random: Random = ThreadLocalRandom.current()) : RandomBackoff(target, random) {

  constructor(target: Backoff, random: Random) : this(target, DEFAULT_MULTIPLIER, random)

  override fun addRandomJitter(initialDelay: Long): Long {
    val randomMultiplier = (1 - 2 * random.nextDouble()) * multiplier
    return (initialDelay * (1 + randomMultiplier)).toLong()
  }
}
