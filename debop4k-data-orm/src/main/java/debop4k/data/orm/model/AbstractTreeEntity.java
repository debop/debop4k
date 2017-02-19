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

import debop4k.core.ToStringHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tree 구조 (Self Reference 구조) 형태의 엔티티의 추상화 클래스
 * 예: Organization, Folder 등에 사용하면 된다.
 *
 * @param <T>   엔티티 수형
 * @param <TId> 엔티티 Identifier 수형
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractTreeEntity<T extends TreeEntity<T>, TId extends Serializable>
    extends AbstractHibernateEntity<TId> implements TreeEntity<T> {

  /**
   * Identifier
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private TId id;

  /**
   * 부모 엔티티
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @LazyToOne(LazyToOneOption.PROXY)
  @JoinColumn(name = "parentId")
  private T parent;

  /**
   * 자식 노드 컬렉션
   */
  @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  private Set<T> children = new HashSet<T>();

  /**
   * 자식 노드를 추가합니다.
   *
   * @param children 자식으로 추가할 엔티티들
   */
  @SuppressWarnings("unchecked")
  public void addChildren(T... children) {
    for (T child : children) {
      child.setParent((T) this);
      getChildren().add(child);
    }
  }

  /**
   * 자식 노드를 삭제합니다.
   *
   * @param children 자식에서 제거할 엔티티들
   */
  @SuppressWarnings("unchecked")
  public void removeChildren(T... children) {
    for (T child : children) {
      child.setParent(null);
      getChildren().remove(child);
    }
    getChildren().removeAll(Arrays.asList(children));
  }

  /**
   * 노드의 위치 정보
   */
  @Embedded
  private TreeNodePosition nodePosition = new TreeNodePosition();

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("getParent", parent)
                .add("nodePosition", nodePosition);
  }

  private static final long serialVersionUID = -7981217533182101670L;
}
