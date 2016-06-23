package debop4k.reactive

import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import rx.Observable
import rx.Observable.OnSubscribe

class BasicRectiveTest : AbstractReactiveTest() {

  @Test fun testCreate() {
    Observable.create(OnSubscribe<String> { subscriber ->
      subscriber.onNext("Hello")
      subscriber.onCompleted()
    })
        .subscribe { result -> a.received(result) }

    verify(a, times(1)).received("Hello")
  }

  @Test fun testFilter() {
    log.debug("Filter test")

    Observable.from(listOf(1, 2, 3)).filter { it >= 2 }.subscribe(received())
    verify(a, times(0)).received(1)
    verify(a, times(1)).received(2)
    verify(a, times(1)).received(3)
  }

}