@file:JvmName("temperatures")

package debop4k.units

import java.io.Serializable


fun Double.toKelvin(): Temperature = Temperature.of(this)
fun Double.toCelcius(): Temperature = Temperature.of(this, TemperatureUnit.CELCIUS)
fun Double.toFahrenheit(): Temperature = Temperature.of(this, TemperatureUnit.FAHRENHEIT)

fun Double.C2F(): Double = this * 1.8 + 32.0
fun Double.F2C(): Double = (this - 32.0) / 1.8

enum class TemperatureUnit(val unitName: String, val factor: Double) {

  KELVIN("K", 0.0),
  CELCIUS("C", 273.15),
  FAHRENHEIT("F", 459.67);

  companion object {
    @JvmStatic
    fun parse(unitStr: String): TemperatureUnit {
      var upper = unitStr.toUpperCase()
      if (upper.endsWith("s"))
        upper = upper.dropLast(1)

      return TemperatureUnit.values().find { it.unitName == upper }
          ?: throw NumberFormatException("Unknown Temperature unit. unitStr=$unitStr")
    }
  }
}

data class Temperature(val kelvin: Double = 0.0) : Comparable<Temperature>, Serializable {

  operator fun plus(other: Temperature) = Temperature(kelvin + other.kelvin)
  operator fun minus(other: Temperature) = Temperature(kelvin - other.kelvin)
  operator fun times(scalar: Double) = Temperature(kelvin * scalar)
  operator fun div(scalar: Double) = Temperature(kelvin / scalar)
  operator fun unaryMinus() = Temperature(-kelvin)

  fun inKelvin(): Double = kelvin
  fun inCelcius(): Double = kelvin - TemperatureUnit.CELCIUS.factor
  fun inFahrenheit(): Double = kelvin - TemperatureUnit.FAHRENHEIT.factor

  override fun compareTo(other: Temperature): Int = kelvin.compareTo(other.kelvin)
  override fun toString(): String = "%.1f %s".format(kelvin, TemperatureUnit.KELVIN.unitName)

  companion object {
    final val ZERO = Temperature(0.0)
    final val MIN_VALUE = Temperature(Double.MIN_VALUE)
    final val MAX_VALUE = Temperature(Double.MAX_VALUE)
    final val POSITIVE_INF = Temperature(Double.POSITIVE_INFINITY)
    final val NEGATIVE_INF = Temperature(Double.NEGATIVE_INFINITY)
    final val NaN = Temperature(Double.NaN)

    @JvmStatic
    fun of(temp: Double, unit: TemperatureUnit = TemperatureUnit.KELVIN): Temperature =
        Temperature(temp + unit.factor)

    @JvmStatic
    fun parse(tempStr: String): Temperature {
      if (tempStr.isNullOrBlank())
        return ZERO

      try {
        val (temp, unit) = tempStr.split(" ", limit = 2)
        return of(temp.toDouble(), TemperatureUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Unknown Temperature string. tempStr=$tempStr")
      }
    }
  }

}