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

import debop4k.core.java.collections.eclipseCollections.asList
import debop4k.core.java.collections.eclipseCollections.fastListOf
import debop4k.core.java.collections.eclipseCollections.intArrayListOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import rx.Notification
import rx.Observable
import rx.schedulers.TestScheduler
import java.util.concurrent.*
import kotlin.test.fail

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

  @Test
  fun testMap1() {
    1.toSingletonObservable().map { v -> "hello_$v" }.subscribe(received())
    verify(a, times(1)).received("hello_1")
  }

  @Test
  fun testMap2() {
    listOf(1, 2, 3).toObservable().map { v -> "hello_$v" }.subscribe(received())
    verify(a, times(1)).received("hello_1")
    verify(a, times(1)).received("hello_2")
    verify(a, times(1)).received("hello_3")

    intArrayListOf(4, 5, 6).asList().toObservable().map { v -> "hello_$v" }.subscribe(received())
    verify(a, times(1)).received("hello_4")
    verify(a, times(1)).received("hello_5")
    verify(a, times(1)).received("hello_6")
  }

  @Test
  fun testMerge() {
    fastListOf(fastListOf(1, 2, 3).toObservable(),
               fastListOf(6.toSingletonObservable(),
                          NullPointerException().toObservable<Int>(),
                          7.toSingletonObservable()
                         ).merge(),
               fastListOf(4, 5).toObservable())
        .merge().subscribe(received()) { e -> a.error(e) }

    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
    verify(a, times(0)).received(4)
    verify(a, times(0)).received(5)
    verify(a, times(1)).received(6)
    verify(a, times(0)).received(7)
    verify(a, times(1)).error(any(NullPointerException::class.java))
  }

  @Test
  fun testScriptWithMaterialize() {
    TestFactory().observable.materialize().subscribe(received())
    verify(a, times(2)).received(any(Notification::class.java))  // onNext : hello_1, onCompleted
  }

  @Test
  fun testScriptWithMerge() {
    val factory = TestFactory()
    (factory.observable.mergeWith(factory.observable)).subscribe(received())
    verify(a, times(1)).received("hello_1")
    verify(a, times(1)).received("hello_2")
  }

  @Test
  fun testStartWith() {
    val list = intArrayListOf(10, 11, 12, 13, 14)
    val startList = intArrayListOf(1, 2, 3, 4, 5)

    assertThat(list.asList().toObservable().startWith(0).count().toBlocking().single()).isEqualTo(6)
    assertThat(list.asList().toObservable().startWith(startList.asList()).count().toBlocking().single()).isEqualTo(10)
  }

  @Test
  fun testScriptWithOnNext() {
    TestFactory().observable.subscribe(received())
    verify(a, times(1)).received("hello_1")
  }

  @Test
  fun testSkipTake() {
    intArrayListOf(1, 2, 3).asList().toObservable().skip(1).take(1).subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test
  fun testSkip() {
    intArrayListOf(1, 2, 3).asList().toObservable().skip(2).subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(0)).received(2)
    verify(a, times(1)).received(3)
  }

  @Test
  fun testTake() {
    intArrayListOf(1, 2, 3).asList().toObservable().take(2).subscribe(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test
  fun testTakeLast() {
    TestFactory().observable.takeLast(1).subscribe(received())
    verify(a, times(1)).received("hello_1")

    intArrayListOf(1, 2, 3).asList().toObservable().takeLast(1).subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(0)).received(2)
    verify(a, times(1)).received(3)
  }

  @Test
  fun testTakeWhile() {
    fastListOf(1, 2, 3).toObservable().takeWhile { x -> x < 3 }.subscribe(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test
  fun testTakeWhileWithIndex() {
    fastListOf(1, 2, 3).toObservable()
        .takeWhile { x -> x < 3 }
        .zipWith((0 .. Int.MAX_VALUE).toObservable()) { x, i -> x }
        .subscribe(received())

    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test
  fun testToSortedList() {
    TestFactory().numbers.toSortedList().subscribe(received())
    verify(a, times(1)).received(fastListOf(1, 2, 3, 4, 5))
  }


  @Test
  fun testForEach() {
    observable(AsyncObservable()).toBlocking().forEach(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
  }

  @Test(expected = RuntimeException::class)
  fun testForEachWithError() {
    observable(AsyncObservable()).toBlocking().forEach { throw RuntimeException("err") }
    fail("We expect an exception to be thrown")
  }

  @Test
  fun testLastOrDefault() {
    val last = listOf("one", "two").toObservable().toBlocking().lastOrDefault("default") { x -> x.length == 3 }
    assertThat(last).isEqualTo("two")
    val default = listOf("one", "two").toObservable().toBlocking().lastOrDefault("default") { x -> x.length > 3 }
    assertThat(default).isEqualTo("default")
  }

  @Test
  fun testDefer() {
    deferredObservable { listOf(1, 2).toObservable() }.subscribe(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
  }

  @Test
  fun testAll() {
    intArrayListOf(1, 2, 3).asList().toObservable().all { x -> x > 0 }.subscribe(received())
    verify(a, times(1)).received(true)
    verify(a, times(0)).received(false)

    intArrayListOf(1, 2, 3).asList().toObservable().all { x -> x < 0 }.subscribe(received())
    verify(a, times(1)).received(false)
  }

  @Test
  fun testZip() {
    val o1 = intArrayListOf(1, 2, 3).asList().toObservable()
    val o2 = intArrayListOf(4, 5, 6).asList().toObservable()
    val o3 = intArrayListOf(7, 8, 9).asList().toObservable()

    val values = Observable.zip(o1, o2, o3) { a, b, c -> intArrayListOf(a, b, c) }.toList().toBlocking().single()
    assertThat(values[0]).isEqualTo(intArrayListOf(1, 4, 7))
    assertThat(values[1]).isEqualTo(intArrayListOf(2, 5, 8))
    assertThat(values[2]).isEqualTo(intArrayListOf(3, 6, 9))
  }

  @Test
  fun testSwitchOnNext() {
    val testScheduler = TestScheduler()
    val worker = testScheduler.createWorker()

    val observable = observable<Observable<Long>> { s ->
      fun at(delay: Long, func: () -> Unit) {
        worker.schedule({ func() }, delay, TimeUnit.MILLISECONDS)
      }

      val first = Observable.interval(5, TimeUnit.MILLISECONDS, testScheduler).take(3)
      at(0, { s.onNext(first) })

      val second = Observable.interval(5, TimeUnit.MILLISECONDS, testScheduler).take(3)
      at(11, { s.onNext(second) })

      at(40, { s.onCompleted() })
    }

    observable.switchOnNext().subscribe(received())

    val inOrder = Mockito.inOrder(a)

    testScheduler.advanceTimeTo(10, TimeUnit.MILLISECONDS)
    inOrder.verify(a, times(1)).received(0L)
    inOrder.verify(a, times(1)).received(1L)

    testScheduler.advanceTimeTo(40, TimeUnit.MILLISECONDS)
    inOrder.verify(a, times(1)).received(0L)
    inOrder.verify(a, times(1)).received(1L)
    inOrder.verify(a, times(1)).received(2L)
    inOrder.verifyNoMoreInteractions()
  }
}