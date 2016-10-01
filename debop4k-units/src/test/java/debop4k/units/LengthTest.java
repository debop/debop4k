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

package debop4k.units;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.units.Length.parse;
import static debop4k.units.Lengths.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LengthTest extends AbstractMeasuresTest {

  @Test
  public void convertDistanceUnit() {
    assertThat(millimeter(1).inMillimeter()).isEqualTo(1);
    assertThat(centimeter(1).inCentimeter()).isEqualTo(1);
    assertThat(meter(1).inMeter()).isEqualTo(1);
    assertThat(kilometer(1).inKilometer()).isEqualTo(1);

    assertThat(millimeter(1000).inMeter()).isEqualTo(1);
    assertThat(millimeter(1).inCentimeter()).isEqualTo(0.1);
    assertThat(millimeter(1).inMeter()).isEqualTo(0.001);
    assertThat(meter(1).inMillimeter()).isEqualTo(1000.0);
    assertThat(kilometer(1).inMeter()).isEqualTo(1000.0);
  }

  @Test
  public void toHuman() {
    assertThat(millimeter(900).toHuman()).isEqualTo("90.0 cm");
    assertThat(kilometer(10.5).toHuman()).isEqualTo("10.5 km");

    assertThat(meter(10050).toHuman()).isEqualTo("10.1 km");
    assertThat(meter(Integer.MAX_VALUE).toHuman()).isEqualTo("2147483.6 km");
  }

  @Test
  public void parsing() {

    assertThat(parse("142 mm").inMeter()).isEqualTo(millimeter(142).inMeter(), offset);
    assertThat(parse("123456.7 cm").inMeter()).isEqualTo(centimeter(123456.7).inMeter(), offset);
    assertThat(parse("0.1 m").inMeter()).isEqualTo(meter(0.1).inMeter(), offset);
    assertThat(parse("10000.1 m").inMeter()).isEqualTo(meter(10000.1).inMeter(), offset);
    assertThat(parse("78.4 km").inMeter()).isEqualTo(kilometer(78.4).inMeter(), offset);
  }

  @Test(expected = NumberFormatException.class)
  public void parse100bottles() {
    parse("100.bottles");
  }

  @Test(expected = NumberFormatException.class)
  public void parse100meter() {
    parse("100 meter");
  }

  @Test(expected = NumberFormatException.class)
  public void parseMalformat() {
    parse("100.0.0.0.meter");
  }

  @Test
  public void negative() {
    assertThat(meter(-132).inMeter()).isEqualTo(-132, offset);
    assertThat(kilometer(-2).toHuman()).isEqualTo("-2.0 km");
  }

  @Test
  public void sameHashCode() {
    Length i = kilometer(4);
    Length j = kilometer(4);
    Length k = kilometer(4.0);

    assertThat(i.hashCode()).isEqualTo(j.hashCode());
    assertThat(j.hashCode()).isEqualTo(k.hashCode());
  }

  @Test
  public void compare() {
    assertThat(kilometer(4.1)).isGreaterThan(kilometer(3.9));
    assertThat(meter(-1.2)).isLessThan(meter(-0.2));
    assertThat(meter(-1.2)).isGreaterThan(meter(-2.5));
  }

  @Test
  public void arithmetics() {
    assertThat(kilometer(1).plus(kilometer(2))).isEqualTo(meter(3000));
    assertThat(kilometer(1).minus(kilometer(2))).isEqualTo(kilometer(-1));
    assertThat(kilometer(4).times(2)).isEqualTo(kilometer(8));
    assertThat(kilometer(4).div(2)).isEqualTo(kilometer(2));
  }

//  @Test
//  public void convertUnit() {
//    assertThat(kilometer(1.0).inMile()).isEqualTo(1000.0 / 1609.344);
//    assertThat(mile(kilometer(1.0).inKilometer()).inMeter()).isEqualTo(1609.344);
//  }
}
