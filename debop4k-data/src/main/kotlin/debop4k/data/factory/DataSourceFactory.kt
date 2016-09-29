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

package debop4k.data.factory

import debop4k.data.DatabaseSetting
import javax.sql.DataSource

/**
 * [DataSource] 를 생성해주는 Factory 인터페이스
 * @author sunghyouk.bae@gmail.com
 */
interface DataSourceFactory {

  /**
   * [DataSource] 생성
   */
  fun create(setting: DatabaseSetting): DataSource

}