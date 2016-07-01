package debop4k.units

import io.kotlintest.specs.FunSpec
import org.assertj.core.api.Assertions

/**
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractUnitFunSpec : FunSpec() {

  val offset = Assertions.offset(1.0e-8)

}
