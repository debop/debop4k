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

import static debop4k.units.Mass.parse;
import static debop4k.units.Masses.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MassTest extends AbstractMeasuresTest {

  @Test
  public void convertWeightUnit() {
    assertThat(milligram(1).inMilligram()).isEqualTo(1);
    assertThat(gram(1).inGram()).isEqualTo(1);
    assertThat(kilogram(1).inKilogram()).isEqualTo(1);

    assertThat(milligram(1000).inGram()).isEqualTo(1);
    assertThat(milligram(1).inGram()).isEqualTo(0.001);
    assertThat(gram(1).inMilligram()).isEqualTo(1000.0);
    assertThat(kilogram(1).inGram()).isEqualTo(1000.0);
  }

  @Test
  public void toHuman() {
    assertThat(milligram(900).toHuman()).isEqualTo("900.0 mg");
    assertThat(kilogram(10.5).toHuman()).isEqualTo("10.5 kg");
    assertThat(kilogram(10.56).toHuman()).isEqualTo("10.6 kg");

    assertThat(gram(10050).toHuman()).isEqualTo("10.1 kg");
    assertThat(gram(Integer.MAX_VALUE).toHuman()).isEqualTo("2147.5 ton");
  }

  @Test
  public void parsing() {

    assertThat(parse("142 mg").inGram()).isEqualTo(milligram(142).inGram(), offset);
    assertThat(parse("0.1 g").inGram()).isEqualTo(gram(0.1).inGram(), offset);
    assertThat(parse("10000.1 g").inGram()).isEqualTo(gram(10000.1).inGram(), offset);
    assertThat(parse("78.4 kg").inGram()).isEqualTo(kilogram(78.4).inGram(), offset);
  }

  @Test(expected = NumberFormatException.class)
  public void parse100bottles() {
    Length.parse("100.bottles");
  }

  @Test(expected = NumberFormatException.class)
  public void parse100gram() {
    Length.parse("100 gram");
  }

  @Test(expected = NumberFormatException.class)
  public void parseMalformat() {
    Length.parse("100.0.0.0.gram");
  }

  @Test
  public void negative() {
    assertThat(gram(-132).inGram()).isEqualTo(-132, offset);
    assertThat(kilogram(-2).toHuman()).isEqualTo("-2.0 kg");
  }

  @Test
  public void sameHashCode() {
    Mass i = kilogram(4);
    Mass j = kilogram(4);
    Mass k = kilogram(4.0);

    assertThat(i.hashCode()).isEqualTo(j.hashCode());
    assertThat(j.hashCode()).isEqualTo(k.hashCode());
  }

  @Test
  public void compare() {
    assertThat(kilogram(4.1)).isGreaterThan(kilogram(3.9));
    assertThat(gram(-1.2)).isLessThan(gram(-0.2));
    assertThat(gram(-1.2)).isGreaterThan(gram(-2.5));
  }

  @Test
  public void arithmetics() {
    assertThat(kilogram(1).plus(kilogram(2))).isEqualTo(gram(3000));
    assertThat(kilogram(1).minus(kilogram(2))).isEqualTo(kilogram(-1));
    assertThat(kilogram(4).times(2)).isEqualTo(kilogram(8));
    assertThat(kilogram(4).div(2)).isEqualTo(kilogram(2));
  }

}
