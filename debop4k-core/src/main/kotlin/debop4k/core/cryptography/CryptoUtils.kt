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

package debop4k.core.cryptography

import java.security.SecureRandom
import java.util.*


fun randomBytes(num: Int): ByteArray {
  require(num >= 0)

  val bytes = ByteArray(num)
  random.nextBytes(bytes)
  return bytes
}

val RANDOM_NUMBER_GENERATION = "SHA1PRNG"

internal val random: Random = SecureRandom.getInstance(RANDOM_NUMBER_GENERATION)