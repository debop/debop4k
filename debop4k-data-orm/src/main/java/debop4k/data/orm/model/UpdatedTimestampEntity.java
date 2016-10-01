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

package debop4k.data.orm.model;

import org.joda.time.DateTime;

import javax.persistence.PreUpdate;

/**
 * 최근 수정일자를 속성으로 가지고 있는 엔티티를 표현하는 인터페이스
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface UpdatedTimestampEntity {

  /**
   * 엔티티의 최근 갱신 일자
   *
   * @return 최근 갱신 일자
   */
  DateTime getUpdatedTimestamp();

  /**
   * 엔티티 갱신 전에 호출되는 함수
   */
  @PreUpdate
  void updateUpdatedTimestamp();
}
