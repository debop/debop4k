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

@file:JvmName("Masses")

package debop4k.units

import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = lazy { LoggerFactory.getLogger("masses") }

const val MILLIGRAM_IN_GRAM: Double = 1.0 / 1000.0
const val GRAM_IN_GRAM: Double = 1.0
const val KILOGRAM_IN_GRAM: Double = 1000.0
const val TON_IN_GRAM: Double = 1000.0 * 1000.0

fun Int.toMilligram(): Mass = Mass.of(this.toDouble(), MassUnit.MILLIGRAM)
fun Int.toGram(): Mass = Mass.of(this.toDouble(), MassUnit.GRAM)
fun Int.toKilogram(): Mass = Mass.of(this.toDouble(), MassUnit.KILOGRAM)
fun Int.toTon(): Mass = Mass.of(this.toDouble(), MassUnit.TON)

operator fun Int.times(m: Mass): Mass = m.times(this.toDouble())

fun Double.toMilligram(): Mass = Mass.of(this, MassUnit.MILLIGRAM)
fun Double.toGram(): Mass = Mass.of(this, MassUnit.GRAM)
fun Double.toKilogram(): Mass = Mass.of(this, MassUnit.KILOGRAM)
fun Double.toTon(): Mass = Mass.of(this, MassUnit.TON)

operator fun Double.times(m: Mass): Mass = m.times(this)

/**
 * 질량/무게 (Mass/Weight) 단위를 표현합니다.
 */
enum class MassUnit(val unitName: String, val factor: Double) {

  MILLIGRAM("mg", MILLIGRAM_IN_GRAM),
  GRAM("g", GRAM_IN_GRAM),
  KILOGRAM("kg", KILOGRAM_IN_GRAM),
  TON("ton", TON_IN_GRAM);

  companion object {

    @JvmStatic
    fun parse(unitStr: String): MassUnit {
      var lower = unitStr.toLowerCase()
      if (lower.endsWith("s")) {
        lower = lower.dropLast(1)
      }
      return MassUnit.values().find { it.unitName == lower }
             ?: throw NumberFormatException("Unknown Mess unit. unit=$unitStr")
    }
  }
}

/**
 * 질량/무게 (Mass/Weight) 를 표현합니다.
 *
 * @author debop sunghyouk.bae@gmail.com
 */
data class Mass(val gram: Double = 0.0) : Comparable<Mass>, Serializable {

  fun inMilligram(): Double = gram / MILLIGRAM_IN_GRAM
  fun inGram(): Double = gram
  fun inKilogram(): Double = gram / KILOGRAM_IN_GRAM
  fun inTon(): Double = gram / TON_IN_GRAM

  fun inUnit(unit: MassUnit = MassUnit.GRAM): Double = when (unit) {
    MassUnit.MILLIGRAM -> inMilligram()
    MassUnit.GRAM -> inGram()
    MassUnit.KILOGRAM -> inKilogram()
    MassUnit.TON -> inTon()
    else -> throw UnsupportedOperationException("Unknown Mass unit. unit=$unit")
  }

  operator fun plus(other: Mass): Mass = Mass(gram + other.gram)
  operator fun minus(other: Mass): Mass = Mass(gram - other.gram)

  operator fun times(scalar: Double): Mass = Mass(gram * scalar)
  operator fun times(other: Mass): Mass = Mass(gram * other.gram)

  operator fun div(scalar: Double): Mass = Mass(gram / scalar)
  operator fun div(other: Mass): Mass = Mass(gram / other.gram)

  operator fun unaryMinus(): Mass = Mass(-gram)

  fun toHuman(): String {
    var unit = MassUnit.GRAM
    var display = Math.abs(gram)

    if (display > TON_IN_GRAM) {
      display /= TON_IN_GRAM
      unit = MassUnit.TON
      return "%.1f %s".format(display * Math.signum(gram), unit.unitName)
    }

    if (display < GRAM_IN_GRAM) {
      unit = MassUnit.MILLIGRAM
      display /= MILLIGRAM_IN_GRAM
    } else if (display > KILOGRAM_IN_GRAM) {
      unit = MassUnit.KILOGRAM
      display /= KILOGRAM_IN_GRAM
    }
    return "%.1f %s".format(display * Math.signum(gram), unit.unitName)
  }

  override fun compareTo(other: Mass): Int = this.gram.compareTo(other.gram)
  override fun toString(): String = "%.1f %s".format(gram, MassUnit.GRAM.unitName)

  companion object {

    @JvmField val ZERO: Mass = Mass(0.0)
    @JvmField val MAX_VALUE = Mass(Double.MAX_VALUE)
    @JvmField val MIN_VALUE = Mass(Double.MIN_VALUE)
    @JvmField val POSITIVE_INF: Mass = Mass(Double.POSITIVE_INFINITY)
    @JvmField val NEGATIVE_INF: Mass = Mass(Double.NEGATIVE_INFINITY)
    @JvmField val NaN: Mass = Mass(Double.NaN)

    /**
     * Static constructor
     */
    @JvmStatic
    fun of(value: Double = 0.0, unit: MassUnit = MassUnit.GRAM): Mass =
        Mass(value * unit.factor)

    @JvmStatic
    fun parse(str: String): Mass {
      if (str.isNullOrBlank())
        return Mass.ZERO

      try {
        val (value, unit) = str.trim().split(" ", limit = 2)
        return of(value.toDouble(), MassUnit.parse(unit))

      } catch(e: Exception) {
        throw NumberFormatException("Invalid Mass string. str=$str")
      }
    }
  }
}
