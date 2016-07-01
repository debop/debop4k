package debop4k.units

import debop4k.units.java.AbstractUnitTest
import debop4k.units.java.MassTest
import io.kotlintest.specs.FunSpec
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory

class MassFunSpec : FunSpec() {

  private val log = LoggerFactory.getLogger(MassTest::class.java)

  init {
    test("convertMassUnit") {
      val millis = 1.0.toMilligram()
      val mg = millis.inMilligram()
      assertThat(mg).isEqualTo(1.0)

      assertThat(1.0.toMilligram().inMilligram()).isEqualTo(1.0)
      assertThat(1.0.toGram().inGram()).isEqualTo(1.0)
      assertThat(1.toKilogram().inKilogram()).isEqualTo(1.0)
      assertThat(1.toTon().inTon()).isEqualTo(1.0)

      assertThat(1000.0.toMilligram().inGram()).isEqualTo(1.0)
      assertThat(1.0.toMilligram().inGram()).isEqualTo(1 / 1000.0)
      assertThat(1.toGram().inMilligram()).isEqualTo(1000.0)
      assertThat(1.toKilogram().inGram()).isEqualTo(1000.0)
    }

    test("toHuman") {
      assertThat(900.toMilligram().toHuman()).isEqualTo("900.0 mg")
      assertThat(10.5.toKilogram().toHuman()).isEqualTo("10.5 kg")
      assertThat(10.56.toKilogram().toHuman()).isEqualTo("10.6 kg")

      assertThat(10050.toGram().toHuman()).isEqualTo("10.1 kg")
      //    assertThat(gram(Integer.MAX_VALUE).toHuman()).isEqualTo("2147.5 ton");
    }

    test("parsing") {
      assertThat(Mass.parse("142.0 mg").inGram()).isEqualTo(142.toMilligram().inGram(), AbstractUnitTest.offset)
      assertThat(Mass.parse("0.1 g").inGram()).isEqualTo(0.1.toGram().inGram(), AbstractUnitTest.offset)
      assertThat(Mass.parse("10000.1 g").inGram()).isEqualTo(10000.1.toGram().inGram(), AbstractUnitTest.offset)
      assertThat(Mass.parse("78.4 kg").inGram()).isEqualTo(78.4.toKilogram().inGram(), AbstractUnitTest.offset)

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
      assertThat((-132).toGram().inGram()).isEqualTo(-132.0, AbstractUnitTest.offset)
      assertThat((-2).toKilogram().toHuman()).isEqualTo("-2.0 kg")
    }

    test("same hashCode") {
      val i = 4.toKilogram()
      val j = 4.toKilogram()
      val k = 4.0.toKilogram()

      assertThat(i.hashCode()).isEqualTo(j.hashCode())
      assertThat(j.hashCode()).isEqualTo(k.hashCode())
    }
    test("compare") {
      assertThat(4.1.toKilogram()).isGreaterThan(3.9.toKilogram())
      assertThat((-1.2).toGram()).isLessThan((-0.2).toGram())
      assertThat((-1.2).toGram()).isGreaterThan((-2.5).toGram())
    }

    test("arithmetics") {
      assertThat(1.toKilogram().plus(2.toKilogram())).isEqualTo(3000.toGram())
      assertThat(1.toKilogram().minus(2.toKilogram())).isEqualTo((-1).toKilogram())
      assertThat(4.toKilogram().times(2.0)).isEqualTo(8.toKilogram())
      assertThat(4.toKilogram().div(2.0)).isEqualTo(2.toKilogram())
    }
  }
}