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
import rx.Observable
import rx.exceptions.OnErrorNotImplementedException
import rx.observers.TestSubscriber
import java.util.concurrent.atomic.*

/**
 * @author sunghyouk.bae@gmail.com
 */
class ObservableTest : AbstractReactiveTest() {

  @Test
  fun testCreation() {
    val o0 = emptyObservable<Int>()

    observable<Int> { s ->
      s.onNext(1)
      s.onNext(777)
      s.onCompleted()
    }
        .toList()
        .forEach { assertThat(it).isEqualTo(listOf(1, 777)) }

    val o1: Observable<Int> = listOf(1, 2, 3).toObservable()
    val o2: Observable<List<Int>> = listOf(1, 2, 3).toSingletonObservable()

    val o3 = deferredObservable { observable<Int> { s -> s.onNext(1) } }
    val o4 = Array(3) { 0 }.toObservable()
    val o5 = IntArray(3).toObservable()

    assertThat(o0).isNotNull()
    assertThat(o1).isNotNull()
    assertThat(o2).isNotNull()
    assertThat(o3).isNotNull()
    assertThat(o4).isNotNull()
    assertThat(o5).isNotNull()
  }

  @Test
  fun testFoldAndMap() {

    val result = observable<String> { subscriber ->
      subscriber.onNext("H")
      subscriber.onNext("e")
      subscriber.onNext("l")
      subscriber.onNext("l")
      subscriber.onNext("o")
      subscriber.onCompleted()
    }.filter { it.isNotBlank() }
        .fold(StringBuilder()) { sb, s -> sb.append(s) }
        .map { it.toString() }
        .toBlocking()
        .single()

    assertThat(result).isEqualTo("Hello")
  }

  @Test
  fun iteratorObservable() {
    val list = listOf(1, 2, 3).iterator().toObservable().toList().toBlocking().single()
    assertThat(list).isEqualTo(listOf(1, 2, 3))
  }

  @Test
  fun intProgressionStep1Empty() {
    val range = (1 .. 1).toObservable().toList().toBlocking().first()
    assertThat(range).isEqualTo(listOf(1))
  }

  @Test
  fun intProgressionStep1() {
    val range = (1 .. 10).toObservable().toList().toBlocking().first()
    assertThat(range).isEqualTo((1 .. 10).toList())
  }

  @Test
  fun intProgressionDownTo() {
    val range = (10 downTo 1).toObservable().toList().toBlocking().first()
    assertThat(range).isEqualTo((10 downTo 1).toList())
  }

  @Test
  fun intProgressionOverflow() {
    val range = (-10 .. Int.MAX_VALUE)
        .toObservable()
        .skip(Int.MAX_VALUE)
        .map { Int.MAX_VALUE - it }
        .toList()
        .toBlocking()
        .first()
    assertThat(range).isEqualTo((10 downTo 0).toList())
  }

  @Test
  fun filterNotNull() {
    val o = listOf(1, null).toObservable().filterNotNull()
    o.toList().forEach {
      assertThat(it).isEqualTo(listOf(1))
    }
  }

  @Test
  fun requireNoNullsWithoutNulls() {
    (listOf(1, 2) as List<Int?>).toObservable().requireNoNulls().subscribe()
  }

  @Test(expected = OnErrorNotImplementedException::class)
  fun requireNoNullsWithNulls() {
    listOf(1, null).toObservable().requireNoNulls().subscribe()
  }

  @Test
  fun testWithIndex() {
    val expected = listOf(IndexedValue(0, "a"),
                          IndexedValue(1, "b"),
                          IndexedValue(2, "c"))

    listOf("a", "b", "c")
        .toObservable()
        .withIndex()
        .toList()
        .forEach {
          assertThat(it).isEqualTo(expected)
        }
  }

  @Test fun `withIndex() 는 복수의 subscriber에서 공유되면 안됩니다`() {
    val o: Observable<IndexedValue<String>> = listOf("a", "b", "c").toObservable().withIndex()

    val subscriber1 = TestSubscriber<IndexedValue<String>>()
    val subscriber2 = TestSubscriber<IndexedValue<String>>()

    o.subscribe(subscriber1)
    o.subscribe(subscriber2)

    subscriber1.awaitTerminalEvent()
    subscriber1.assertValues(IndexedValue(0, "a"), IndexedValue(1, "b"), IndexedValue(2, "c"))

    subscriber2.awaitTerminalEvent()
    subscriber2.assertValues(IndexedValue(0, "a"), IndexedValue(1, "b"), IndexedValue(2, "c"))
  }

  @Test
  fun testFold() {
    listOf(1, 2, 3).toObservable().fold(0) { acc, n -> acc + n }.single().forEach {
      assertThat(it).isEqualTo(6)
    }
  }

  @Test
  fun kotlinSequence() {
    generateSequence(0) { it + 1 }.toObservable().take(10).toList().forEach {
      assertThat(it).isEqualTo((0 .. 9).toList())
    }
  }

  @Test
  fun infinite_iterable() {
    val generated = AtomicInteger()
    generateSequence { generated.incrementAndGet() }
        .toObservable()
        .take(100)
        .toList()
        .subscribe()

    assertThat(generated.get()).isEqualTo(100)
  }

  @Test
  fun testFlatMapSequence() {
    val actual = listOf(1, 2, 3)
        .toObservable()
        .flatMapSequence { listOf(it, it + 1, it + 2).asSequence() }
        .toList()
        .toBlocking()
        .single()

    val expected = listOf(1, 2, 3, 2, 3, 4, 3, 4, 5)

    assertThat(actual).isEqualTo(expected)
  }

  @Test
  fun testCombineLatest() {
    val list = listOf(1, 2, 3, 2, 3, 4, 3, 4, 5)

    val actual = list.map { it.toSingletonObservable() }.combineLatest { it }.toBlocking().first()

    assertThat(actual).isEqualTo(list)
  }

  @Test
  fun testZip() {
    val list = listOf(1, 2, 3, 2, 3, 4, 3, 4, 5)
    val actual = list.map { it.toSingletonObservable() }.zip { it }.toBlocking().first()
    assertThat(actual).isEqualTo(list)
  }

  @Test
  fun testCast() {
    val source = Observable.just<Any>(1, 2)
    val observable = source.cast<Int>()
    val subscriber = TestSubscriber<Int>()

    observable.subscribe(subscriber)

    subscriber.apply {
      this.assertValues(1, 2)
      this.assertNoErrors()
      this.assertCompleted()
    }
  }

  @Test
  fun testCastWithWrongType() {
    val source = Observable.just<Any>(1, 2)
    val observable = source.cast<String>()
    val subscriber = TestSubscriber<Any>()

    observable.subscribe(subscriber)

    subscriber.assertError(ClassCastException::class.java)
  }


}