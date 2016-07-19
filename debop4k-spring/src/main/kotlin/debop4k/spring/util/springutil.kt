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

@file:JvmName("springutils")

package debop4k.spring.util

import debop4k.core.functional.GetterSetterOperation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch

private val log: Logger by lazy { LoggerFactory.getLogger("SpringUtils") }

/**
 * [System#getProperty], [System#setProperty] 를 kotlin 방식으로 수행해주는 변수
 */
val sysproperty: GetterSetterOperation<String, String>
    = GetterSetterOperation({ k -> System.getProperty(k) },
                            { k, v -> System.setProperty(k, v) })

/**
 * [StopWatch] 실행
 */
fun stopWatch(id: String = "", body: StopWatch.() -> Unit): StopWatch {
  log.trace("Start stopwatch ... id=$id")
  val watch = StopWatch()
  watch.body()
  return watch
}

/**
 * [StopWatch] 실행
 */
fun <T> StopWatch.task(name: String = "", body: () -> T): T {
  this.start(name)
  try {
    return body()
  } finally {
    this.stop()
    log.trace("{}", this.prettyPrint())
  }
}