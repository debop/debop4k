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

import debop4k.units.java.AbstractUnitTest
import debop4k.units.java.MassTest
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory

class MassFunSpec : AbstractUnitFunSpec() {

  private val log = LoggerFactory.getLogger(MassTest::class.java)

  init {
    test("convertMassUnit") {
      val millis = 1.0.toMilligram()
      val mg = millis.inMilligram()

      mg shouldEqual 1.0

      1.0.toMilligram().inMilligram() shouldEqual 1.0
      1.0.toGram().inGram() shouldEqual 1.0
      1.toKilogram().inKilogram() shouldEqual 1.0
      1.toTon().inTon() shouldEqual 1.0

      1000.0.toMilligram().inGram() shouldEqual 1.0
      1.0.toMilligram().inGram() shouldEqual 1.0e-3
      1.toGram().inMilligram() shouldEqual 1000.0
      1.toKilogram().inGram() shouldEqual 1000.0
    }

    test("toHuman") {
      assertThat(900.toMilligram().toHuman()).isEqualTo("900.0 mg")
      assertThat(10.5.toKilogram().toHuman()).isEqualTo("10.5 kg")
      assertThat(10.56.toKilogram().toHuman()).isEqualTo("10.6 kg")

      assertThat(10050.toGram().toHuman()).isEqualTo("10.1 kg")
      //    assertThat(gram(Integer.MAX_VALUE).toHuman()).isEqualTo("2147.5 ton");

      900.toMilligram().toHuman() shouldBe "900.0 mg"
      10.5.toKilogram().toHuman() shouldBe "10.5 kg"
      10.56.toKilogram().toHuman() shouldBe "10.6 kg"
      10050.toGram().toHuman() shouldBe "10.1 kg"
      2.11.toTon().toHuman() shouldEqual "2.1 ton"
    }

    test("parsing") {
      Mass.parse("142.0 mg").inGram() shouldEqual (142.toMilligram().inGram() plusOrMinus TOLERANCE)
      Mass.parse("0.1 g").inGram() shouldEqual (0.1.toGram().inGram() plusOrMinus TOLERANCE)
      Mass.parse("10000.1 g").inGram() shouldEqual (10000.1.toGram().inGram() plusOrMinus TOLERANCE)
      Mass.parse("78.4 kg").inGram() shouldEqual (78.4.toKilogram().inGram() plusOrMinus TOLERANCE)

      shouldThrow<NumberFormatException> {
        Mass.parse("100.bottles")
      }

      shouldThrow<NumberFormatException> {
        Mass.parse("100 m")
      }
      shouldThrow<NumberFormatException> {
        Mass.parse("100.0.0.0.gram")
      }
    }

    test("negative") {
      val a = -132
      a.toGram().inGram() shouldBe (-132.0 plusOrMinus AbstractUnitTest.TOLERANCE)

      val b = -2
      b.toKilogram().toHuman() shouldBe "-2.0 kg"
    }

    test("same hashCode") {
      val i = 4.toKilogram()
      val j = 4.toKilogram()
      val k = 4.0.toKilogram()

      i.hashCode() shouldEqual j.hashCode()
      j.hashCode() shouldEqual k.hashCode()
    }

    test("compare") {
      (4.1.toKilogram() > 3.9.toKilogram()) shouldBe true
      ((-1.2).toGram() < (-0.2).toGram()) shouldBe true
      ((-1.2).toGram() > (-2.5).toGram()) shouldBe true
    }

    test("arithmetics") {
      1.toKilogram() + 2.toKilogram() shouldEqual 3000.toGram()
      1.toKilogram() - 2.toKilogram() shouldEqual (-1).toKilogram()
      4.toKilogram() * 2.0 shouldEqual 8.toKilogram()
      4.toKilogram() / 2.0 shouldEqual 2.toKilogram()

      2.1 * 4.toKilogram() shouldEqual 8.4.toKilogram()
      5 * 4.toKilogram() shouldEqual 20.toKilogram()
    }
  }
}