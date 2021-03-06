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

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * 계층형 자료 구조를 표현하며, 조상과 자손에 대한 컬렉션 속성을 가지는 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface HierarchyEntity<T extends HierarchyEntity<T>> {

  /**
   * @return 엔티티의 Identifier
   */
  Serializable getId();

  /**
   * @return 조상 엔티티의 컬렉션
   */
  LinkedHashSet<T> getAncestors();

  /**
   * @return 자손 엔티티의 컬렉션
   */
  LinkedHashSet<T> getDescendents();
}
