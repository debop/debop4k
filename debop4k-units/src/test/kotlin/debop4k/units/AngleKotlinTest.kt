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

import debop4k.units.AngleUnit.Radian
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * AngleKotlinTest
 * @author sunghyouk.bae@gmail.com
 */
class AngleKotlinTest : AbstractUnitKotlinTest() {

  @Test fun convertAngleUnit() {
    val one = 1.0.radian().inRadian()

    assertThat(one).isEqualTo(1.0)
    assertThat(1.0.radian().inRadian()).isEqualTo(1.0)
    assertThat(1.0.degree().inDegree()).isEqualTo(1.0)

    assertThat(90.0.degree().inRadian()).isEqualTo(degreeToRadian(90.0))
  }

  @Test fun degreeToDegre() {
    assertThat(5.0.degree().degree).isEqualTo(5.0, TOLERANCE)
    assertThat(Double.POSITIVE_INFINITY.degree().degree).isEqualTo(Double.POSITIVE_INFINITY)
    assertThat(Angle.POSITIVE_INF.degree).isEqualTo(Double.POSITIVE_INFINITY)
  }

  @Test fun toHuman() {
    assertThat(90.0.degree().toHuman()).isEqualTo("90.0 deg")
    assertThat(10.5.degree().toHuman()).isEqualTo("10.5 deg")
    assertThat(10.56.degree().toHuman()).isEqualTo("10.6 deg")

    assertThat(degreeToRadian(180.0).radian().toHuman(AngleUnit.Radian)).isEqualTo("3.1416 rad")
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
    val a = -132.0
    assertThat(a.degree().inDegree()).isEqualTo(-132.0, TOLERANCE)

    val b = -2.0
    assertThat(b.radian().toHuman(Radian)).isEqualTo("-2.0000 rad")
  }

  @Test fun same_hashCode() {
    val i = 4.0.degree()
    val j = 4.0.degree()
    val k = 4.0.degree()

    assertThat(i.hashCode()).isEqualTo(j.hashCode())
    assertThat(j.hashCode()).isEqualTo(k.hashCode())
  }

  @Test fun compare() {
    assertThat(4.1.degree() > 3.9.degree()).isTrue()
    assertThat((-1.2).degree() < (-0.2).degree()).isTrue()
    assertThat((-1.2).degree() > (-2.5).degree()).isTrue()
  }

  @Test fun arithmetics() {
    assertThat(1.degree() + 2.degree()).isEqualTo(3.degree())
    assertThat(1.degree() - 2.degree()).isEqualTo((-1).degree())
    assertThat(4.degree() * 2.0).isEqualTo(8.degree())
    assertThat(4.degree() / 2.0).isEqualTo(2.degree())

    assertThat(2.1 * 4.degree()).isEqualTo(8.4.degree())
    assertThat(5 * 4.degree()).isEqualTo(20.degree())
  }
}