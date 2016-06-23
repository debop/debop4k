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
import rx.Single
import rx.Subscriber
import rx.exceptions.OnErrorNotImplementedException

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class SubscribersTest : AbstractReactiveTest() {

  private fun callSubscriberMethods(hasOnError: Boolean, s: Subscriber<Int>) {
    log.debug("onStart")
    s.onStart()
    log.debug("onNext 1")
    s.onNext(1)
    try {
      log.debug("onError")
      s.onError(RuntimeException())
    } catch(e: Throwable) {
      if (hasOnError) {
        log.debug("throw Exception")
        throw e
      }
    }
    log.debug("onCompleted")
    s.onCompleted()
  }

  @Test
  fun testEmptySubscriber() {
    val s = subscriber<Int>()
    callSubscriberMethods(false, s)
  }

  @Test
  fun testSubscriberConstruction() {
    val events = arrayListOf<String>()

    callSubscriberMethods(false,
                          subscriber<Int>().onNext { events.add("onNext($it)") })

    assertThat(events).isEqualTo(listOf("onNext(1)"))
    events.clear()

    callSubscriberMethods(true,
                          subscriber<Int>()
                              .onNext { events.add("onNext($it)") }
                              .onError { events.add(it.javaClass.simpleName) })
    assertThat(events).isEqualTo(listOf("onNext(1)", "RuntimeException"))
    events.clear()

    callSubscriberMethods(true,
                          subscriber<Int>()
                              .onNext { events.add("onNext($it)") }
                              .onError { events.add(it.javaClass.simpleName) }
                              .onCompleted { events.add("onCompleted") })
    assertThat(events).isEqualTo(listOf("onNext(1)", "RuntimeException", "onCompleted"))
    events.clear()
  }

  @Test(expected = OnErrorNotImplementedException::class)
  fun testNotErrorHandlers() {
    subscriber<Int>().onError(Exception("expected"))
  }

  @Test
  fun testErrorHandlers() {
    var errorsCaught = 0

    subscriber<Int>()
        .onError { errorsCaught++ }
        .onError { errorsCaught++ }
        .onError(Exception("expected"))

    assertThat(errorsCaught).isEqualTo(2)
  }

  @Test
  fun testMultipleOnNextHandlers() {
    var nextCaught = 0

    subscriber<Int>()
        .onNext { nextCaught++ }
        .onNext { nextCaught++ }
        .onNext(1)

    assertThat(nextCaught).isEqualTo(2)
  }

  @Test
  fun testOnStart() {
    var started = false
    subscriber<Int>().onStart { started = true }.onStart()
    assertThat(started).isTrue()
  }

  @Test
  fun testSubscribeWith() {
    val completeObservable = observable<Int> { subscriber ->
      subscriber.onNext(1)
      subscriber.onCompleted()
    }
    val events = arrayListOf<String>()

    completeObservable.subscribeWith {
      onNext { events.add("onNext($it)") }
    }
    assertThat(events).isEqualTo(listOf("onNext(1)"))
    events.clear()

    completeObservable.subscribeWith {
      onNext { events.add("onNext($it)") }
      onCompleted { events.add("onCompleted") }
    }
    assertThat(events).isEqualTo(listOf("onNext(1)", "onCompleted"))
    events.clear()

    val errorObservable = observable<Int> {
      it.onNext(1)
      it.onError(RuntimeException())
    }
    errorObservable.subscribeWith {
      onNext { events.add("onNext($it)") }
      onError { events.add("onError(${it.javaClass.simpleName})") }
      onCompleted { events.add("onCompleted") }
    }
    assertThat(events).isEqualTo(listOf("onNext(1)", "onError(RuntimeException)"))
    events.clear()

    // 에러 처리를 구독자가 하지 않는 경우 예외가 발생한다.
    try {
      errorObservable.subscribeWith {
        onNext { events.add("onNext($it)") }
      }
    } catch(e: Throwable) {
      events.add("catch(${e.javaClass.simpleName})")
    }
    assertThat(events).isEqualTo(listOf("onNext(1)", "catch(OnErrorNotImplementedException)"))
  }

  @Test
  fun testSingleSubscribeWith() {
    val events = arrayListOf<String>()
    val successSingle: Single<Int> = singleOf(1)

    successSingle.subscribeWith {
      onSuccess { events.add("onSuccess($it)") }
    }
    assertThat(events).isEqualTo(listOf("onSuccess(1)"))
    events.clear()

    val errorSingle = RuntimeException().toSingle<Int>()
    errorSingle.subscribeWith {
      onSuccess { events.add("onSuccess($it)") }
      onError { events.add("onError(${it.javaClass.simpleName})") }
    }
    assertThat(events).isEqualTo(listOf("onError(RuntimeException)"))
    events.clear()

    try {
      errorSingle.subscribeWith {
        onSuccess { events.add("onNext($it)") }
      }
    } catch(e: Throwable) {
      events.add("catch(${e.javaClass.simpleName})")
    }
    assertThat(events).isEqualTo(listOf("catch(OnErrorNotImplementedException)"))
    events.clear()
  }

  @Test
  fun testIdiomaticAdd() {
    var subscriptionCalled = false
    val s = subscriber<Int>()

    s.add { subscriptionCalled = true }
    s.unsubscribe()

    assertThat(subscriptionCalled).isTrue()
  }

}