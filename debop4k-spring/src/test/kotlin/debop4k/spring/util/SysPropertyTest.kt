package debop4k.spring.util

import debop4k.spring.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SysPropertyTest : AbstractSpringTest() {

  @Test
  fun setAndGetProperty() {
    sysproperty["activeProfile"] = "test"
    assertThat(sysproperty["activeProfile"]).isEqualTo("test")
  }
}