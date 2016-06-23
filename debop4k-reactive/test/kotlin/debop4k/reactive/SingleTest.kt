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

import org.junit.Test
import org.mockito.Mockito.*
import rx.SingleSubscriber
import java.util.concurrent.*

class SingleTest : AbstractReactiveTest() {

  @Test
  fun testCreate() {
    single<String> { subscriber: SingleSubscriber<in String> ->
      subscriber.onSuccess("Hello World!")
    }.subscribe(received())

    verify(a, times(1)).received("Hello World!")
  }

  @Test
  fun testCreateFromFuture() {
    val future = "Hello World!".toSingletonObservable().toBlocking().toFuture()
    val single = future.toSingle()

    single.subscribe(received<Any?>())

    verify(a, times(1)).received("Hello World!")
  }

  @Test
  fun testCreateFromCallable() {
    val callable = mock(Callable::class.java)
    `when`(callable.call()).thenReturn("value")

    callable.toSingle().subscribe(received<Any?>())

    verify(a, times(1)).received("value")
  }

  @Test
  fun testCreateFromSupplier() {
    { "value" }.toSingle().subscribe(received<Any?>())
    verify(a, times(1)).received("value")
  }

  @Test
  fun testCreateFromJust() {
    singleOf("Hello World!").subscribe(received<Any?>())
    verify(a, times(1)).received("Hello World!")
  }

  @Test
  fun testCreateFromSubscriber() {
    single<String> { it.onSuccess("Success") }.subscribe(received<Any?>())
    verify(a, times(1)).received("Success")
  }
}