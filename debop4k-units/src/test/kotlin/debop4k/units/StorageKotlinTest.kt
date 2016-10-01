/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.units

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StorageKotlinTest : AbstractUnitKotlinTest() {

  @Test fun `convert Storage Unit`() {

    assertThat(100.bytes().inBytes()).isEqualTo(100.0)
    assertThat(100.kilobytes().inBytes()).isEqualTo(100.0 * 1024)
    assertThat(100.megabytes().inBytes()).isEqualTo(100.0 * 1024 * 1024)

    assertThat(100.gigabytes().inKBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.terabytes().inMBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.petabytes().inGBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.exabytes().inTBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.zetabytes().inPBytes()).isEqualTo(100.0 * 1024 * 1024)
    assertThat(100.yotabytes().inXBytes()).isEqualTo(100.0 * 1024 * 1024)
  }

  @Test fun toHuman() {
    assertThat(100.bytes().toHuman()).isEqualTo("100 B")
    assertThat(123.kilobytes().toHuman()).isEqualTo("123.0 KB")
    assertThat(123.43.megabytes().toHuman()).isEqualTo("123.4 MB")
    assertThat(12.59.gigabytes().toHuman()).isEqualTo("12.6 GB")
  }

  @Test fun `parsing Storage String`() {
    assertThat(Storage.parse("100 B")).isEqualTo(100.bytes())
    assertThat(Storage.parse("17.5 KB")).isEqualTo(17.5.kilobytes())
    assertThat(Storage.parse("8.1 MB")).isEqualTo(8.1.megabytes())
  }

  @Test fun negative() {
    assertThat(-Storage(100.0)).isEqualTo(-100.bytes())
  }

  @Test fun `arithmetic - plus minus`() {
    val a = Storage(100.0)
    val b = Storage(200.0)
    assertThat(a + b).isEqualTo(300.bytes())
    assertThat(b - a).isEqualTo(100.bytes())
  }

  @Test fun `arithmetic - times div`() {
    val a = Storage(100.0)
    val b = Storage(200.0)

    assertThat(a * 2).isEqualTo(b)
    assertThat(b / 2).isEqualTo(a)
    assertThat(2 * a).isEqualTo(b)
  }

  @Test fun compare() {
    assertThat(1.7.kilobytes().compareTo(1.7.bytes())).isGreaterThan(0)
    assertThat(1.2.megabytes().compareTo(1.2.kilobytes())).isGreaterThan(0)
    assertThat(1.1.megabytes().compareTo(0.9.gigabytes())).isLessThan(0)

    assertThat(1.7.kilobytes() > 1.7.bytes()).isTrue()
    assertThat(1.2.megabytes() > 1.2.kilobytes()).isTrue()
    assertThat(1.1.megabytes() < 0.9.gigabytes()).isTrue()
  }

}