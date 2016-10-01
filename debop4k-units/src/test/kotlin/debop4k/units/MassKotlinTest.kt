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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class MassKotlinTest : AbstractUnitKotlinTest() {

  @Test fun convertMassUnit() {
    val millis = 1.0.milligram()
    val mg = millis.inMilligram()

    assertThat(mg).isEqualTo(1.0)

    assertThat(1.0.milligram().inMilligram()).isEqualTo(1.0)
    assertThat(1.0.gram().inGram()).isEqualTo(1.0)
    assertThat(1.kilogram().inKilogram()).isEqualTo(1.0)
    assertThat(1.ton().inTon()).isEqualTo(1.0)

    assertThat(1000.0.milligram().inGram()).isEqualTo(1.0)
    assertThat(1.0.milligram().inGram()).isEqualTo(1.0e-3)
    assertThat(1.gram().inMilligram()).isEqualTo(1000.0)
    assertThat(1.kilogram().inGram()).isEqualTo(1000.0)
  }

  @Test fun gramToGram() {

    assertThat(5.0.gram().gram).isEqualTo(5.0, TOLERANCE)
    assertThat(Double.POSITIVE_INFINITY.gram().gram).isEqualTo(Double.POSITIVE_INFINITY)
    assertThat(Mass.POSITIVE_INF.gram).isEqualTo(Double.POSITIVE_INFINITY)

  }

  @Test fun toHuman() {
    assertThat(900.milligram().toHuman()).isEqualTo("900.0 mg")
    assertThat(10.5.kilogram().toHuman()).isEqualTo("10.5 kg")
    assertThat(10.56.kilogram().toHuman()).isEqualTo("10.6 kg")

    assertThat(10050.gram().toHuman()).isEqualTo("10.1 kg")
    //    assertThat(gram(Integer.MAX_VALUE).toHuman()).isEqualTo("2147.5 ton");

    assertThat(900.milligram().toHuman()).isEqualTo("900.0 mg")
    assertThat(10.5.kilogram().toHuman()).isEqualTo("10.5 kg")
    assertThat(10.56.kilogram().toHuman()).isEqualTo("10.6 kg")
    assertThat(10050.gram().toHuman()).isEqualTo("10.1 kg")
    assertThat(2.11.ton().toHuman()).isEqualTo("2.1 ton")
  }

  @Test fun parsing() {
    assertThat(Mass.parse("142.0 mg").inGram()).isEqualTo(142.milligram().inGram(), TOLERANCE)
    assertThat(Mass.parse("0.1 g").inGram()).isEqualTo(0.1.gram().inGram(), TOLERANCE)
    assertThat(Mass.parse("10000.1 g").inGram()).isEqualTo(10000.1.gram().inGram(), TOLERANCE)
    assertThat(Mass.parse("78.4 kg").inGram()).isEqualTo(78.4.kilogram().inGram(), TOLERANCE)

    assertThatThrownBy {
      Mass.parse("100.bottles")
    }.isInstanceOf(NumberFormatException::class.java)

    assertThatThrownBy {
      Mass.parse("100 m")
    }.isInstanceOf(NumberFormatException::class.java)

    assertThatThrownBy {
      Mass.parse("100.0.0.0.gram")
    }.isInstanceOf(NumberFormatException::class.java)
  }

  @Test fun negative() {
    val a = -132
    assertThat(a.gram().inGram()).isEqualTo(-132.0, TOLERANCE)

    val b = -2
    assertThat(b.kilogram().toHuman()).isEqualTo("-2.0 kg")
  }

  @Test fun same_hashCode() {
    val i = 4.kilogram()
    val j = 4.kilogram()
    val k = 4.0.kilogram()

    assertThat(i.hashCode()).isEqualTo(j.hashCode())
    assertThat(j.hashCode()).isEqualTo(k.hashCode())
  }

  @Test fun compare() {
    assertThat(4.1.kilogram() > 3.9.kilogram()).isTrue()
    assertThat((-1.2).gram() < (-0.2).gram()).isTrue()
    assertThat((-1.2).gram() > (-2.5).gram()).isTrue()
  }

  @Test fun arithmetics() {
    assertThat(1.kilogram() + 2.kilogram()).isEqualTo(3000.gram())
    assertThat(1.kilogram() - 2.kilogram()).isEqualTo((-1).kilogram())
    assertThat(4.kilogram() * 2.0).isEqualTo(8.kilogram())
    assertThat(4.kilogram() / 2.0).isEqualTo(2.kilogram())

    assertThat(2.1 * 4.kilogram()).isEqualTo(8.4.kilogram())
    assertThat(5 * 4.kilogram()).isEqualTo(20.kilogram())
  }
}