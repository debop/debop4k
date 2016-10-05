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

@file:JvmName("Modelx")

package debop4k.spring.ui

import org.springframework.ui.Model
import org.springframework.ui.ModelMap

operator fun Model.set(attributeName: String, attributeValue: Any?): Model {
  return this.addAttribute(attributeName, attributeValue)
}

fun Model.addAllAttributes(vararg attributes: Pair<String, Any?>): Model {
  return this.addAllAttributes(mapOf(*attributes))
}

fun Model.mergeAttributes(vararg attributes: Pair<String, Any?>): Model {
  return this.mergeAttributes(mapOf(*attributes))
}

operator fun ModelMap.set(attributeName: String, attributeValue: Any?): ModelMap {
  return this.addAttribute(attributeName, attributeValue)
}

fun ModelMap.addAllAttributes(vararg attributes: Pair<String, Any?>): ModelMap {
  return this.addAllAttributes(mapOf(*attributes))
}

fun ModelMap.mergeAttributes(vararg attributes: Pair<String, Any?>): ModelMap {
  return this.mergeAttributes(mapOf(*attributes))
}
