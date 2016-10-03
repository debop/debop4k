/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.reactive

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.*
import rx.Notification
import rx.Observable
import rx.Observable.OnSubscribe
import rx.observables.GroupedObservable
import kotlin.test.fail

class BasicRectiveTest : AbstractReactiveTest() {

  @Test fun testCreate() {
    Observable.create(OnSubscribe<String> { subscriber ->
      subscriber.onNext("Hello")
      subscriber.onCompleted()
    }).subscribe { result -> a.received(result) }

    verify(a, times(1)).received("Hello")
  }

  @Test fun testFilter() {
    Observable.from(listOf(1, 2, 3)).filter { it >= 2 }.subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
  }

  @Test fun testLast() {
    assertThat(Observable.from(listOf("one", "two", "three")).toBlocking().last()).isEqualTo("three")
  }

  @Test fun testLastWithPredicate() {
    assertThat(Observable.from(listOf("one", "two", "three")).toBlocking().last { x -> x.length == 3 }).isEqualTo("two")
  }

  @Test fun testMap1() {
    Observable.just(1).map { v -> "hello_$v" }.subscribe(received())
    verify(a, times(1)).received("hello_1")
  }

  @Test fun testMap2() {
    Observable.from(listOf(1, 2, 3)).map { v -> "hello_$v" }.subscribe(received())
    verify(a, times(1)).received("hello_1")
    verify(a, times(1)).received("hello_2")
    verify(a, times(1)).received("hello_3")
  }

  @Test fun testMaterialize() {
    Observable.from(listOf(1, 2, 3)).materialize().subscribe(received())
    verify(a, times(4)).received(any(Notification::class.java))  // onNext 3, onComplete 1
    verify(a, times(0)).error(any(Exception::class.java))
  }

  @Test fun testMerge() {
    Observable.merge(
        Observable.from(listOf(1, 2, 3)),
        Observable.merge(
            Observable.just(6),
            Observable.error(NullPointerException()),
            Observable.just(7)
                        ),
        Observable.from(listOf(4, 5))
                    )
        .subscribe(received()) { e: Throwable -> a.error(e) }

    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
    verify(a, times(0)).received(4)
    verify(a, times(0)).received(5)
    verify(a, times(1)).received(6)
    verify(a, times(0)).received(7)
    verify(a, times(1)).error(any(NullPointerException::class.java))
  }

  @Test fun testScriptWithMaterialize() {
    // counter = 1 이다 ...
    TestFactory().observable.materialize().subscribe(received())
    verify(a, times(2)).received(any(Notification::class.java))  // onNext : "hello_xxx", onCompleted
  }

  @Test fun testFromWithIterable() {
    val list = listOf(1, 2, 3, 4, 5)
    assertThat(Observable.from(list).count().toBlocking().single()).isEqualTo(5)
  }

  @Test fun testFromWithObjects() {
    val list = listOf(1, 2, 3, 4, 5)
    assertThat(Observable.from(listOf(list, 6)).count().toBlocking().single()).isEqualTo(2)
  }

  @Test fun testStartWith() {
    val list = listOf(10, 11, 12, 13, 14)
    val startList = listOf(1, 2, 3, 4, 5)

    // 0, 10, 11, 12, 13, 14
    assertThat(Observable.from(list).startWith(0).count().toBlocking().single()).isEqualTo(6)
    // 13, 10, 11, 12, 13, 14
    assertThat(Observable.from(list).startWith(13).count().toBlocking().single()).isEqualTo(6)

    // 1,2,3,4,5,10,11,12,13,14
    assertThat(Observable.from(list).startWith(startList).count().toBlocking().single()).isEqualTo(10)
  }

  @Test fun testScriptWithOnNext() {
    TestFactory().observable.subscribe(received())
    verify(a, times(1)).received(contains("hello_"))
  }

  @Test fun testSkipTake() {
    Observable.from(listOf(1, 2, 3)).skip(1).take(1).subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test fun testSkip() {
    Observable.from(listOf(1, 2, 3)).skip(2).subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(0)).received(2)
    verify(a, times(1)).received(3)
  }

