package debop4k.units.java;

import com.github.debop.siunits.Mass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.debop.siunits.masses.*;
import static org.assertj.core.api.Assertions.assertThat;


public class MassTest extends AbstractUnitTest {

  private Logger log = LoggerFactory.getLogger(MassTest.class);

  @Test
  public void convertMassUnit() {
    Mass millis = toMilligram(1.0);
    Double mg = millis.inMilligram();
    assertThat(mg).isEqualTo(1.0);

    assertThat(toMilligram(1.0).inMilligram()).isEqualTo(1);
    assertThat(toGram(1.0).inGram()).isEqualTo(1);
    assertThat(toKilogram(1).inKilogram()).isEqualTo(1);
    assertThat(toTon(1).inTon()).isEqualTo(1);

    assertThat(toMilligram(1000.0).inGram()).isEqualTo(1.0);
    assertThat(toMilligram(1.0).inGram()).isEqualTo(1 / 1000.0);
    assertThat(toGram(1).inMilligram()).isEqualTo(1000.0);
    assertThat(toKilogram(1).inGram()).isEqualTo(1000.0);
  }

  @Test
  public void testToHuman() {
    assertThat(toMilligram(900).toHuman()).isEqualTo("900.0 mg");
    assertThat(toKilogram(10.5).toHuman()).isEqualTo("10.5 kg");
    assertThat(toKilogram(10.56).toHuman()).isEqualTo("10.6 kg");

    assertThat(toGram(10050).toHuman()).isEqualTo("10.1 kg");
//    assertThat(gram(Integer.MAX_VALUE).toHuman()).isEqualTo("2147.5 ton");
  }

  @Test
  public void parsing() {

    assertThat(Mass.parse("142.0 mg").inGram()).isEqualTo(toMilligram(142).inGram(), offset);
    assertThat(Mass.parse("0.1 g").inGram()).isEqualTo(toGram(0.1).inGram(), offset);
    assertThat(Mass.parse("10000.1 g").inGram()).isEqualTo(toGram(10000.1).inGram(), offset);
    assertThat(Mass.parse("78.4 kg").inGram()).isEqualTo(toKilogram(78.4).inGram(), offset);
  }

//  @Test(expected = NumberFormatException.class)
//  public void parse100bottles() {
//    Length.valueOf("100.bottles");
//  }
//
//  @Test(expected = NumberFormatException.class)
//  public void parse100gram() {
//    Length.valueOf("100 gram");
//  }
//
//  @Test(expected = NumberFormatException.class)
//  public void parseMalformat() {
//    Length.valueOf("100.0.0.0.gram");
//  }

  @Test
  public void negative() {
    assertThat(toGram(-132).inGram()).isEqualTo(-132, offset);
    assertThat(toKilogram(-2).toHuman()).isEqualTo("-2.0 kg");
  }

  @Test
  public void sameHashCode() {
    Mass i = toKilogram(4);
    Mass j = toKilogram(4);
    Mass k = toKilogram(4.0);

    assertThat(i.hashCode()).isEqualTo(j.hashCode());
    assertThat(j.hashCode()).isEqualTo(k.hashCode());
  }

  @Test
  public void compare() {
    assertThat(toKilogram(4.1)).isGreaterThan(toKilogram(3.9));
    assertThat(toGram(-1.2)).isLessThan(toGram(-0.2));
    assertThat(toGram(-1.2)).isGreaterThan(toGram(-2.5));
  }

  @Test
  public void arithmetics() {
    assertThat(toKilogram(1).plus(toKilogram(2))).isEqualTo(toGram(3000));
    assertThat(toKilogram(1).minus(toKilogram(2))).isEqualTo(toKilogram(-1));
    assertThat(toKilogram(4).times(2)).isEqualTo(toKilogram(8));
    assertThat(toKilogram(4).div(2)).isEqualTo(toKilogram(2));
  }

}
