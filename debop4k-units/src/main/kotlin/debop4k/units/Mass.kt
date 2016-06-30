/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("masses")

package debop4k.units

import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 * 무게를 나타내는 단위
 * @author debop sunghyouk.bae@gmail.com
 */
enum class MassUnit(val abbr: String, val factor: Double) {

  Milligram("mg", Mass.MILLIGRAM_IN_GRAM),
  Gram("g", Mass.GRAM_IN_GRAM),
  Kilogram("kg", Mass.KILOGRAM_IN_GRAM),
  Ton("ton", Mass.TON_IN_GRAM),

  Grain("gr", Mass.GRAIN_IN_GRAM),
  Once("oz", Mass.ONCE_IN_GRAM),
  Found("fl", Mass.FOUND_IN_GRAM);

  companion object {

    fun valueOf(unit: String): MassUnit {
      var lower = unit.toLowerCase()
      if (lower.endsWith("s")) {
        lower = lower.dropLast(1)
      }
      return when (lower) {
        MassUnit.Milligram.abbr -> MassUnit.Milligram
        MassUnit.Gram.abbr -> MassUnit.Gram
        MassUnit.Kilogram.abbr -> MassUnit.Kilogram
        MassUnit.Ton.abbr -> MassUnit.Ton
        else -> throw NumberFormatException("알 수 없는 MassUnit 문자열입니다. unit=$unit")
      }
    }
  }
}

open class Mass(var gram: Double) : Comparable<Mass>, Serializable {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun compareTo(other: Mass): Int = if (gram > other.gram) 1 else -1

  override fun equals(other: Any?): Boolean {
    return other is Mass && gram == other.gram
  }

  override fun hashCode(): Int {
    return gram.hashCode()
  }

  override fun toString(): String {
    return "$gram.g"
  }

  fun toHuman(): String {
    var unit = MassUnit.Gram
    var display = Math.abs(gram)

    if (display > TON_IN_GRAM) {
      display /= TON_IN_GRAM
      unit = MassUnit.Ton
      return "%.1f %s".format(display * Math.signum(gram), unit.abbr)
    }

    if (display < 1.0) {
      unit = MassUnit.Milligram
      display *= MILLIGRAM_IN_GRAM
    } else if (display > 1000.0) {
      unit = MassUnit.Kilogram
      display /= KILOGRAM_IN_GRAM
    }
    return "%.1f %s".format(display * Math.signum(gram), unit.abbr)
  }

  companion object {
    private val log = LoggerFactory.getLogger(Mass::class.java)

    val ZERO = Mass(0.0)
    val POSITIVE_INF = Mass(Double.POSITIVE_INFINITY)
    val NEGATIVE_INF = Mass(Double.NEGATIVE_INFINITY)

    val MILLIGRAM_IN_GRAM: Double = 1.0 / 1000.0
    val GRAM_IN_GRAM: Double = 1.0
    val KILOGRAM_IN_GRAM: Double = 1000.0
    val TON_IN_GRAM: Double = 1000.0 * 1000.0
    val GRAIN_IN_GRAM: Double = 1.0 / 15.432
    val ONCE_IN_GRAM: Double = 28.349523125
    val FOUND_IN_GRAM: Double = 453.592

    fun valueOf(str: String?): Mass {
      log.trace("parse string to Mass. str={}", str)
      if (str.isNullOrBlank())
        return Mass.ZERO

      try {
        val parts = str!!.split(".")
        var v = parts.get(0)
        var u = parts.get(1)

        if (v.isNullOrBlank()) v = ""
        if (u.startsWith(".")) u = u.drop(1)

        log.trace("parsed mass. v={}, u={}", v, u)

        val vv = v.toDouble()
        val uu = MassUnit.valueOf(u)
        return Mass(vv * uu.factor)

      } catch(e: Throwable) {
        throw NumberFormatException("무게를 나타내는 문자열을 파싱하는데 실패했습니다. str=$str")
      }
    }
  }
}

fun Double.milligram(): Mass = Mass(this * MassUnit.Milligram.factor)
fun Double.gram(): Mass = Mass(this)
fun Double.kilogram(): Mass = Mass(this * MassUnit.Kilogram.factor)
fun Double.ton(): Mass = Mass(this * MassUnit.Ton.factor)

fun Double.grain(): Mass = Mass(this * MassUnit.Grain.factor)
fun Double.once(): Mass = Mass(this * MassUnit.Once.factor)
fun Double.found(): Mass = Mass(this * MassUnit.Found.factor)

fun Double.toMass(): Mass = Mass(this)

fun Mass.inUnit(unit: MassUnit): Double = when (unit) {
  MassUnit.Milligram -> inMilligram()
  MassUnit.Grain -> inGram()
  MassUnit.Kilogram -> inKilogram()
  MassUnit.Grain -> inGrain()
  MassUnit.Once -> inOnce()
  MassUnit.Found -> inFound()
  else -> throw UnsupportedOperationException("Unknown unit type. $unit")
}

fun Mass.inMilligram(): Double = gram / Mass.MILLIGRAM_IN_GRAM
fun Mass.inGram(): Double = gram
fun Mass.inKilogram(): Double = gram / Mass.KILOGRAM_IN_GRAM
fun Mass.inTon(): Double = gram / Mass.TON_IN_GRAM

fun Mass.inGrain(): Double = gram / Mass.GRAIN_IN_GRAM
fun Mass.inOnce(): Double = gram / Mass.ONCE_IN_GRAM
fun Mass.inFound(): Double = gram / Mass.FOUND_IN_GRAM

operator fun Mass.plus(that: Mass): Mass {
  gram += that.gram
  return this
}

operator fun Mass.plus(gramValue: Double): Mass {
  gram += gramValue
  return this
}

operator fun Mass.minus(that: Mass): Mass {
  gram -= that.gram
  return this
}

operator fun Mass.minus(gramValue: Double): Mass {
  gram -= gramValue
  return this
}

operator fun Mass.times(that: Mass): Mass {
  gram *= that.gram
  return this
}

operator fun Mass.times(scala: Double): Mass {
  gram *= scala
  return this
}

operator fun Mass.div(that: Mass): Mass {
  gram /= that.gram
  return this
}

operator fun Mass.div(scala: Double): Mass {
  gram /= scala
  return this
}

