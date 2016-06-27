package debop4k.spring.core

import debop4k.spring.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertyResolver
import org.springframework.core.env.PropertySourcesPropertyResolver
import java.util.*
import kotlin.properties.Delegates

class PropertyResolverTest : AbstractSpringTest() {

  private var testProperties: Properties by Delegates.notNull()
  private var propertySources: MutablePropertySources by Delegates.notNull()
  private var propertyResolver: PropertyResolver by Delegates.notNull()

  @Before
  fun setup() {
    propertySources = MutablePropertySources()
    propertyResolver = PropertySourcesPropertyResolver(propertySources)
    testProperties = Properties()
    propertySources.addFirst(PropertiesPropertySource("testProperties", testProperties))
  }

  @Test
  fun getProperty() {

    assertThat(propertyResolver["foo"]).isNull()
    assertThat(propertyResolver["num"]).isNull()
    assertThat(propertyResolver["enabled"]).isNull()

    testProperties["foo"] = "bar"
    testProperties["num"] = 5
    testProperties["enabled"] = true

    assertThat(propertyResolver["foo"]).isEqualTo("bar")
    assertThat(propertyResolver["num", Int::class.java]).isEqualTo(5)
    assertThat(propertyResolver["enabled", Boolean::class.java]).isEqualTo(true)
  }

  @Test
  fun getPropertyWithDefaultValue() {
    assertThat(propertyResolver["foo", "myDefault"]).isEqualTo("myDefault")
    assertThat(propertyResolver["num", Int::class.java, 5]).isEqualTo(5)
    assertThat(propertyResolver["enabled", Boolean::class.java, true]).isEqualTo(true)

    testProperties["foo"] = "bar"
    testProperties["num"] = 42
    testProperties["enabled"] = true

    assertThat(propertyResolver["foo"]).isEqualTo("bar")
    assertThat(propertyResolver["num", Int::class.java]).isEqualTo(42)
    assertThat(propertyResolver["enabled", Boolean::class.java]).isEqualTo(true)
  }
}