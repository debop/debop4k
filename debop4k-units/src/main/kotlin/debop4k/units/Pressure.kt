@file:JvmName("pressures")

package debop4k.units

import java.io.Serializable

/**
 * 압력 단위 종류
 */
enum class PressureUnit(val unitName: String, val factor: Double) {
  /**
   * 기압 :  지구 해수면 근처에서 잰 대기압을 기준으로 한다. 1기압은 101325 Pa, 760 mmHg이다.
   */
  ATM("atm", 101325.0),
  /**
   * Pascal 단위 ( N/m2 ) : https://ko.wikipedia.org/wiki/%ED%8C%8C%EC%8A%A4%EC%B9%BC_(%EB%8B%A8%EC%9C%84)
   */
  PASCAL("pascal", 1.0),
  /**
   * HectoPascal (1 hPa = 100 pa)
   */
  HECTO_PASCAL("hp", 100.0),
  /**
   * KiloPascal (1kPa = 1000 Pa)
   */
  KILO_PASCAL("kp", 1000.0),

  /**
   * MegaPascal (1 MPa = 1000,000 Pa)
   */
  MEGA_PASCLA("mp", 1.0e6),

  /**
   * Bar : 1 bar = 100,000 Pa
   *
   *
   * 대기압은 통상적으로 밀리바를 사용하여, "표준" 해수면 압력을 1.01325 바와 같은 1013.25 밀리바(hPa, 헥토파스칼)로 정의한다.
   * 밀리바는 SI 단위계가 아닌데도 불구하고, 아직 기상학 분야에서 대기압을 기술하는 압력 단위로 사용되곤 한다.
   */
  BAR("bar", 1.0e5),

  /**
   * Deci Bar : 1 dbar = 0.1 bar = 10,000 Pa
   *
   *
   * 대기압은 통상적으로 밀리바를 사용하여, "표준" 해수면 압력을 1.01325 바와 같은 1013.25 밀리바(hPa, 헥토파스칼)로 정의한다.
   * 밀리바는 SI 단위계가 아닌데도 불구하고, 아직 기상학 분야에서 대기압을 기술하는 압력 단위로 사용되곤 한다.
   */
  DECI_BAR("decibar", 1.0e4),

  /**
   * Milli Bar : 1 mbar = 0.001 bar = 100 Pa
   *
   *
   * 대기압은 통상적으로 밀리바를 사용하여, "표준" 해수면 압력을 1.01325 바와 같은 1013.25 밀리바(hPa, 헥토파스칼)로 정의한다.
   * 밀리바는 SI 단위계가 아닌데도 불구하고, 아직 기상학 분야에서 대기압을 기술하는 압력 단위로 사용되곤 한다.
   */
  MILLI_BAR("mmbar", 100.0),

  /**
   * 제곱 인치당 파운드(영어: pound per square inch, 정확하게는 영어: pound-force per square inch,
   * 기호는 psi, lbf/in2, lbf/in2, lbf/sq in, lbf/sq in)는 애버더포와 단위로 나타낸 압력이나 응력 단위다.
   * 이 단위는 1 제곱 인치 넓이에 1 파운드힘이 누르는 압력이다. 1 제곱 인치당 파운드 는 약 6894.757 Pa이다.
   */
  PSI("psi", 6895.757),

  /**
   * 토르(torr, 기호 Torr)는 압력의 단위로 1mm의 수은주 압력에 해당한다.
   * 1 Torr = 1 mmHg. 1기압은 760 mmHg 이므로 1 Torr는 1 기압의 1/760이다. 이탈리아의 과학자 에반젤리스타 토리첼리의 이름을 따서 만들어졌다.
   */
  TORR("torr", 1.0 / 122.322),

  /**
   * 수은주 밀리미터(mmHg)는 압력의 단위로 1mmHg는 수은주의 높이가 1mm일 때의 압력이다. 1기압은 약 760mmHg에 해당한다.
   */
  MMHG("mmHg", 1.0 / 122.322);

