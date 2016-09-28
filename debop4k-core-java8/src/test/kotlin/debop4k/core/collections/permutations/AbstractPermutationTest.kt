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

package debop4k.core.collections.permutations

import debop4k.core.loggerOf
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class AbstractPermutationTest {

  protected val log = loggerOf(javaClass)

  @Before
  fun injectMocks() {
    MockitoAnnotations.initMocks(this)
  }

  protected val emptyPermutation: Permutation<Any> get() = emptyPermutation<Any>()
  protected val emptyIntPermutation: Permutation<Int> get() = emptyPermutation<Int>()

  protected val expectedList = listOf(3, -2, 8, 5, -4, 11, 2, 1)

  protected fun lazy(): Permutation<Int> {
    return cons(3) {
      cons(-2) {
        cons(8) {
          cons(5) {
            cons(-4) {
              cons(11) {
                cons(2) {
                  permutationOf(1)
                }
              }
            }
          }
        }
      }
    }
  }

  protected fun lazy2() = expectedList.toPermutation()

  protected fun loremIpsum(): Array<String> {
    return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi id metus at ligula convallis imperdiet. "
        .toLowerCase().split("[ \\.,]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  }
}
