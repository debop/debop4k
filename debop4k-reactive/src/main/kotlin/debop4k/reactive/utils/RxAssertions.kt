/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("RxAssertions")

package debop4k.reactive.utils

import rx.Observable
import rx.schedulers.Schedulers

/**
 * RxJava 코드를 쉽게 테스트 할 수 있게 해주는 Utility class 입니다.
 *
 * 공식적으로는 [rx.observers.TestSubscriber] 를 사용하면 되지만, 이 클래스도 유용합니다.
 *
 * @see rx.observers.TestSubscriber
 * @author sunghyouk.bae@gmail.com
 */
object RxAssertions {

  fun <T> subscribeAssertThat(observable: Observable<T>): RxAssertions.ObservableAssertions<T> {
    return RxAssertions.ObservableAssertions<T>(observable)
  }

  class ObservableAssertions<T>(val observable: Observable<T>) {

    var result: MutableList<T> = arrayListOf()
    var error: Throwable? = null
    var completed: Boolean = false

    init {
      observable.subscribeOn(Schedulers.immediate()).subscribe(
          { item -> result.add(item) },
          { e -> error = e },
          { completed = true }
                                                              )
    }

    fun completesSuccessfully(): RxAssertions.ObservableAssertions<T> {
      if (!completed || error != null) {
        if (error != null)
          error?.printStackTrace()
        val msg = error?.toString() ?: "onComplet not called"
        throw AssertionFailedError("Observable has not completed successfully - cause: $msg")
      }
      return this
    }

    fun fails(): RxAssertions.ObservableAssertions<T> {
      if (error == null)
        throw AssertionFailedError("Observable has not failed")
      return this
    }

    fun failsWithError(throwable: Throwable): RxAssertions.ObservableAssertions<T> {
      fails()
      if (!throwable.equals(error))
        throw AssertionFailedError("Observable has failed with a different error, expected is $throwable but thrown was $error")

      return this
    }

    fun hasSize(expectedSize: Int): RxAssertions.ObservableAssertions<T> {
      if (expectedSize != result.size) {
        throw AssertionFailedError("Observable has emitted ${result.size} items but expected was $expectedSize")
      }
      return this
    }

    fun emits(vararg expectedItems: T): RxAssertions.ObservableAssertions<T> {
      completesSuccessfully()
      assertEmittedEquals(expectedItems.toList())
      return this
    }

    fun emits(expectedItems: Collection<T>): RxAssertions.ObservableAssertions<T> {
      completesSuccessfully()
      assertEmittedEquals(expectedItems.toList())
      return this
    }

    fun emitsNothing(): RxAssertions.ObservableAssertions<T> {
      completesSuccessfully()
      if (result.size > 0) {
        throw AssertionFailedError("Observable has omitted ${result.size} items")
      }
      return this
    }

    private fun <T> assertEmittedEquals(expectedItems: List<T>) {
      hasSize(expectedItems.size)
      for (i in expectedItems.indices) {
        val expected = expectedItems[i]
        val actual = result[i]

        if (expected != actual) {
          throw AssertionFailedError("Emitted item in position $i does not match,\nexpected: $expected\nactual: $actual")
        }
      }
    }
  }

}