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

@file:JvmName("Lengths")

package debop4k.units

import java.io.Serializable

const val MILLIMETER_IN_METER = 1.0e-3
const val CENTIMETER_IN_METER = 1.0e-2
const val METER_IN_METER = 1.0
const val KILOMETER_IN_METER = 1.0e3
const val INCH_IN_METER = 39.37
const val FEET_IN_METER = 3.2809
const val YARD_IN_METER = 1.0936
const val MILE_IN_METER = 1609.344

fun Double.millimeter(): Length = Length.of(this, LengthUnit.MILLIMETER)
fun Double.centimeter(): Length = Length.of(this, LengthUnit.CENTIMETER)
fun Double.meter(): Length = Length.of(this)
fun Double.kilometer(): Length = Length.of(this, LengthUnit.KILOMETER)
//fun Double.inch(): Length = Length.of(this, LengthUnit.INCH)
//fun Double.feet(): Length = Length.of(this, LengthUnit.FEET)
//fun Double.yard(): Length = Length.of(this, LengthUnit.YARD)
//fun Double.mile(): Length = Length.of(this, LengthUnit.MILE)

/**
 * 길이(Length)의 단위
 */
enum class LengthUnit(val unitName: String, val factor: Double) {

  MILLIMETER("mm", MILLIMETER_IN_METER),
  CENTIMETER("cm", CENTIMETER_IN_METER),
  METER("m", METER_IN_METER),
  KILOMETER("km", KILOMETER_IN_METER);

//  INCH("inch", INCH_IN_METER),
//  FEET("ft", FEET_IN_METER),
//  YARD("yd", YARD_IN_METER),
//  MILE("ml", MILE_IN_METER);

  companion object {

    @JvmStatic
    fun parse(str: String): LengthUnit {
      val lower = str.toLowerCase()
      return LengthUnit.values().find { it.unitName == lower }
             ?: throw UnsupportedOperationException("Unknwon Length unit string. str=$str")
    }
  }
}

/**
 * 길이를 나타내는 클래스
 */
data class Length(val meter: Double = 0.0) : Comparable<Length>, Serializable {

  operator final fun plus(other: Length): Length = Length(meter + other.meter)
  operator final fun minus(other: Length): Length = Length(meter - other.meter)
  operator final fun times(scalar: Double): Length = Length(meter * scalar)
  operator final fun times(other: Length): Area = Area(meter * other.meter)
  operator final fun div(scalar: Double): Length = Length(meter / scalar)
  operator final fun unaryMinus(): Length = Length(-meter)


  fun inMillimeter(): Double = meter / LengthUnit.MILLIMETER.factor
  fun inCentimeter(): Double = meter / LengthUnit.CENTIMETER.factor
  fun inMeter(): Double = meter
  fun inKilometer(): Double = meter / LengthUnit.KILOMETER.factor
//  fun inInch(): Double = meter / LengthUnit.INCH.factor
//  fun inFeet(): Double = meter / LengthUnit.FEET.factor
//  fun inYard(): Double = meter / LengthUnit.YARD.factor
//  fun inMile(): Double = meter / LengthUnit.MILE.factor

  override fun compareTo(other: Length): Int = meter.compareTo(other.meter)
  override fun toString(): String = "%.1f %s".format(meter, LengthUnit.METER.factor)

  fun toHuman(): String {
    val value = Math.abs(meter)
    val displayUnit = LengthUnit.values().last { value / it.factor > 1.0 }
    return "%.1f %s".format(meter / displayUnit.factor, displayUnit.unitName)
  }

  fun toHuman(unit: LengthUnit): String {
    return "%.1f %s".format(meter / unit.factor, unit.unitName)
  }


  companion object {
    @JvmField val ZERO = Length(0.0)
    @JvmField val MIN_VALUE = Length(Double.MIN_VALUE)
    @JvmField val MAX_VALUE = Length(Double.MAX_VALUE)
    @JvmField val POSITIVE_INF = Length(Double.POSITIVE_INFINITY)
    @JvmField val NEGATIVE_INF = Length(Double.NEGATIVE_INFINITY)
    @JvmField val NaN = Length(Double.NaN)

    @JvmOverloads
    @JvmStatic
    fun of(length: Double = 0.0, unit: LengthUnit = LengthUnit.METER): Length =
        Length(length * unit.factor)

    @JvmStatic
    fun parse(str: String): Length {
      if (str.isBlank()) {
        return ZERO
      }
      try {
        val (length, unit) = str.split(" ", limit = 2)
        return of(length.toDouble(), LengthUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Invalid Length string. str=$str")
      }
    }
  }

}