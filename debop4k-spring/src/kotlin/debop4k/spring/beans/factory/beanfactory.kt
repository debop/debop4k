/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("beanfactory")

package debop4k.spring.beans.factory

import org.springframework.beans.factory.BeanFactory

operator fun BeanFactory.get(name: String): Any = getBean(name)

operator fun <T> BeanFactory.get(requiredType: Class<T>): T = getBean(requiredType)

operator fun <T> BeanFactory.get(name: String, requiredType: Class<T>): T = getBean(name, requiredType)

operator fun BeanFactory.get(name: String, vararg args: Any): Any = getBean(name, *args)