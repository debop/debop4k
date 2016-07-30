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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class MassKotlinTest : AbstractUnitKotlinTest() {

  @Test fun convertMassUnit() {
    val millis = 1.0.toMilligram()
    val mg = millis.inMilligram()

    assertThat(mg).isEqualTo(1.0)

    assertThat(1.0.toMilligram().inMilligram()).isEqualTo(1.0)
    assertThat(1.0.toGram().inGram()).isEqualTo(1.0)
    assertThat(1.toKilogram().inKilogram()).isEqualTo(1.0)
    assertThat(1.toTon().inTon()).isEqualTo(1.0)

    assertThat(1000.0.toMilligram().inGram()).isEqualTo(1.0)
    assertThat(1.0.toMilligram().inGram()).isEqualTo(1.0e-3)
    assertThat(1.toGram().inMilligram()).isEqualTo(1000.0)
    assertThat(1.toKilogram().inGram()).isEqualTo(1000.0)
  }

  @Test fun gramToGram() {

    assertThat(5.0.toGram().gram).isEqualTo(5.0, TOLERANCE)
    assertThat(Double.POSITIVE_INFINITY.toGram().gram).isEqualTo(Double.POSITIVE_INFINITY)
    assertThat(Mass.POSITIVE_INF.gram).isEqualTo(Double.POSITIVE_INFINITY)

  }

  @Test fun toHuman() {
    assertThat(900.toMilligram().toHuman()).isEqualTo("900.0 mg")
    assertThat(10.5.toKilogram().toHuman()).isEqualTo("10.5 kg")
    assertThat(10.56.toKilogram().toHuman()).isEqualTo("10.6 kg")

    assertThat(10050.toGram().toHuman()).isEqualTo("10.1 kg")
    //    assertThat(gram(Integer.MAX_VALUE).toHuman()).isEqualTo("2147.5 ton");

    assertThat(900.toMilligram().toHuman()).isEqualTo("900.0 mg")
    assertThat(10.5.toKilogram().toHuman()).isEqualTo("10.5 kg")
    assertThat(10.56.toKilogram().toHuman()).isEqualTo("10.6 kg")
    assertThat(10050.toGram().toHuman()).isEqualTo("10.1 kg")
    assertThat(2.11.toTon().toHuman()).isEqualTo("2.1 ton")
  }

  @Test fun parsing() {
    assertThat(Mass.parse("142.0 mg").inGram()).isEqualTo(142.toMilligram().inGram(), TOLERANCE)
    assertThat(Mass.parse("0.1 g").inGram()).isEqualTo(0.1.toGram().inGram(), TOLERANCE)
    assertThat(Mass.parse("10000.1 g").inGram()).isEqualTo(10000.1.toGram().inGram(), TOLERANCE)
    assertThat(Mass.parse("78.4 kg").inGram()).isEqualTo(78.4.toKilogram().inGram(), TOLERANCE)

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
    assertThat(a.toGram().inGram()).isEqualTo(-132.0, TOLERANCE)

    val b = -2
    assertThat(b.toKilogram().toHuman()).isEqualTo("-2.0 kg")
  }

  @Test fun same_hashCode() {
    val i = 4.toKilogram()
    val j = 4.toKilogram()
    val k = 4.0.toKilogram()

    assertThat(i.hashCode()).isEqualTo(j.hashCode())
    assertThat(j.hashCode()).isEqualTo(k.hashCode())
  }

  @Test fun compare() {
    assertThat(4.1.toKilogram() > 3.9.toKilogram()).isTrue()
    assertThat((-1.2).toGram() < (-0.2).toGram()).isTrue()
    assertThat((-1.2).toGram() > (-2.5).toGram()).isTrue()
  }

  @Test fun arithmetics() {
    assertThat(1.toKilogram() + 2.toKilogram()).isEqualTo(3000.toGram())
    assertThat(1.toKilogram() - 2.toKilogram()).isEqualTo((-1).toKilogram())
    assertThat(4.toKilogram() * 2.0).isEqualTo(8.toKilogram())
    assertThat(4.toKilogram() / 2.0).isEqualTo(2.toKilogram())

    assertThat(2.1 * 4.toKilogram()).isEqualTo(8.4.toKilogram())
    assertThat(5 * 4.toKilogram()).isEqualTo(20.toKilogram())
  }
}