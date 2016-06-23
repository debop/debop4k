/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.reactive

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class ExtensionTest : AbstractReactiveTest() {

  @Test
  fun testCreate() {
    observable<String> { subscriber ->
      subscriber.onNext("Hello")
      subscriber.onCompleted()
    }.subscribe { result -> a.received(result) }

    verify(a, times(1)).received("Hello")
  }

  @Test
  fun testFilter() {
    listOf(1, 2, 3).toObservable().filter { it >= 2 }.subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
  }

  @Test
  fun testLast() {
    assertThat(listOf(1, 2, 3).toObservable().toBlocking().last()).isEqualTo(3)
  }

  @Test
  fun testLastWithPredicate() {
    assertThat(listOf("one", "two", "three").toObservable().toBlocking().last { x -> x.length == 3 }).isEqualTo("two")
  }

}