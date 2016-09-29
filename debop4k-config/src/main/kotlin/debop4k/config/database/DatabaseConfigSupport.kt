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

package debop4k.config.database

import debop4k.config.ConfigSupport

/**
 * Database 관련 환경 설정 정보를 표현하는 {@link Config} 를 읽어드리는 Adapter.
 * <p>
 * 설정 정보는 아래와 같이 정의하면 됩니다.
 * <pre>
 *   <code>
 *     application {
 *       database {
 *         driverClass="org.h2.driver"
 *         jdbcUrl="jdbc://sql:xxxxx"
 *         username="sa
 *         password=""
 *         maxPoolSize = 100
 *         minIdleSize = 2
 *       }
 *     }
 *   </code>
 * </pre>
 *
 * @author sunghyouk.bae@gmail.com
 */
interface DatabaseConfigSupport : ConfigSupport {

  /** Database configuration element */
  val database: DatabaseConfigElement
    get() = DatabaseConfigElement(config.getConfig("database"))

}