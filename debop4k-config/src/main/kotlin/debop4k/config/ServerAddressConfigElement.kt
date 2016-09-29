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

package debop4k.config

/**
 * 서버 주소와 Port 정보를 제공하는 환경설정 요소
 *
 * @author sunghyouk.bae@gmail.com
 */
interface ServerAddressConfigElement : ConfigSupport {

  /** 서버 주소 */
  val host: String
    get() = config.loadString("host", "localhost") ?: "localhost"

  /** Port */
  val port: Int
    get() = config.loadInt("port", 0)

}