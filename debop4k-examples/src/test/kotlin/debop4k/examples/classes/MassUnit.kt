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

package debop4k.examples.classes

// 상수 역할을 하는 것은 항상 const 가 필요하다
const val MILLIGRAM_IN_GRAM: Double = 1.0 / 1000.0
const val GRAM_IN_GRAM: Double = 1.0
const val KILOGRAM_IN_GRAM: Double = 1000.0
const val TON_IN_GRAM: Double = 1000.0 * 1000.0

enum class MassUnit(val unitName: String, val factor: Double) {

  MILLIGRAM("mg", MILLIGRAM_IN_GRAM),
  GRAM("g", GRAM_IN_GRAM),
  KILOGRAM("kg", KILOGRAM_IN_GRAM),
  TON("ton", TON_IN_GRAM);

  companion object {

    @JvmStatic fun parse(unitStr: String): MassUnit {
      var lower = unitStr.toLowerCase()
      if (lower.endsWith("s")) {
        lower = lower.dropLast(1)
      }

      return MassUnit.values().find { it.unitName == lower }
             ?: throw NumberFormatException("알 수 없는 MassUnit 문자열입니다. unit=$unitStr")
    }
  }
}
