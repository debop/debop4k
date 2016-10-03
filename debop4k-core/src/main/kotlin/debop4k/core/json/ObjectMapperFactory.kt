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

package debop4k.core.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import debop4k.core.loggerOf

/**
 * ObjectMapperFactory
 * @author debop sunghyouk.bae@gmail.com
 */
object ObjectMapperFactory {

  private val log = loggerOf(javaClass)

  @JvmStatic
  fun newMapper(): ObjectMapper {
    val mapper = ObjectMapper()

    // Joda-time 라이브러리들의 수형들을 Mapping 하도록 한다.
    mapper.registerModule(JodaModule())

    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

    // Single Value 도 Array 로 인식하게끔 한다.
    mapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    mapper.configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
    mapper.configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
    mapper.configure(READ_ENUMS_USING_TO_STRING, true)

    // 알 수 없는 속성에 대해서는 무시하도록 한다.
    mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

    return mapper
  }
}