  @Test fun testTake() {
    Observable.from(listOf(1, 2, 3)).take(2).subscribe(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test fun testTakeLast() {
    TestFactory().observable.takeLast(1).subscribe(received())
    verify(a, times(1)).received(contains("hello_"))
  }

  @Test fun testTakeWhile() {
    Observable.from(listOf(1, 2, 3)).takeWhile { it < 3 }.subscribe(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test fun testTakeWhileWithIndex() {
    Observable.from(listOf(1, 2, 3)).takeWhile { it < 3 }
        .zipWith(Observable.range(0, Int.MAX_VALUE)) { x, i -> log.debug("x=$x, i=$i"); x }
        .subscribe(received())

    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(0)).received(3)
  }

  @Test fun toSortedList() {
    TestFactory().numbers.toSortedList().subscribe(received())
    verify(a, times(1)).received(listOf(1, 2, 3, 4, 5))

    assertThat(TestFactory().numbers.toSortedList().toBlocking().single()).isEqualTo(listOf(1, 2, 3, 4, 5))
  }

  @Test fun testForEach() {
    Observable.create(AsyncObservable()).toBlocking().forEach(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
    verify(a, times(0)).received(4)
  }

  @Test(expected = RuntimeException::class)
  fun testForEachWithError() {
    Observable.create(AsyncObservable()).toBlocking().forEach { throw RuntimeException("err") }
    fail("We expect an exception to be thrown")
  }

  @Test fun testLastOrDefault() {
    val obs = Observable.just("one", "two")

    assertThat(obs.toBlocking().lastOrDefault("unknown") { x -> x.length == 3 }).isEqualTo("two")
    assertThat(obs.toBlocking().lastOrDefault("unknown") { x -> x.length > 3 }).isEqualTo("unknown")
  }

  @Test(expected = IllegalArgumentException::class)
  fun testSingle() {
    val one = Observable.just("one").toBlocking().single { x -> x.length == 3 }
    assertThat(one).isEqualTo("one")

    // list 가 되므로 single 을 쓸 수 없다.
    Observable.just("one", "two").toBlocking().single { x -> x.length == 3 }

    // list 가 되므로 single 을 쓸 수 없다.
    Observable.from(listOf("one", "two")).toBlocking().single { x -> x.length == 3 }

    fail()
  }

  @Test fun testDefer() {
    Observable.defer { Observable.just(1, 2) }.subscribe(received())
    verify(a, times(1)).received(1)
    verify(a, times(1)).received(1)

    deferredObservable { Observable.just(3, 4) }.subscribe(received())
    verify(a, times(1)).received(3)
    verify(a, times(1)).received(4)
  }

  @Test fun testAll() {
    Observable.from(listOf(1, 2, 3)).all { it > 0 }.subscribe(received())
  }

  @Test fun testZip() {
    val o1 = Observable.from(listOf(1, 2, 3))
    val o2 = Observable.from(listOf(4, 5, 6))
    val o3 = Observable.from(listOf(7, 8, 9))

    val values = Observable.zip(o1, o2, o3) { a, b, c -> listOf(a, b, c) }.toList().toBlocking().single()

    assertThat(values[0]).isEqualTo(listOf(1, 4, 7))
    assertThat(values[1]).isEqualTo(listOf(2, 5, 8))
    assertThat(values[2]).isEqualTo(listOf(3, 6, 9))
  }

  @Test fun testZipWithIterable() {
    val o1 = Observable.from(listOf(1, 2, 3))
    val o2 = Observable.from(listOf(4, 5, 6))
    val o3 = Observable.from(listOf(7, 8, 9))

    val values = Observable.zip(listOf(o1, o2, o3)) { args -> listOf(*args) }.toList().toBlocking().single()
    assertThat(values[0]).isEqualTo(listOf(1, 4, 7))
    assertThat(values[1]).isEqualTo(listOf(2, 5, 8))
    assertThat(values[2]).isEqualTo(listOf(3, 6, 9))
  }

  @Test fun testGroupBy() {
    var count = 0

    Observable.from(listOf("one", "two", "three", "four", "five", "six"))
        .groupBy { s -> s.length }
        .flatMap { groupObservable: GroupedObservable<Int, String> ->
          groupObservable.map { s ->
            "Value: `$s` in Group ${groupObservable.key}"
          }
        }.toBlocking().forEach { s -> println(s); count++ }

    assertThat(count).isEqualTo(6)
  }


}