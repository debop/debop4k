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

package debop4k.http.retrofit

import debop4k.core.json.DEFAULT_OBJECT_MAPPER
import debop4k.core.loggerOf
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


private val log = loggerOf("Retrofitx")

@JvmField val DefaultConverterFactory: Converter.Factory
    = JacksonConverterFactory.create(DEFAULT_OBJECT_MAPPER)
//    = MoshiConverterFactory.create()

/**
 * [Retrofit] 인스턴스를 생성합니다.
 */
@JvmOverloads
fun retrofitOf(baseUrl: String, converterFactory: Converter.Factory = DefaultConverterFactory): Retrofit {
  return Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(converterFactory)
      .build()
}

fun <T> Retrofit.service(serviceClazz: Class<T>): T {
  return create(serviceClazz)
}