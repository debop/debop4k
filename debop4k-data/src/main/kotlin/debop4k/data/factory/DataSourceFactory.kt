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

import debop4k.config.database.DatabaseSetting
import javax.sql.DataSource

/**
 * [DataSource] 를 생성해주는 Factory의 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
interface DataSourceFactory {

  /**
   * [DataSource] 를 생성합니다.
   * @param setting 데이터베이스 연결 정보
   * @return 생성된 [DataSource] 인스턴스. 실패 시 null 반환
   */
  fun create(setting: DatabaseSetting): DataSource

}