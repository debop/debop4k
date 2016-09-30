/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

package debop4k.core.utils;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static debop4k.core.utils.Convertx.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ConvertExTest extends AbstractCoreTest {

  @Test
  public void testAsChar() {
    Character one = 'A';
    Character empty = null;

    assertThat(asChar(one)).isEqualTo('A');
    assertThat(asChar(empty)).isEqualTo('\u0000');
    assertThat(asInt('A')).isEqualTo(Integer.valueOf('A'));

    assertThat(asChar(5000)).isEqualTo((char) asByte(5000));
  }

  @Test
  public void testAsByte() {
    Byte one = Byte.valueOf("1");
    Byte empty = null;

    assertThat(asByte(one)).isEqualTo((byte) 1);
    assertThat(asByte(empty)).isEqualTo((byte) 0);
    assertThat(asByte(12)).isEqualTo((byte) 12);
    assertThat(asByte("12")).isEqualTo((byte) 12);

    assertThat(asByte(5000)).isEqualTo((byte) 5000);
  }

  @Test
  public void testAsShort() {
    Short one = (short) 1;
    Short empty = null;

    assertThat(asShort(one)).isEqualTo((short) 1);
    assertThat(asShort(empty)).isEqualTo((short) 0);
    assertThat(asShort(12)).isEqualTo((short) 12);
    assertThat(asShort("12.2")).isEqualTo((short) 12);

    assertThat(asShort(5000)).isEqualTo((short) 5000);
  }

  @Test
  public void asInt111() {
    Object v = null;
    Integer a = (v != null) ? (Integer) v : 0;
    assertThat(a).isEqualTo(0);
  }

  @Test
  public void testAsInt() {
    Integer one = 1;
    Integer empty = null;

    assertThat(asInt(one)).isEqualTo(1);
    assertThat(asInt(empty)).isEqualTo(0);
    assertThat(asInt(12)).isEqualTo(12);
    assertThat(asInt("12.2")).isEqualTo(12);

    assertThat(asInt("12.abc")).isEqualTo(0);
    assertThat(asInt(5000L)).isEqualTo(5000);
  }

  @Test
  public void testAsLong() {
    Long one = 1L;
    Long empty = null;

    assertThat(asLong(one)).isEqualTo(1L);
    assertThat(asLong(empty)).isEqualTo(0L);
    assertThat(asLong(12)).isEqualTo(12L);
    assertThat(asLong("12.2")).isEqualTo(12L);

    assertThat(asLong("12.abc")).isEqualTo(0L);
    assertThat(asLong(5000)).isEqualTo(5000L);
  }

  @Test
  public void testAsFloat() {
    Float one = 1.0F;
    Float empty = null;

    assertThat(asFloat(one)).isEqualTo(1F);
    assertThat(asFloat(empty)).isEqualTo(0F);
    assertThat(asFloat(12)).isEqualTo(12F);
    assertThat(asFloat("12.12")).isEqualTo(12.12F);

    assertThat(asFloat("12.abc")).isEqualTo(0F);
    assertThat(asFloat(5000)).isEqualTo(5000F);
  }

  @Test
  public void testAsDouble() {
    Double one = 1.0D;
    Double empty = null;

    assertThat(asDouble(one)).isEqualTo(1D);
    assertThat(asDouble(empty)).isEqualTo(0D);
    assertThat(asDouble(12)).isEqualTo(12D);
    assertThat(asDouble("12.12")).isEqualTo(12.12D);

    assertThat(asDouble("12.abc")).isEqualTo(0D);
    assertThat(asDouble(5000)).isEqualTo(5000D);
  }

  @Test
  public void testAsBigInt() {
    BigInteger one = BigInteger.ONE;
    BigInteger empty = null;

    assertThat(asBigInt(one)).isEqualTo(BigInteger.ONE);
    assertThat(asBigInt(empty)).isEqualTo(BigInteger.ZERO);
    assertThat(asBigInt(12)).isEqualTo(BigInteger.valueOf(12L));
    assertThat(asBigInt("12.12")).isEqualTo(BigInteger.valueOf(12L));

    assertThat(asBigInt(5000)).isEqualTo(BigInteger.valueOf(5000L));
  }

  @Test
  public void testAsBigDecimal() {
    BigDecimal one = BigDecimal.ONE;
    BigDecimal empty = null;

    assertThat(asBigDecimal(one)).isEqualTo(BigDecimal.ONE);
    assertThat(asBigDecimal(empty)).isEqualTo(BigDecimal.ZERO);
    assertThat(asBigDecimal(12.0)).isEqualTo(BigDecimal.valueOf(12.0));
    assertThat(asBigDecimal("12.12")).isEqualTo(BigDecimal.valueOf(12.12D));

    assertThat(asBigDecimal(5000)).isEqualTo(BigDecimal.valueOf(5000.0));
  }

  @Test
  public void testAsDateTime() {
    DateTime today = DateTime.now().withTimeAtStartOfDay();
    DateTime empty = null;

    assertThat(asDateTime(today)).isEqualTo(DateTime.now().withTimeAtStartOfDay());
    assertThat(asDateTime(empty)).isEqualTo(empty);
    assertThat(asDateTime(0L)).isEqualTo(new DateTime(0L));
    assertThat(asDateTime("2000-10-14")).isEqualTo(new DateTime().withTimeAtStartOfDay().withDate(2000, 10, 14));
    assertThat(asDateTime("")).isEqualTo(empty);
    assertThat(asDateTime("abc")).isNull();
    assertThat(asDateTime("2000")).isEqualTo(new DateTime().withTimeAtStartOfDay().withDate(2000, 1, 1));
  }

  @Test
  public void testAsFloatFloor() {
    float one = 1.00123456F;
    float one1 = 1.011111F;
    float one5 = 1.0502345F;
    float one49 = 1.0499999999F;
    Float empty = null;

    assertThat(asFloatFloor(one, 2)).isEqualTo(1.00F);
    assertThat(asFloatFloor(one, 1)).isEqualTo(1.0F);

    assertThat(asFloatFloor(one1, 2)).isEqualTo(1.01F);
    assertThat(asFloatFloor(one1, 1)).isEqualTo(1.0F);

    assertThat(asFloatFloor(one5, 2)).isEqualTo(1.05F);
    assertThat(asFloatFloor(one5, 1)).isEqualTo(1.1F);

    assertThat(asFloatFloor(one49, 2)).isEqualTo(1.05F);
    assertThat(asFloatFloor(one49, 1)).isEqualTo(1.0F);

    assertThat(asFloatFloor(empty, 2)).isEqualTo(0.00F);
    assertThat(asFloatFloor(empty, 1)).isEqualTo(0.0F);
  }

  @Test
  public void testAsDoubleFloor() {
    Double one = 1.00123456D;
    Double one1 = 1.011111D;
    Double one5 = 1.0512341D;
    Double one49 = 1.0499999999D;
    Double empty = null;

    assertThat(asDoubleFloor(one, 2)).isEqualTo(1.00D);
    assertThat(asDoubleFloor(one, 1)).isEqualTo(1.0D);

    assertThat(asDoubleFloor(one1, 2)).isEqualTo(1.01D);
    assertThat(asDoubleFloor(one1, 1)).isEqualTo(1.0D);

    assertThat(asDoubleFloor(one5, 2)).isEqualTo(1.05D);
    assertThat(asDoubleFloor(one5, 1)).isEqualTo(1.1D);

    assertThat(asDoubleFloor(one49, 2)).isEqualTo(1.05D);
    assertThat(asDoubleFloor(one49, 1)).isEqualTo(1.0D);

    assertThat(asDoubleFloor(empty, 2)).isEqualTo(0.00D);
    assertThat(asDoubleFloor(empty, 1)).isEqualTo(0.0D);
  }

  @Test
  public void testAsFloatRound() {
    float one = 1.00123456F;
    float one1 = 1.011111F;
    float one5 = 1.0502345F;
    float one49 = 1.0499999999F;
    Float empty = null;

    assertThat(asFloatRound(one, 2)).isEqualTo(1.00F);
    assertThat(asFloatRound(one, 1)).isEqualTo(1.0F);

    assertThat(asFloatRound(one1, 2)).isEqualTo(1.01F);
    assertThat(asFloatRound(one1, 1)).isEqualTo(1.0F);

    assertThat(asFloatRound(one5, 2)).isEqualTo(1.05F);
    assertThat(asFloatRound(one5, 1)).isEqualTo(1.1F);

    assertThat(asFloatRound(one49, 2)).isEqualTo(1.05F);
    assertThat(asFloatRound(one49, 1)).isEqualTo(1.0F);

    assertThat(asFloatRound(empty, 2)).isEqualTo(0.00F);
    assertThat(asFloatRound(empty, 1)).isEqualTo(0.0F);
  }

  @Test
  public void testAsDoubleRound() {
    Double one = 1.00123456D;
    Double one1 = 1.011111D;
    Double one5 = 1.0512341D;
    Double one49 = 1.0499999999D;
    Double empty = null;

    assertThat(asDoubleRound(one, 2)).isEqualTo(1.00D);
    assertThat(asDoubleRound(one, 1)).isEqualTo(1.0D);

    assertThat(asDoubleRound(one1, 2)).isEqualTo(1.01D);
    assertThat(asDoubleRound(one1, 1)).isEqualTo(1.0D);

    assertThat(asDoubleRound(one5, 2)).isEqualTo(1.05D);
    assertThat(asDoubleRound(one5, 1)).isEqualTo(1.1D);

    assertThat(asDoubleRound(one49, 2)).isEqualTo(1.05D);
    assertThat(asDoubleRound(one49, 1)).isEqualTo(1.0D);

    assertThat(asDoubleRound(empty, 2)).isEqualTo(0.00D);
    assertThat(asDoubleRound(empty, 1)).isEqualTo(0.0D);
  }

}
