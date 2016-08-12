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

package debop4k.reactive

import debop4k.core.uninitialized
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.slf4j.LoggerFactory
import rx.Observable
import rx.Observable.OnSubscribe
import rx.Subscriber
import kotlin.concurrent.thread

abstract class AbstractReactiveTest {

  val log = LoggerFactory.getLogger(javaClass)

  @Mock val a: ScriptAssertion = uninitialized()
  @Mock val w: Observable<Int> = uninitialized()

  @Before fun setup() {
    MockitoAnnotations.initMocks(this)
  }

  @Suppress("BASE_WITH_NULLABLE_UPPER_BOUND")
  fun <T> received(): (T?) -> Unit = { result: T? -> a.received(result) }

  interface ScriptAssertion {
    fun error(e: Throwable?)
    fun received(e: Any?)
  }

  class TestFactory() {
    var counter = 1

    val numbers: Observable<Int>
      get() = Observable.just(1, 3, 2, 5, 4)

    val onSubscribe: TestOnSubscribe
      get() = TestOnSubscribe(counter++)

    val observable: Observable<String>
      get() = Observable.create(onSubscribe)
  }

  class AsyncObservable : OnSubscribe<Int> {
    override fun call(op: Subscriber<in Int>) {
      thread {
        Thread.sleep(50)
        op.onNext(1)
        op.onNext(2)
        op.onNext(3)
        op.onCompleted()
      }
    }
  }

  class TestOnSubscribe(val count: Int) : OnSubscribe<String> {
    override fun call(subscriber: Subscriber<in String>) {
      subscriber.onNext("hello_$count")
      subscriber.onCompleted()
    }
  }
}