  companion object {

    @JvmStatic fun parse(unitStr: String): PressureUnit {
      var lower = unitStr.toLowerCase()
      if (lower.endsWith("s"))
        lower = lower.dropLast(1)

      return PressureUnit.values().find { it.unitName == lower }
          ?: throw NumberFormatException("Unknown Pressure unit. unitStr=$unitStr")
    }
  }
}


fun Double.toAtm(): Pressure = Pressure.of(this, PressureUnit.ATM)
fun Double.toPascal(): Pressure = Pressure.of(this, PressureUnit.PASCAL)
fun Double.toHectoPascal(): Pressure = Pressure.of(this, PressureUnit.HECTO_PASCAL)
fun Double.toKiloPascal(): Pressure = Pressure.of(this, PressureUnit.KILO_PASCAL)
fun Double.toMegaPascal(): Pressure = Pressure.of(this, PressureUnit.MEGA_PASCLA)

fun Double.toBar(): Pressure = Pressure.of(this, PressureUnit.BAR)
fun Double.toDeciBar(): Pressure = Pressure.of(this, PressureUnit.DECI_BAR)
fun Double.toMilliBar(): Pressure = Pressure.of(this, PressureUnit.MILLI_BAR)

fun Double.toPsi(): Pressure = Pressure.of(this, PressureUnit.PSI)
fun Double.toTorr(): Pressure = Pressure.of(this, PressureUnit.TORR)
fun Double.toMMHg(): Pressure = Pressure.of(this, PressureUnit.MMHG)

/**
 * 압력 (Pressure) 를 나타내는 클래스
 */
data class Pressure(val pascal: Double = 0.0) : Comparable<Pressure>, Serializable {

  fun inAtm(): Double = pascal / PressureUnit.ATM.factor
  fun inPascal(): Double = pascal
  fun inHectoPascal(): Double = pascal / PressureUnit.HECTO_PASCAL.factor
  fun inKiloPascal(): Double = pascal / PressureUnit.KILO_PASCAL.factor
  fun inMegaPascal(): Double = pascal / PressureUnit.MEGA_PASCLA.factor

  fun inBar(): Double = pascal / PressureUnit.BAR.factor
  fun inDeciBar(): Double = pascal / PressureUnit.DECI_BAR.factor
  fun inMilliBar(): Double = pascal / PressureUnit.MILLI_BAR.factor

  fun inPsi(): Double = pascal / PressureUnit.PSI.factor
  fun inTorr(): Double = pascal / PressureUnit.TORR.factor
  fun inMmHg(): Double = pascal / PressureUnit.MMHG.factor

  operator fun plus(other: Pressure): Pressure = Pressure(pascal + other.pascal)
  operator fun minus(other: Pressure): Pressure = Pressure(pascal - other.pascal)
  operator fun times(scalar: Double): Pressure = Pressure(pascal * scalar)
  operator fun div(scalar: Double): Pressure = Pressure(pascal / scalar)

  operator fun unaryMinus(): Pressure = Pressure(-pascal)

  fun toHuman(): String = TODO()

  override fun compareTo(other: Pressure): Int = pascal.compareTo(other.pascal)
  override fun toString(): String = "%.1f %s".format(pascal, PressureUnit.PASCAL.unitName)

  companion object {

    final val ZERO = Pressure(0.0)
    final val MIN_VALUE = Pressure(Double.MIN_VALUE)
    final val MAX_VALUE = Pressure(Double.MAX_VALUE)
    final val POSITIVE_INF = Pressure(Double.POSITIVE_INFINITY)
    final val NEGATIVE_INF = Pressure(Double.NEGATIVE_INFINITY)
    final val NaN = Pressure(Double.NaN)

    @JvmStatic
    fun of(value: Double = 0.0, unit: PressureUnit = PressureUnit.PASCAL): Pressure =
        Pressure(value * unit.factor)

    @JvmStatic
    fun parse(str: String): Pressure {
      if (str.isNullOrBlank())
        return ZERO

      try {
        val (pressure, unit) = str.split(" ", limit = 2)
        return of(pressure.toDouble(), PressureUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Unknown pressure format. str=$str")
      }
    }
  }

}