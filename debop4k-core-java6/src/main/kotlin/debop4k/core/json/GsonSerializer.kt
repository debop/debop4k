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

package debop4k.core.json

import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import debop4k.core.EMPTY_STRING

/**
 * GsonSerializer
 * @author debop sunghyouk.bae@gmail.com
 */
open class GsonSerializer(val gson: Gson) : AbstractJsonSerializer() {


  override fun toString(graph: Any?): String {
    if (graph == null)
      return EMPTY_STRING

    return gson.toJson(graph)
  }

  override fun <T> fromString(jsonText: String?, clazz: Class<T>): T? {
    if (jsonText.isNullOrBlank())
      return null as T

    return gson.fromJson(jsonText, clazz)
  }

  companion object {

    @JvmStatic
    fun of(): GsonSerializer {
      val builder = GsonBuilder().enableComplexMapKeySerialization().serializeNulls()

      // NOTE : Gson 에 joda time 관련 converter를 추가
      // NOTE : Chronology 와 같이 기본생성자가 없는 수형에 대해서 예외가 발생한다.
      val gson = Converters.registerAll(builder).create()

      return GsonSerializer(gson)
    }

    @JvmStatic
    fun of(gson: Gson): GsonSerializer {
      return GsonSerializer(gson)
    }
  }
}