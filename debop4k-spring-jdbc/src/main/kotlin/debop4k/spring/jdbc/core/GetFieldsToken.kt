/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.spring.jdbc.core

/**
 * [ResultSet] 으로부터 필드 정보를 얻기 위한 메소드를 제공하는 클래스입니다.
 * @author sunghyouk.bae@gmail.com
 */
class GetFieldsToken<T>(val withFieldName: (String) -> T,
                        val withIndex: (Int) -> T) {

  operator fun get(columnIndex: Int): T = withIndex(columnIndex)

  operator fun get(columnLabel: String) = withFieldName(columnLabel)
}