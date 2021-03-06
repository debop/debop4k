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

import com.google.gson.Gson
import debop4k.core.utils.EMPTY_STRING


/**
 * google gson 라이브러리를 이용한 JSON Serializer 입니다.
 * @author debop sunghyouk.bae@gmail.com
 */
class GsonSerializer @JvmOverloads constructor(val gson: Gson = DefaultGson) : JsonSerializer {

  override fun <T : Any> toString(graph: T?): String {
    if (graph == null)
      return EMPTY_STRING

    return gson.toJson(graph)
  }

  override fun <T : Any> fromString(jsonText: String?, clazz: Class<T>): T? {
    if (jsonText.isNullOrBlank())
      return null

    return gson.fromJson(jsonText, clazz)
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(gson: Gson = DefaultGson): GsonSerializer = GsonSerializer(gson)
  }
}