package debop4k.reactive

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Observable

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
  fun testFold() {

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

}