/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.io.serializers

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.collections.eclipseCollections.fastListOf
import debop4k.fst.FstSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.Serializable


class SerializerTest : AbstractCoreKotlinTest() {

  private val serializers = fastListOf(BinarySerializer(), FstSerializer())

  data class YearWeek(val year: Int, val month: Int) : Serializable


  @Test fun `serialize ValueObject`() {
    val yearWeek = YearWeek(2016, 7)
    serializers.forEach { serializer ->
      val copied = serializer.deserialize<YearWeek>(serializer.serialize(yearWeek))
      assertThat(copied).isEqualTo(yearWeek)
      log.debug("yearWeek={}, copied={}", yearWeek, copied)
    }
  }
}