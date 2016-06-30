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

@file:JvmName("styles")

package debop4k.spring.core

import org.springframework.core.style.*

val ToStringCreator.append: ToStringCreatorAppendTokens
  get() = ToStringCreatorAppendTokens(this)

fun ToStringCreator(obj: Any,
                    body: ToStringCreator.() -> Unit): ToStringCreator {
  val creator = ToStringCreator(obj)
  creator.body()
  return creator
}

fun ToStringCreator(obj: Any,
                    styler: ValueStyler = DefaultValueStyler(),
                    body: ToStringCreator.() -> Unit): ToStringCreator {
  val creator = ToStringCreator(obj, styler)
  creator.body()
  return creator
}

fun ToStringCreator(obj: Any,
                    styler: ToStringStyler = DefaultToStringStyler(DefaultValueStyler()),
                    body: ToStringCreator.() -> Unit): ToStringCreator {
  val creator = ToStringCreator(obj, styler)
  creator.body()
  return creator
}