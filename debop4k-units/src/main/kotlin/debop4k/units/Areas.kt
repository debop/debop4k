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

@file:JvmName("Areas")

package debop4k.units

import java.io.Serializable

fun Double.millimeter2(): Area = Area.of(this, AreaUnit.MILLI_METER_2)
fun Double.centimeter2(): Area = Area.of(this, AreaUnit.CENTI_METER_2)
fun Double.meter2(): Area = Area.of(this, AreaUnit.METER_2)
fun Double.ares(): Area = Area.of(this, AreaUnit.ARES)
fun Double.hectare(): Area = Area.of(this, AreaUnit.HECTARE)
//fun Double.inch2():Area = Area.of(this, AreaUnit.INCH_2)
//fun Double.feet2():Area = Area.of(this, AreaUnit.FEET_2)
//fun Double.yard2():Area = Area.of(this, AreaUnit.YARD_2)
fun Double.acre(): Area = Area.of(this, AreaUnit.ACRE)

enum class AreaUnit(val unitName: String, val factor: Double) {

  MILLI_METER_2("mm^2", 1.0e-6),

  CENTI_METER_2("cm^2", 1.0e-4),

  METER_2("m^2", 1.0),

  /** 아르 (Ares, a) (1 a = 100 m2) (deprecated) */
  ARES("ares", 100.0),

  /** 헥타르(Hectare, ha) (1 ha = 100 ares = 10,000 m2) */
  HECTARE("hec", 1.0e4),

  INCH_2("in^2", INCH_IN_METER * INCH_IN_METER),

  FEET_2("ft^2", FEET_IN_METER * FEET_IN_METER),

  YARD_2("yd^2", YARD_IN_METER * YARD_IN_METER),

  /** 에이커 (1 ac = 4,046.8564224 m2 = 0.40468564224 Hx ) */
  ACRE("ac", 4046.8564224);

  companion object {

    @JvmStatic
    fun parse(unitStr: String): AreaUnit {
      var lower = unitStr.toLowerCase()
      if (lower.endsWith("s"))
        lower = lower.dropLast(1)

      return AreaUnit.values().find { it.unitName == lower }
          ?: throw NumberFormatException("Unknown Area unit. unitStr=$unitStr")
    }
  }
}

data class Area(val m2: Double = 0.0) : Comparable<Area>, Serializable {

  operator fun plus(other: Area): Area = Area(m2 + other.m2)
  operator fun minus(other: Area): Area = Area(m2 - other.m2)
  operator fun times(scalar: Double): Area = Area(m2 / scalar)
  operator fun div(scalar: Double): Area = Area(m2 / scalar)
  operator fun div(length: Length): Length = Length(m2 / length.meter)

  operator fun unaryMinus(): Area = Area(-m2)

  fun inMillimeter2(): Double = m2 / AreaUnit.MILLI_METER_2.factor
  fun inCentimeter2(): Double = m2 / AreaUnit.CENTI_METER_2.factor
  fun inMeter2(): Double = m2
  fun inAres(): Double = m2 / AreaUnit.ARES.factor
  fun inHectare(): Double = m2 / AreaUnit.HECTARE.factor

  fun inInch2(): Double = m2 / AreaUnit.INCH_2.factor
  fun inFeet2(): Double = m2 / AreaUnit.FEET_2.factor
  fun inYard2(): Double = m2 / AreaUnit.YARD_2.factor
  fun inAcre(): Double = m2 / AreaUnit.ACRE.factor

  override fun compareTo(other: Area): Int = m2.compareTo(other.m2)
  override fun toString() = "%.1f %s".format(m2, AreaUnit.METER_2.unitName)

  fun toHuman(): String {
    val displayUnit = AreaUnit.values().first { m2 * it.factor > 1.0 }
    return "%.1f %s".format(m2 * displayUnit.factor, displayUnit.unitName)
  }

  fun toHuman(unit: AreaUnit): String {
    return "%.1f %s".format(m2 * unit.factor, unit.unitName)
  }

  companion object {
    val ZERO = Area(0.0)
    val MAX_VALUE = Area(Double.MAX_VALUE)
    val MIN_VALUE = Area(Double.MIN_VALUE)
    val POSITIVE_INF = Area(Double.POSITIVE_INFINITY)
    val NEGATIVE_INF = Area(Double.NEGATIVE_INFINITY)
    val NaN = Area(Double.NaN)

    @JvmOverloads
    @JvmStatic
    fun of(area: Double = 0.0, unit: AreaUnit = AreaUnit.METER_2): Area = Area(area * unit.factor)

    @JvmStatic
    fun parse(areaStr: String): Area {
      if (areaStr.isBlank())
        return ZERO

      try {
        val (v, u) = areaStr.split(" ", limit = 2)
        return Area.of(v.toDouble(), AreaUnit.parse(u))

      } catch(e: Exception) {
        throw NumberFormatException("Invalid Area string. areaStr=$areaStr")
      }
    }
  }
}