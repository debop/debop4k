/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.spring.web.servlet

import org.springframework.web.servlet.ModelAndView

operator fun ModelAndView.set(attrName: String, attrValue: Any?): Unit {
  this.addObject(attrName, attrValue)
}

fun ModelAndView.put(attrValue: Any?): ModelAndView {
  this.addObject(attrValue)
  return this
}

fun ModelAndView.put(attrName: String, attrValue: Any?): ModelAndView {
  this.addObject(attrName, attrValue)
  return this
}

fun ModelAndView.putAll(vararg objects: Pair<String, Any?>): ModelAndView {
  this.addAllObjects(mapOf(*objects))
  return this
}

fun ModelAndView.putAll(modelMap: Map<String, *>): ModelAndView {
  this.addAllObjects(modelMap)
  return this
}