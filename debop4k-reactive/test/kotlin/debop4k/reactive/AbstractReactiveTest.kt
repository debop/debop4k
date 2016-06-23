package debop4k.reactive

import debop4k.core.utils.uninitialized
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.slf4j.LoggerFactory
import rx.Observable

abstract class AbstractReactiveTest {

  val log = LoggerFactory.getLogger(javaClass)

  @Mock var a: ScriptAssertion = uninitialized()
  @Mock var w: Observable<Int> = uninitialized()

  @Before fun setup() {
    MockitoAnnotations.initMocks(this)
  }

  fun <T> received() = { result: T? -> a.received(result) }

  interface ScriptAssertion {
    fun error(e: Throwable)
    fun received(e: Any?)
  }

}