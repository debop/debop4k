/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.core.retry.backoff

import debop4k.core.retry.RetryContext
import java.util.*
import java.util.concurrent.*

/**
 * RandomBackoff
 * @author debop sunghyouk.bae@gmail.com
 */
abstract class RandomBackoff @JvmOverloads constructor(target: Backoff,
                                                       val random: Random = ThreadLocalRandom.current())
: BackoffWrapper(target) {

  override fun delayMillis(context: RetryContext): Long {
    val initialDelay = target.delayMillis(context)
    val randomDelay = addRandomJitter(initialDelay)

    return Math.max(randomDelay, 0)
  }

  abstract fun addRandomJitter(initialDelay: Long): Long

}