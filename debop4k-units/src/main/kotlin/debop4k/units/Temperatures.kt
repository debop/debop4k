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

@file:JvmName("Temperatures")

package debop4k.units

import java.io.Serializable


fun Double.kelvin(): Temperature = Temperature.of(this)
fun Double.celcius(): Temperature = Temperature.of(this, TemperatureUnit.CELCIUS)
fun Double.fahrenheit(): Temperature = Temperature.of(this, TemperatureUnit.FAHRENHEIT)

fun Double.C2F(): Double = this * 1.8 + 32.0
fun Double.F2C(): Double = (this - 32.0) / 1.8

/**
 * 온도 단위의 종류
 */
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

/**
 * 온도를 나타내는 클래스입니다.
 */
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
    @JvmField val ZERO = Temperature(0.0)
    @JvmField val MIN_VALUE = Temperature(Double.MIN_VALUE)
    @JvmField val MAX_VALUE = Temperature(Double.MAX_VALUE)
    @JvmField val POSITIVE_INF = Temperature(Double.POSITIVE_INFINITY)
    @JvmField val NEGATIVE_INF = Temperature(Double.NEGATIVE_INFINITY)
    @JvmField val NaN = Temperature(Double.NaN)

    @JvmOverloads
    @JvmStatic
    fun of(temp: Double, unit: TemperatureUnit = TemperatureUnit.KELVIN): Temperature =
        Temperature(temp + unit.factor)

    @JvmStatic
    fun parse(tempStr: String?): Temperature {
      if (tempStr.isNullOrBlank())
        return ZERO

      try {
        val (temp, unit) = tempStr!!.split(" ", limit = 2)
        return of(temp.toDouble(), TemperatureUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Unknown Temperature string. tempStr=$tempStr")
      }
    }
  }

}