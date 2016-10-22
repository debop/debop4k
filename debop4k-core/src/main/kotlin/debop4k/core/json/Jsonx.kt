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

@file:JvmName("Jsonx")

package debop4k.core.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi


val DefaultMoshi: Moshi by lazy {
  Moshi.Builder().build()
}

/**
 * 기본 Gson 인스턴스
 */
val DefaultGson: Gson by lazy {

  val builder = GsonBuilder().enableComplexMapKeySerialization().serializeNulls()

  // NOTE : Gson 에 joda time 관련 converter를 추가
  // NOTE : Chronology 와 같이 기본생성자가 없는 수형에 대해서 예외가 발생한다.
  Converters.registerAll(builder).create()
}

/** 기본 Jackson Object Mapper */
val DefaultObjectMapper: ObjectMapper by lazy { createDefaultObjectMapper() }

/**
 * Jackson의 [ObjectMapper]를 생성합니다.
 */
fun createDefaultObjectMapper(): ObjectMapper {
  return ObjectMapper().apply {

    // Joda-time 라이브러리들의 수형들을 Mapping 하도록 한다.
    registerModule(JodaModule())

    // Kotlin 언어의 수형을 Mapping 한다.
    registerModule(KotlinModule())

    setSerializationInclusion(JsonInclude.Include.NON_NULL)

    // Single Value 도 Array 로 인식하게끔 한다.
    configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
    configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
    configure(READ_ENUMS_USING_TO_STRING, true)

    // 알 수 없는 속성에 대해서는 무시하도록 한다.
    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
  }
}

/** Jackson 라이브러리를 사용하는 Serializer */
val DefaultJacksonSerializer by lazy { JacksonSerializer() }

/** GSON 라이브러리를 사용하는 Serializer */
val DefaultGsonSerializer by lazy { GsonSerializer() }


