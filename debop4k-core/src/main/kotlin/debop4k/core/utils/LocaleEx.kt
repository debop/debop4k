/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

@file:JvmName("LocaleEx")

package debop4k.core.utils

import debop4k.core.collections.fastListOf
import debop4k.core.shouldNotBeEmpty
import org.slf4j.LoggerFactory
import java.util.*

private val log = LoggerFactory.getLogger("LocaleEx")

/** 현 Locale 이 기본 Locale 인가 */
fun Locale?.isDefault(): Boolean = Locale.getDefault() == this

/** 지정한 Locale 이 null 이면 기본 Locale 을 반환합니다 */
fun Locale?.orDefault(): Locale = this ?: Locale.getDefault()

/** [Locale] 의 부모 Locale을 구합니다. */
fun Locale?.getParent(): Locale? {
  if (this == null)
    return null

  if (variant.length > 0 && (language.length > 0 || country.length > 0)) {
    return Locale(language, country)
  }
  if (country.length > 0)
    return Locale(language)

  return null
}

/** [Locale] 의 조상 Locale 들을 구합니다. */
fun Locale?.getParents(): List<Locale> {
  val results = mutableListOf<Locale>()
  if (this == null)
    return results

  var current = this
  while (current != null) {
    results.add(0, current)
    current = current.getParent()
  }
  return results
}

/**
 * Calculate the filenames for the given bundle basename and Locale,
 * appending language code, country code, and variant code.
 * E.g.: basename "messages", Locale "de_AT_oo" -> "messages_de_AT_OO",
 * "messages_de_AT", "messages_de".
 * <p>Follows the rules defined by {@link java.util.Locale#toString()}.
 *
 * @param basename the basename of the bundle
 * @param locale   the locale
 * @return the List of filenames to check
 */
fun calculateFilenamesForLocale(basename: String, locale: Locale): List<String> {

  basename.shouldNotBeEmpty("basename")
  log.trace("Locale에 해당하는 파일명을 조합합니다. basename=$basename, locale=$locale")

  val results = fastListOf<String>()

  val language = locale.language
  val country = locale.country
  val variant = locale.variant

  log.trace("language=$language, country=$country, variant=$variant")

  val temp = StringBuilder(basename)
  temp.append("_")

  if (language.length > 0) {
    temp.append(language)
    results.add(0, temp.toString())
  }

  temp.append("_")

  if (country.length > 0) {
    temp.append(country)
    results.add(0, temp.toString())
  }

  if (variant.length > 0 && (language.length > 0 || country.length > 0)) {
    temp.append("_").append(variant)
    results.add(0, temp.toString())
  }
  results.add(basename)

  log.trace("Locale에 해당하는 파일명을 조합했습니다. results={}", results)
  return results
}

