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

package debop4k.core.logback

import org.joda.time.DateTime

/**
 * Logback 의 로그 정보를 표현하는 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @version $Id: $Id
 * @since 2015. 8. 9.
 */
data class LogDocument(val serverName: String = "localhost",
                       val applicationName: String = "",
                       val logger: String = "",
                       val levelInt: Int = 0,
                       val levelStr: String = "",
                       val threadName: String = "",
                       val message: String = "",
                       val timestamp: DateTime = DateTime.now(),
                       val marker: String? = null,
                       val exception: String? = null,
                       val stackTrace: List<String> = emptyList())