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

package debop4k.examples.sequences

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringReader

/**
 * SequenceExample
 * @author sunghyouk.bae@gmail.com
 */
class SequenceExample : AbstractExampleTest() {

  @Test
  fun sequence_progression() {
    val nums = generateSequence(1) { it + 1 }         // + 1
    val powersOf2 = generateSequence(1) { it * 2 }   // 2 times

    val list = nums.take(10).toList()
    println(list)
    assertThat(list).hasSize(10)
    assertThat(list.last()).isEqualTo(10)
  }

  @Test
  fun map_and_filters() {
    val squares = generateSequence(1) { it + 1 }.map { it * it }
    val oddSquares = squares.filter { it % 2 != 0 }

    val odds = oddSquares.take(5).toList()
    println(odds)
    assertThat(odds).hasSize(5)
    assertThat(odds.last()).isEqualTo(81)
  }

  @Test
  fun readers_to_sequence() {
    val reader = StringReader("line1\nline2\nline3\n").buffered()
    val lines = generateSequence { reader.readLine() }

    val list = lines.takeWhile { it != null }.toList()
    println(list)
    assertThat(list).hasSize(3)
  }

  @Test
  fun fibonacci_sequence() {
    val fibonacci = generateSequence(1 to 1) { it.second to it.first + it.second }.map { it.first }

    val list = fibonacci.take(10).toList()
    println("fibonacci=$list")
  }

  @Test
  fun pair_approach() {
    val primes = generateSequence(2 to generateSequence(3) { it + 2 }) {
      val currSeq = it.second.iterator()
      val nextPrime = currSeq.next()
      nextPrime to currSeq.asSequence().filter { it % nextPrime != 0 }
    }.map { it.first }

    val list = primes.take(10).toList()
    println("primes=$list")
    assertThat(list).containsAll(listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29))
  }

  @Test
  fun recursive_approach() {

    fun primesFilter(from: Sequence<Int>): Sequence<Int> = from.iterator().let {
      val current = it.next()
      sequenceOf(current) + { primesFilter(it.asSequence().filter { it % current != 0 }) }
    }

    val primes = primesFilter(generateSequence(2) { it + 1 })
    val list = primes.take(10).toList()
    println("primes=$list")
    assertThat(list).containsAll(listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29))
  }

  operator fun <T> Sequence<T>.plus(otherGenerator: () -> Sequence<T>) =
      object : Sequence<T> {
        private val thisIter: Iterator<T> by lazy { this@plus.iterator() }
        private val otherIter: Iterator<T> by lazy { otherGenerator().iterator() }

        override fun iterator(): Iterator<T> = object : Iterator<T> {
          override fun next(): T =
              if (thisIter.hasNext()) thisIter.next()
              else otherIter.next()

          override fun hasNext(): Boolean =
              thisIter.hasNext() || otherIter.hasNext()
        }
      }
}