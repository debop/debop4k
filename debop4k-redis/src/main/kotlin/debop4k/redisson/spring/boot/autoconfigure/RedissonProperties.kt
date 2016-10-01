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

package debop4k.redisson.spring.boot.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

/**
 * Redisson 을 이용하여 Redis 에 접속하기 위한 환경설정 정보
 *
 * <code>
 * debop4k.redisson.config: redisson.yml
 * </code>
 *
 * @author sunghyouk.bae@gmail.com
 */
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
open class RedissonProperties {

  /**
   * Redisson 환경설정을 가진 Yaml 파일의 리소스 경로를 지정하면 됩니다.
   */
  var config: Resource? = null

  companion object {
    const val PREFIX = "debop4k.redisson"
  }
}