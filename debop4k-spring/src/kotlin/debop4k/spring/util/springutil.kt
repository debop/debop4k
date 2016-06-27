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

@file:JvmName("springutils")

package debop4k.spring.util

import debop4k.core.functional.GetterSetterOperation
import org.springframework.util.StopWatch

/**
 * [System#getProperty], [System#setProperty] 를 kotlin 방식으로 수행해주는 변수
 */
val sysproperty: GetterSetterOperation<String, String>
    = GetterSetterOperation({ k -> System.getProperty(k) },
                            { k, v -> System.setProperty(k, v) })

fun stopWatch(id: String = "", body: StopWatch.() -> Unit): StopWatch {
  val watch = StopWatch()
  watch.body()
  return watch
}

fun <T> StopWatch.task(name: String = "", body: () -> T): T {
  start(name)
  try {
    return body()
  } finally {
    stop()
  }
}