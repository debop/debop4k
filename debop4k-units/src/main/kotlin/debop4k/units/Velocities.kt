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

@file:JvmName("Velocities")

package debop4k.units

import java.io.Serializable

fun Double.toMps(): Velocity = Velocity.of(this, VelocityUnit.METER_PER_SEC)
fun Double.toMph(): Velocity = Velocity.of(this, VelocityUnit.METER_PER_HOUR)
fun Double.toKps(): Velocity = Velocity.of(this, VelocityUnit.KILOMETER_PER_SEC)
fun Double.toKph(): Velocity = Velocity.of(this, VelocityUnit.KILOMETER_PER_HOUR)

fun Double.toMilePerSec(): Velocity = Velocity.of(this, VelocityUnit.MILE_PER_SEC)
fun Double.toMilePerHr(): Velocity = Velocity.of(this, VelocityUnit.MILE_PER_HOUR)

fun Double.toKnot(): Velocity = Velocity.of(this, VelocityUnit.KNOT)
fun Double.toMach(): Velocity = Velocity.of(this, VelocityUnit.MACH)

enum class VelocityUnit(val unitName: String, val factor: Double) {

  METER_PER_SEC("m/s", 1.0),
  METER_PER_HOUR("m/h", 1.0 / 60),

  KILOMETER_PER_SEC("km/s", 1000.0),
  KILOMETER_PER_HOUR("km/h", 1000.0 / 60),

  MILE_PER_SEC("ml/s", MILE_IN_METER),
  MILE_PER_HOUR("ml/h", MILE_IN_METER / 60),

  KNOT("knot", 0.51444444),
  MACH("mach", 340.0);

  companion object {
    @JvmStatic
    fun parse(unitStr: String): VelocityUnit {
      val lower = unitStr.toLowerCase()

      return VelocityUnit.values().find { it.unitName == lower }
          ?: throw NumberFormatException("Unknown Velocity unit. unitStr=$unitStr")
    }
  }
}

data class Velocity(val ms: Double = 0.0) : Comparable<Velocity>, Serializable {

  operator fun plus(other: Velocity): Velocity = Velocity(ms + other.ms)
  operator fun minus(other: Velocity): Velocity = Velocity(ms - other.ms)
  operator fun times(scalar: Double): Velocity = Velocity(ms * scalar)
  operator fun div(scalar: Double): Velocity = Velocity(ms / scalar)
  operator fun unaryMinus(): Velocity = Velocity(-ms)

  fun inMps(): Double = ms
  fun inMph(): Double = ms / VelocityUnit.KILOMETER_PER_HOUR.factor
  fun inKps(): Double = ms / VelocityUnit.KILOMETER_PER_SEC.factor
  fun inKph(): Double = ms / VelocityUnit.KILOMETER_PER_HOUR.factor

  fun inMilePerSec(): Double = ms / VelocityUnit.MILE_PER_SEC.factor
  fun inMilePerHr(): Double = ms / VelocityUnit.MILE_PER_HOUR.factor
  fun inKnot(): Double = ms / VelocityUnit.KNOT.factor
  fun inMach(): Double = ms / VelocityUnit.MACH.factor

  override fun compareTo(other: Velocity): Int = ms.compareTo(other.ms)
  override fun toString(): String = "%.1f %s".format(ms, VelocityUnit.METER_PER_SEC.unitName)

  companion object {

    final val ZERO = Velocity(0.0)
    final val MAX_VALUE = Velocity(Double.MAX_VALUE)
    final val MIN_VALUE = Velocity(Double.MIN_VALUE)
    final val POSITIVE_INF = Velocity(Double.POSITIVE_INFINITY)
    final val NEGATIVE_INF = Velocity(Double.NEGATIVE_INFINITY)
    final val NaN = Velocity(Double.NaN)

    @JvmStatic
    fun of(speed: Double, unit: VelocityUnit = VelocityUnit.METER_PER_SEC): Velocity =
        Velocity(speed * unit.factor)

    @JvmStatic
    fun parse(velocityStr: String): Velocity {
      if (velocityStr.isNullOrBlank())
        return ZERO

      try {
        val (velocity, unit) = velocityStr.split(" ", limit = 2)
        return of(velocity.toDouble(), VelocityUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Unknown Velocity string. velocityStr=$velocityStr")
      }
    }
  }
}