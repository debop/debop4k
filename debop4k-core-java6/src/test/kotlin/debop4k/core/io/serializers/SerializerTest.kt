package debop4k.core.io.serializers

import debop4k.core.AbstractCoreTest
import debop4k.core.collections.fastListOf
import debop4k.core.io.serializers.java6.FstJava6Serializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.Serializable

/**
 * @author sunghyouk.bae@gmail.com
 */
class SerializerTest : AbstractCoreTest() {

  private val serializers = fastListOf(BinarySerializer(), FstJava6Serializer())

  data class YearWeek(val year: Int, val month: Int) : Serializable

  @Test fun serializeValueObject() {
    val yearWeek = YearWeek(2016, 7)
    serializers.forEach { serializer ->
      val copied = serializer.deserialize<YearWeek>(serializer.serialize(yearWeek))
      assertThat(copied).isEqualTo(yearWeek)
    }
  }
}