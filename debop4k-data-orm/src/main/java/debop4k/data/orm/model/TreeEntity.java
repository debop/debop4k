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
 * 트리 구조를 가지는 엔티티를 표현하는 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface TreeEntity<T extends TreeEntity<T>> extends PersistentObject {

  /**
   * 현재 노드의 부모 노드를 반환합니다. null이면, 현 노드가 최상위 노드 (root node) 입니다.
   *
   * @return 부모 엔티티
   */
  T getParent();

  /**
   * 부모 노드를 설정합니다. (null 을 설정하면, 최상위 노드(root node) 가 됩니다.)
   *
   * @param parent 부모가 될 엔티티
   */
  void setParent(T parent);

  /**
   * 자식 노드 집합 (Set)
   *
   * @return 자식노드의 컬렉션
   */
  Set<T> getChildren();

  /**
   * 현재 노드의 트리 구조상의 위치를 나타냅니다.
   *
   * @return 트리 구조 상의 위치 정보
   */
  TreeNodePosition getNodePosition();


  public void addChildren(T... children);

  public void removeChildren(T... children);
}
