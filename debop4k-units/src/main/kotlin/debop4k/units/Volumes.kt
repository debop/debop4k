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

@file:JvmName("Volumnes")

package debop4k.units

import java.io.Serializable

fun Double.cc(): Volumn = Volumn.of(this, VolumnUnit.CC)
fun Double.milliliter(): Volumn = Volumn.of(this, VolumnUnit.MILLILETER)
fun Double.deciliter(): Volumn = Volumn.of(this, VolumnUnit.DECILITER)
fun Double.liter(): Volumn = Volumn.of(this, VolumnUnit.LITER)
fun Double.centimeter3(): Volumn = Volumn.of(this, VolumnUnit.CENTIMETER_3)
fun Double.meter3(): Volumn = Volumn.of(this, VolumnUnit.METER_3)
fun Double.inch3(): Volumn = Volumn.of(this, VolumnUnit.INCH_3)
fun Double.feet3(): Volumn = Volumn.of(this, VolumnUnit.FEET_3)
fun Double.yard3(): Volumn = Volumn.of(this, VolumnUnit.YARD_3)
fun Double.gallon(): Volumn = Volumn.of(this, VolumnUnit.GALLON)
fun Double.barrel(): Volumn = Volumn.of(this, VolumnUnit.BARREL)
fun Double.once(): Volumn = Volumn.of(this, VolumnUnit.ONCE)

enum class VolumnUnit(val unitName: String, val factor: Double) {

  CC("cc", 1.0e-9),
  MILLILETER("ml", 1.0e-3),
  DECILITER("dl", 1.0e-2),
  LITER("l", 1.0),

  CENTIMETER_3("cm^3", 1.0e-3),
  METER_3("m^3", 1.0e3),

  INCH_3("in^3", 1.0e3 / (INCH_IN_METER * INCH_IN_METER * INCH_IN_METER)),
  FEET_3("ft^3", 1.0e3 / (FEET_IN_METER * FEET_IN_METER * FEET_IN_METER)),
  YARD_3("yd^3", 1.0e3 / (YARD_IN_METER * YARD_IN_METER * YARD_IN_METER)),

  GALLON("gl", 1.0 / 0.264172),
  BARREL("barrel", 1.0 / 0.006293),
  ONCE("oz", 1.0 / 33.814022);

  companion object {

    @JvmStatic
    fun parse(unitStr: String): VolumnUnit {
      var lower = unitStr.toLowerCase()
      if (lower.endsWith("s"))
        lower = lower.dropLast(1)

      return VolumnUnit.values().find { it.unitName == lower }
             ?: throw NumberFormatException("Unknown Volumn unit. unitStr=$unitStr")
    }
  }
}

data class Volumn(val liter: Double = 0.0) : Comparable<Volumn>, Serializable {

  operator fun plus(other: Volumn): Volumn = Volumn(liter + other.liter)
  operator fun minus(other: Volumn): Volumn = Volumn(liter - other.liter)
  operator fun times(scalar: Double): Volumn = Volumn(liter * scalar)
  operator fun div(scalar: Double): Volumn = Volumn(liter / scalar)
  operator fun div(area: Area): Length = Length(inMeter3() / area.m2)
  operator fun div(length: Length): Area = Area(inMeter3() / length.meter)
  operator fun unaryMinus(): Volumn = Volumn(-liter)

  fun inCC(): Double = liter / VolumnUnit.CC.factor
  fun inMilliLiter() = liter / VolumnUnit.MILLILETER.factor
  fun inDeciLiter() = liter / VolumnUnit.DECILITER.factor
  fun inCentiMeter3() = liter / VolumnUnit.CENTIMETER_3.factor
  fun inMeter3() = liter / VolumnUnit.METER_3.factor
  fun inInch3() = liter / VolumnUnit.INCH_3.factor
  fun inFeet3() = liter / VolumnUnit.FEET_3.factor
  fun inYard3() = liter / VolumnUnit.YARD_3.factor
  fun inGallon() = liter / VolumnUnit.GALLON.factor
  fun inBarrel() = liter / VolumnUnit.BARREL.factor
  fun inOnce() = liter / VolumnUnit.ONCE.factor

  override fun compareTo(other: Volumn): Int = liter.compareTo(other.liter)

  override fun toString(): String = "%.1f %s".format(liter, VolumnUnit.LITER.unitName)

  companion object {
    @JvmField val ZERO = Volumn(0.0)
    @JvmField val MAX_VALUE = Volumn(Double.MAX_VALUE)
    @JvmField val MIN_VALUE = Volumn(Double.MIN_VALUE)
    @JvmField val POSITIVE_INF = Volumn(Double.POSITIVE_INFINITY)
    @JvmField val NEGATIVE_INF = Volumn(Double.NEGATIVE_INFINITY)
    @JvmField val NaN = Volumn(Double.NaN)

    @JvmOverloads
    @JvmStatic
    fun of(volumn: Double, unit: VolumnUnit = VolumnUnit.LITER): Volumn =
        Volumn(volumn * unit.factor)

    @JvmStatic
    fun parse(volStr: String): Volumn {
      if (volStr.isNullOrBlank())
        return ZERO

      try {
        val (vol, unit) = volStr.split(" ", limit = 2)
        return of(vol.toDouble(), VolumnUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Unknown Volumn string. volStr=$volStr")
      }
    }
  }

}