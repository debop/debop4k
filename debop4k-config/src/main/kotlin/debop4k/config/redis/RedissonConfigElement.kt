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

package debop4k.config.redis

import com.typesafe.config.Config
import debop4k.config.ConfigSupport
import debop4k.config.loadString

/**
 * Redisson 을 이용한 접속 정보
 *
 * @author debop sunghyouk.bae@gmail.com
 */
open class RedissonConfigElement(override val config: Config) : ConfigSupport {

  /** Redisson Connection 정보를 담은 yaml 파일의 경로 */
  val configPath: String
      by lazy { config.loadString("configPath", "redisson.yml")!! }

}