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

package debop4k.units.java;

import org.junit.Test;

import static debop4k.units.Storage.parse;
import static debop4k.units.Storages.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StorageTest extends AbstractUnitTest {

  @Test
  public void convertStorageUnit() {
    assertThat(toBytes(1).inBytes()).isEqualTo(1);
    assertThat(toKBytes(1).inKBytes()).isEqualTo(1);
    assertThat(toMBytes(1).inMBytes()).isEqualTo(1);
    assertThat(toGBytes(1).inGBytes()).isEqualTo(1);

    assertThat(toBytes(1).inKBytes()).isEqualTo(1.0 / 1024.0);
    assertThat(toBytes(1 << 10).inKBytes()).isEqualTo(1);
    assertThat(toBytes(1 << 20).inKBytes()).isEqualTo(1024);
    assertThat(toKBytes(1).inBytes()).isEqualTo(1L << 10);
    assertThat(toMBytes(1).inBytes()).isEqualTo(1L << 20);
    assertThat(toGBytes(1).inBytes()).isEqualTo(1L << 30);
    assertThat(toTBytes(1).inBytes()).isEqualTo(1L << 40);
    assertThat(toPBytes(1).inBytes()).isEqualTo(1L << 50);
    assertThat(toXBytes(1).inBytes()).isEqualTo(1L << 60);

  }

  @Test
  public void testToHuman() {
    assertThat(toBytes(1).toHuman()).isEqualTo("1 B");
    assertThat(toBytes(1000).toHuman()).isEqualTo("1000 B");
    assertThat(toBytes(1024).toHuman()).isEqualTo("1.0 KB");

    assertThat(toKBytes(1).toHuman()).isEqualTo("1.0 KB");

    assertThat(toBytes(1800).toHuman()).isEqualTo("1.8 KB");
    assertThat(toBytes(2000).toHuman()).isEqualTo("2.0 KB");

    assertThat(toMBytes(1.2).toHuman()).isEqualTo("1.2 MB");
    assertThat(toGBytes(1.2).toHuman()).isEqualTo("1.2 GB");
    assertThat(toTBytes(1.2).toHuman()).isEqualTo("1.2 TB");
    assertThat(toPBytes(1.2).toHuman()).isEqualTo("1.2 PB");
    assertThat(toXBytes(1.2).toHuman()).isEqualTo("1.2 XB");
  }

  @Test
  public void testParsing() {

    assertThat(parse("12.0 KB").inBytes()).isEqualTo(toKBytes(12.0).inBytes());
    assertThat(parse("1.7 MB").inBytes()).isEqualTo(toMBytes(1.7).inBytes());
  }

}
