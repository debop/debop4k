package debop4k.units.java;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;


public abstract class AbstractUnitTest {

  public static Offset<Double> offset = Assertions.offset(1.0e-8);

}
