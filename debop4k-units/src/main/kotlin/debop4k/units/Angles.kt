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

@file:JvmName("Angles")

package debop4k.units

import java.io.Serializable


const val DEGREE_FORMAT = "%.1f deg"
const val RADIAN_FORMAT = "%.4f rad"

/** 각도 단위를 Degree 를 Radian 으로 변경 */
fun degreeToRadian(degree: Double): Double = degree * Math.PI / 180.0

/** 각도 단위를 Redian 을 Degree 로 변환 */
fun radianToDegree(rad: Double): Double = rad * 180.0 / Math.PI


/** 지정된 각도 (Degree) 를 가지는 Angle 을 생성 */
fun Int.degree(): Angle = Angle(this.toDouble())

/** 지정된 각도 (Degree) 를 가지는 Angle 을 생성 */
fun Long.degree(): Angle = Angle(this.toDouble())

/** 지정된 각도 (Degree) 를 가지는 Angle 을 생성 */
fun Float.degree(): Angle = Angle(this.toDouble())

/** 지정된 각도 (Degree) 를 가지는 Angle 을 생성 */
fun Double.degree(): Angle = Angle(this)

/** 지정된 각도 (Radian) 를 가지는 Angle 을 생성 */
fun Double.radian(): Angle = Angle(radianToDegree(this))

operator fun Int.times(angle: Angle): Angle = angle.times(this.toDouble())
operator fun Double.times(angle: Angle): Angle = angle.times(this)

/**
 * 각도 단위 종류
 */
enum class AngleUnit(val unitName: String) {

  Degree("deg"),
  Radian("rad");

  companion object {
    @JvmStatic fun parse(str: String): AngleUnit {
      var lower = str.toLowerCase()
      if (lower.endsWith("s"))
        lower = lower.dropLast(1)

      return AngleUnit.values().find { it.unitName == lower }
             ?: throw NumberFormatException("Unknwon AngleUnit format. str=$str")
    }
  }
}

/**
 * 각도를 나타내는 클래스입니다
 */
data class Angle(val degree: Double = 0.0) : Comparable<Angle>, Serializable {

  fun inDegree(): Double = this.degree
  fun inRadian(): Double = degreeToRadian(this.degree)

  fun in360() = Angle(degree % 360.0)

  operator fun plus(angle: Angle): Angle = Angle(degree + angle.degree)
  operator fun plus(scalar: Double): Angle = Angle(degree + scalar)
  operator fun minus(angle: Angle): Angle = Angle(degree - angle.degree)
  operator fun minus(scalar: Double): Angle = Angle(degree - scalar)

  operator fun times(scalar: Double): Angle = Angle(degree * scalar)
  operator fun div(scalar: Double): Angle = Angle(degree / scalar)

  operator fun unaryMinus(): Angle = Angle(-degree)

  fun toHuman(unit: AngleUnit = AngleUnit.Degree): String = when (unit) {
    AngleUnit.Degree -> DEGREE_FORMAT.format(inDegree())
    AngleUnit.Radian -> RADIAN_FORMAT.format(inRadian())
  }

  override fun compareTo(other: Angle): Int = degree.compareTo(other.degree)
  override fun toString(): String = "%.4f %s".format(degree, AngleUnit.Degree.unitName)

  companion object {

    @JvmField val ZERO = Angle(0.0)
    @JvmField val DEGREE_ZERO = ZERO
    @JvmField val DEGREE_90 = Angle(90.0)
    @JvmField val DEGREE_180 = Angle(180.0)
    @JvmField val DEGREE_270 = Angle(270.0)
    @JvmField val DEGREE_360 = Angle(360.0)

    @JvmField val MAX_VALUE = Angle(Double.MAX_VALUE)
    @JvmField val MIN_VALUE = Angle(Double.MIN_VALUE)
    @JvmField val POSITIVE_INF = Angle(Double.POSITIVE_INFINITY)
    @JvmField val NEGATIVE_INF = Angle(Double.NEGATIVE_INFINITY)
    @JvmField val NaN = Angle(Double.NaN)

    @JvmOverloads
    @JvmStatic
    fun of(angle: Double = 0.0, unit: AngleUnit = AngleUnit.Degree): Angle = when (unit) {
      AngleUnit.Degree -> degree(angle)
      AngleUnit.Radian -> radian(angle)
      else -> throw IllegalArgumentException("Unknown Angle unit. unit=$unit")
    }

    @JvmStatic
    fun degree(angle: Double) = Angle(angle)

    @JvmStatic
    fun radian(angle: Double) = Angle(radianToDegree(angle))

    @JvmStatic
    fun parse(str: String?): Angle {
      if (str.isNullOrBlank())
        return ZERO

      try {
        val (angle, unit) = str!!.split(" ", limit = 2)
        return of(angle.toDouble(), AngleUnit.parse(unit))
      } catch(e: Exception) {
        throw NumberFormatException("Invalid Angle string. str=$str")
      }
    }
  }
}
