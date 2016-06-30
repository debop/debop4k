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

@file:JvmName("annotation")

package debop4k.spring.beans

import org.springframework.beans.annotation.AnnotationBeanUtils
import org.springframework.util.StringValueResolver

fun Annotation.copyPropertiesToBean(bean: Any, vararg excludedProperties: String)
    = AnnotationBeanUtils.copyPropertiesToBean(this, bean, *excludedProperties)

fun Annotation.copyPropertiesToBean(bean: Any, resolver: StringValueResolver, vararg excludedProperties: String)
    = AnnotationBeanUtils.copyPropertiesToBean(this, bean, resolver, *excludedProperties)
