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

package debop4k.core.collections.permutations

import java.util.stream.*

fun <T> streamOf(permutation: Permutation<T>): Stream<T> {
  return PermutationStream(permutation)
}

fun <T> Permutation<T>.toStream(): Stream<T> {
  return PermutationStream(this)
}

fun <T> permutationOf(stream: Stream<T>?): Permutation<T> {
  if (stream == null)
    return emptyPermutation()

  return permutationOf(stream.iterator())
}

fun <T> Stream<T>?.toPermutation(): Permutation<T> {
  if (this == null)
    return emptyPermutation()

  return permutationOf(this.iterator())
}

fun IntStream.toPermutation(): Permutation<Int> = permutationOf(iterator())
fun LongStream.toPermutation(): Permutation<Long> = permutationOf(iterator())
fun DoubleStream.toPermutation(): Permutation<Double> = permutationOf(iterator())

fun Permutation<Int>.toIntStream(): IntStream = toStream().mapToInt { it }
fun Permutation<Long>.toLongStream(): LongStream = toStream().mapToLong { it }
fun Permutation<Float>.toFloatStream(): DoubleStream = toStream().mapToDouble(Float::toDouble)
fun Permutation<Double>.toDoubleStream(): DoubleStream = toStream().mapToDouble { it }
