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

  @Mock var a: ScriptAssertion = uninitialized()
  @Mock var w: Observable<Int> = uninitialized()

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