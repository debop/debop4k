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

package debop4k.web.utils

import debop4k.core.json.*
import debop4k.core.utils.asInt
import debop4k.core.utils.toUtf8Bytes
import org.springframework.http.MediaType
import javax.servlet.ServletRequest


val APPLICATION_JSON_UTF8: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
                                                 MediaType.APPLICATION_JSON.subtype,
                                                 Charsets.UTF_8)

private val jsonSerialzer: JsonSerializer by lazy { JacksonSerializer() }

/**
 * 객체를 Json 직렬화를 수행하고, 바이트 배열로 반환합니다.
 *
 * @return Json 직렬화된 정보를 담은 바이트 배열
 */
fun Any?.toJsonByteArray(): ByteArray = jsonSerialzer.toByteArray(this)

/**
 * 객체가 Map 인 경우, Map의 정보를 HTTP GET 방식의 URL 을 만듭니다.
 *
 * @return 객체 정보를 Form UrlEncoding 방식으로 인코딩한 정보
 */
fun Map<*, *>.toUrlEncodedBytes(): ByteArray {
  val builder = StringBuilder()
  val mapper = DefaultObjectMapper

  val props = mapper.convertValue(this, Map::class.java)

  props.entries.forEach {
    builder.append(it.key).append("=").append(it.value).append("&")
  }

  return builder.toString().toUtf8Bytes()
}


@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun java.lang.Integer.getPageNo(): Int = this.asInt()

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun java.lang.Integer.getPageSize(defaultSize: Int = 10): Int
    = this.asInt(defaultSize)

/**
 * Request 에서 parameter 정보를 가져옵니다. 만약 없다면 defaultValue 를 반환합니다.
 *
 * @param paramName    파라미터 이름
 * @param defaultValue 파라미터의 값이 없을 때 제공되는 값
 * @return 파라미터 값
 */
fun ServletRequest.getParamValue(paramName: String, defaultValue: String): String {
  return getParameter(paramName) ?: defaultValue
}