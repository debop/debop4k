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

package debop4k.core.collections.permutations.samples

import debop4k.core.collections.permutations.*
import org.quartz.CronExpression
import java.util.*

fun primes(): Permutation<Int> {
  return iterate(2, ::nextPrimeAfter)
}

private fun nextPrimeAfter(after: Int): Int {
  var candidate = after + 1
  while (!isPrime(candidate)) {
    candidate++
  }
  return candidate
}

private fun isPrime(candidate: Int): Boolean {
  val max = Math.sqrt(candidate.toDouble()).toInt()
  for (div in 2..max) {
    if (candidate % div == 0) {
      return false
    }
  }
  return true
}

/**
 * 1 - 1/3 + 1/5 - 1/7 + 1/9
 */
fun piSeriesEstimation(): Permutation<Double> {
  return tabulate(0) { (1.0 - (it % 2.0) * 2.0) / (2.0 * it + 1.0) }
}

fun cronFireTimes(expr: CronExpression, after: Date): Permutation<Date> {
  val nextFireTime = expr.getNextValidTimeAfter(after)
  if (nextFireTime == null) {
    return emptyPermutation()
  } else {
    return cons(nextFireTime, cronFireTimes(expr, nextFireTime))
  }
}