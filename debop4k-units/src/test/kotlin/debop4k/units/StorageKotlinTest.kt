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

package debop4k.units

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StorageKotlinTest : AbstractUnitKotlinTest() {

  @Test fun `convert Storage Unit`() {

    assertThat(100.toBytes().inBytes()).isEqualTo(100.0)
    assertThat(100.toKBytes().inBytes()).isEqualTo(100.0 * 1024)
    assertThat(100.toMBytes().inBytes()).isEqualTo(100.0 * 1024 * 1024)

    assertThat(100.toGBytes().inKBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.toTBytes().inMBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.toPBytes().inGBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.toXBytes().inTBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.toZBytes().inPBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.toYBytes().inXBytes()).isEqualTo(100.0 * 1024 * 1024)
  }

  @Test fun toHuman() {
    assertThat(100.toBytes().toHuman()).isEqualTo("100 B")
    assertThat(123.toKBytes().toHuman()).isEqualTo("123.0 KB")
    assertThat(123.43.toMBytes().toHuman()).isEqualTo("123.4 MB")
    assertThat(12.59.toGBytes().toHuman()).isEqualTo("12.6 GB")
  }

  @Test fun `parsing Storage String`() {
    assertThat(Storage.parse("100 B")).isEqualTo(100.toBytes())
    assertThat(Storage.parse("17.5 KB")).isEqualTo(17.5.toKBytes())
    assertThat(Storage.parse("8.1 MB")).isEqualTo(8.1.toMBytes())
  }

  @Test fun negative() {
    assertThat(-Storage(100.0)).isEqualTo(-100.toBytes())
  }

  @Test fun `arithmetic - plus minus`() {
    val a = Storage(100.0)
    val b = Storage(200.0)
    assertThat(a + b).isEqualTo(300.toBytes())
    assertThat(b - a).isEqualTo(100.toBytes())
  }

  @Test fun `arithmetic - times div`() {
    val a = Storage(100.0)
    val b = Storage(200.0)

    assertThat(a * 2).isEqualTo(b)
    assertThat(b / 2).isEqualTo(a)
    assertThat(2 * a).isEqualTo(b)
  }

  @Test fun compare() {
    assertThat(1.7.toKBytes().compareTo(1.7.toBytes())).isGreaterThan(0)
    assertThat(1.2.toMBytes().compareTo(1.2.toKBytes())).isGreaterThan(0)
    assertThat(1.1.toMBytes().compareTo(0.9.toGBytes())).isLessThan(0)

    assertThat(1.7.toKBytes() > 1.7.toBytes()).isTrue()
    assertThat(1.2.toMBytes() > 1.2.toKBytes()).isTrue()
    assertThat(1.1.toMBytes() < 0.9.toGBytes()).isTrue()
  }

}