package debop4k.units.java;

import debop4k.units.Angle;
import debop4k.units.AngleUnit;
import org.assertj.core.data.Offset;
import org.junit.Test;

import static debop4k.units.angles.toDegree;
import static debop4k.units.angles.toRadian;
import static org.assertj.core.api.Assertions.assertThat;

public class AngleTest extends AbstractUnitTest {

  @Test
  public void testConvert() {
    assertThat(toDegree(0.0).inDegree()).isEqualTo(0.0);
    assertThat(toDegree(90.0).inDegree()).isEqualTo(90.0);
    assertThat(toDegree(0.0).inRadian()).isEqualTo(0.0);
    assertThat(toDegree(90.0).inRadian()).isEqualTo(Math.PI / 2.0);

    assertThat(toRadian(Math.PI / 2).inDegree()).isEqualTo(90.0);
    assertThat(toRadian(Math.PI).inDegree()).isEqualTo(180.0);
    assertThat(toRadian(Math.PI / 2).inRadian()).isEqualTo(Math.PI / 2);
    assertThat(toRadian(Math.PI).inRadian()).isEqualTo(Math.PI);
  }

  @Test
  public void testToHuman() {
    assertThat(toDegree(90.0).toHuman(AngleUnit.Degree)).isEqualTo("90.0 deg");
    assertThat(toRadian(Math.PI / 2).toHuman(AngleUnit.Degree)).isEqualTo("90.0 deg");

    assertThat(toDegree(90.0).toHuman(AngleUnit.Radian)).isEqualTo("1.5708 rad");
    assertThat(toRadian(Math.PI / 2).toHuman(AngleUnit.Radian)).isEqualTo("1.5708 rad");
  }

  @Test
  public void testParse() {
    Offset<Double> offset = Offset.offset(1.e-4);
    assertThat(Angle.parse("1.5708 rad").inRadian()).isEqualTo(toDegree(90.0).inRadian(), offset);
    assertThat(Angle.parse("90.0 deg").inDegree()).isEqualTo(toDegree(90.0).inDegree(), offset);
    assertThat(Angle.parse("90.0 deg").inRadian()).isEqualTo(toRadian(Math.PI / 2).inRadian(), offset);
  }

  @Test
  public void testUnaryMinus() {
    Angle angle = new Angle(132.0);
    assertThat(-angle.inDegree()).isEqualTo(-132.0, offset);
  }

  @Test
  public void testCompare() {
    assertThat(toDegree(90.0)).isGreaterThan(toDegree(45.0));

    assertThat(toDegree(-90.0)).isLessThan(toDegree(-45.0));
    assertThat(toDegree(-90.0)).isGreaterThan(toDegree(-125.0));
  }

  @Test
  public void arithmetic() {
    assertThat(toDegree(90.0).plus(toDegree(90.0))).isEqualTo(toDegree(180.0));
    assertThat(toDegree(90.0).minus(toDegree(45.0))).isEqualTo(toDegree(45.0));
    assertThat(toDegree(90.0).times(2.0)).isEqualTo(toDegree(180.0));
    assertThat(toDegree(90.0).div(2.0)).isEqualTo(toDegree(45.0));
  }
}
