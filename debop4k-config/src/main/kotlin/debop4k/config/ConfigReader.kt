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

@file:JvmName("ConfigReader")

package debop4k.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigValue
import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.*


private val log = LoggerFactory.getLogger("ConfigReader")

private fun assertPath(path: String): Unit {
  assert(path.isNotBlank()) { "경로가 지정되지 않았습니다 (null 이거나 blank 입니다)" }
}

/**
 * Config 의 지정된 경로에서 값을 조회합니다.

 * @param path   경로
 * @param getter Config Value 를 가져오는 함수
 * @param defaultValue  읽기 실패 시의 기본 값
 * @return Config 값
 */
private inline fun <T> loadConfigValue(path: String, getter: () -> T, defaultValue: T): T {
  log.trace("환경설정 정보를 읽습니다... path={}", path)
  assertPath(path)
  try {
    return getter()
  } catch (ignored: Exception) {
    log.warn("환경설정 정보를 읽어오는데 실패했습니다. path=$path, 기본값[$defaultValue]을 반환합니다.")
    return defaultValue
  }

}

/**
 * 환경설정에서 String 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadString(path: String, defaultValue: String? = null): String? {
  return loadConfigValue(path,
                         { this.getString(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;String&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadStringList(path: String, defaultValue: List<String> = emptyList<String>()): List<String> {
  return loadConfigValue(path,
                         { this.getStringList(path) },
                         defaultValue)
}


/**
 * 환경설정에서 Boolean 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadBool(path: String, defaultValue: Boolean = false): Boolean {
  return loadConfigValue(path,
                         { this.getBoolean(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Boolean&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadBoolList(path: String, defaultValue: List<Boolean> = emptyList()): List<Boolean> {
  return loadConfigValue(path,
                         { this.getBooleanList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Integer 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadInt(path: String, defaultValue: Int = 0): Int {
  return loadConfigValue(path,
                         { this.getInt(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Integer&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadIntList(path: String, defaultValue: List<Int> = emptyList()): List<Int> {
  return loadConfigValue(path,
                         { this.getIntList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Long 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadLong(path: String, defaultValue: Long = 0L): Long {
  return loadConfigValue(path,
                         { this.getLong(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Long&gt; 수형의 설정 값을 읽습니다
 *
 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadLongList(path: String, defaultValue: List<Long> = emptyList()): List<Long> {
  return loadConfigValue(path,
                         { this.getLongList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Double 수형의 설정 값을 읽습니다
 *
 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadDouble(path: String, defaultValue: Double = 0.0): Double {
  return loadConfigValue(path,
                         { this.getDouble(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Double&gt; 수형의 설정 값을 읽습니다
 *
 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadDoubleList(path: String, defaultValue: List<Double> = emptyList()): List<Double> {
  return loadConfigValue(path,
                         { this.getDoubleList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Number 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadNumber(path: String, defaultValue: Number = 0): Number {
  return loadConfigValue(path,
                         { this.getNumber(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Number&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadNumberList(path: String, defaultValue: List<Number> = emptyList()): List<Number> {
  return loadConfigValue(path,
                         { this.getNumberList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Object 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadObject(path: String, defaultValue: Any? = null): Any? {
  return loadConfigValue(path,
                         { this.getAnyRef(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Object&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@SuppressWarnings("unchecked")
@JvmOverloads
fun Config.loadAnyRefList(path: String, defaultValue: List<Any?> = emptyList()): List<Any?> {
  return loadConfigValue(path,
                         { this.getAnyRefList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Bytes 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadBytes(path: String, defaultValue: Long = 0L): Long {
  return loadConfigValue(path,
                         { this.getBytes(path) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Byte&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadBytesList(path: String, defaultValue: List<Long> = emptyList()): List<Long> {
  return loadConfigValue(path,
                         { this.getBytesList(path) },
                         defaultValue)
}

/**
 * 환경설정에서 Duration 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadDuration(path: String, unit: TimeUnit, defaultValue: Long = 0L): Long {
  return loadConfigValue(path,
                         { this.getDuration(path, unit) },
                         defaultValue)
}

/**
 * 환경설정에서 List&lt;Duration&gt; 수형의 설정 값을 읽습니다

 * @param path   설정 key 의 경로
 * @param unit   시간 단위
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadDurationList(path: String, unit: TimeUnit, defaultValue: List<Long> = emptyList()): List<Long> {
  return loadConfigValue(path,
                         { this.getDurationList(path, unit) },
                         defaultValue)
}

/**
 * 환경설정에서 ConfigValue 를 읽어온다.

 * @param path   설정 key 의 경로
 * @return 환경설정 값
 */
@JvmOverloads
fun Config.loadConfigValue(path: String, defaultValue: ConfigValue? = null): ConfigValue? {
  return loadConfigValue(path,
                         { this.getValue(path) },
                         defaultValue)
}

/**
 * 환경설정의 모든 key=value 값을 [Properties] 으로 빌드하여 반환합니다.

 * @return 환경설정 정보를 담은 Properties 인스턴스
 */
fun Config.asProperties(): Properties {
  val props = Properties()
  try {
    props.putAll(this.asMap())
  } catch (ignored: Throwable) {
    log.warn("환경설정을 읽는데 실패했습니다.", ignored)
  }
  return props
}

/**
 * 환경설정의 모든 key=value 값을 Map 으로 빌드하여 반환합니다.
 * @return 환경설정 정보를 담은 Map 인스턴스
 */
fun Config.asMap(): MutableMap<String, String> {
  val map = Maps.mutable.of<String, String>()
  try {
    this.entrySet().forEach { entry ->
      val value = entry.value?.unwrapped()?.toString() ?: ""
      map.put(entry.key, value)
    }
  } catch (ignored: Throwable) {
    log.warn("환경설정을 읽는데 실패했습니다.", ignored)
  }
  return map
}