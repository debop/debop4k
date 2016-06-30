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

package debop4k.spring.jdbc.core.config

import debop4k.spring.jdbc.core.config.ExecutionValue.INIT
import org.springframework.jdbc.datasource.init.ScriptUtils

/**
 * Embeded Database 에 대한 설정을 수행하는 정보를 표현합니다.
 * @author sunghyouk.bae@gmail.com
 */
class EmbeddedDatabaseTag {

  val scripts: MutableList<ScriptTag> = arrayListOf()

  fun script(location: String,
             encoding: String? = null,
             separator: String = ScriptUtils.DEFAULT_STATEMENT_SEPARATOR,
             execution: ExecutionValue = INIT) {
    scripts.add(ScriptTag(location, encoding, separator, execution))
  }
}