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

package debop4k.web.rest

import java.io.Serializable

/**
 * RESTful API 의 결과
 * @author sunghyouk.bae@gmail.com
 */
data class ApiResult
@JvmOverloads
constructor(val header: ApiHeader = DefaultApiHeader, val body: Any? = null) : Serializable {

  constructor(code: Int, body: Any?) : this(ApiHeader(code = code), body)

}

val EmptyApiResult = ApiResult()