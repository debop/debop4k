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

import java.util.Set;

/**
 * 메타 정보 (key-value) 를 가지는 엔티티의 인터페이스
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface MetaEntity extends PersistentObject {

  /**
   * 메타 키에 해당하는 메타 값을 반환합니다.
   *
   * @param key 메타 키
   * @return 메타 값
   */
  MetaValue getMetaValue(String key);

  /**
   * 엔티티가 가진 메타 키 컬렉션을 반환합니다.
   *
   * @return 메타 키의 컬렉션
   */
  Set<String> getMetaKeys();

  /**
   * 메타 정보를 추가합니다.
   *
   * @param key       메타 키
   * @param metaValue 메타 값
   */
  void addMeta(String key, MetaValue metaValue);

  /**
   * 메타 정보를 추가합니다.
   *
   * @param key   메타 키
   * @param value 메타 값
   */
  void addMeta(String key, Object value);

  /**
   * 해당 메타 정보를 삭제합니다.
   *
   * @param key 메타 키
   */
  void removeMeta(final String key);
}
