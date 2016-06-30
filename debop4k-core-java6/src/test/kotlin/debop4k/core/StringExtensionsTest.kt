package debop4k.core

import org.assertj.core.api.Assertions
import org.junit.Test

class StringExtensionsTest : AbstractCoreTest() {

  @Test
  fun testIsNull() {
    val a: String? = null
    Assertions.assertThat(a.isNull()).isTrue()

    val b: String? = "non null"
    Assertions.assertThat(b.isNull()).isFalse()

    log.debug("Success isNull test")
  }

  @Test
  fun testNonEmpty() {
    val a: String? = null
    Assertions.assertThat(a.nonEmpty()).isFalse()

    val b: String? = "non null"
    Assertions.assertThat(b.nonEmpty()).isTrue()

    log.debug("Success nonEmpty test")
  }
}
