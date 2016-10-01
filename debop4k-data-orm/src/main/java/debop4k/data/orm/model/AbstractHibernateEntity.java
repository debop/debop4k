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
import debop4k.core.utils.Hashx;
import debop4k.core.utils.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import java.io.Serializable;


/**
 * Hibernate, JPA 의 모든 엔티티의 기본 클래스입니다.
 *
 * @param <TId> 엔티티 Identifier의 수형
 * @author sunghyouk.bae@gmail.com
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class AbstractHibernateEntity<TId extends Serializable>
    extends AbstractPersistentObject implements HibernateEntity<TId> {

  /**
   * {@inheritDoc}
   */
  abstract public TId getId();

  public void resetIdentifier() {
    // nothing to do.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @PostPersist
  public void onSave() {
    super.onSave();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @PostLoad
  public void onLoad() {
    super.onLoad();
  }

  /**
   * 엔티티가 같은 지 비교합니다.
   * 일반적인 Language 차원의 비교가 아닌,
   * 엔티티의 Identifier를 비교하던가, 저장 전 객체 (transient object) 인 경우에는 Business identifier 를 구해 비교합니다.
   *
   * @param obj 비교할 엔티티
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    boolean isSameType = (obj != null) && Objects.equals(getClass(), obj.getClass());

    if (isSameType) {
      HibernateEntity<TId> entity = (HibernateEntity<TId>) obj;
      int hash = (getId() != null) ? Hashx.compute(getId()) : hashCode();
      return hasSameNonDefaultIdAs(getId(), entity) ||
             ((!isPersisted() || !entity.isPersisted()) && hasSameBusinessSignature(hash, entity));
    }
    return false;
  }

  /**
   * 엔티티의 고유 값을 제공합니다.
   * persistent object 인 경우에는 identifier의 hash code를 반환하고,
   * transient object 인 경우에는 business logic 에 의한 고유 값을 반환합니다.
   */
  @Override
  public int hashCode() {
    return (getId() == null) ? System.identityHashCode(this) : Hashx.compute(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("id", getId());
  }

  /**
   * 엔티티가 저장되었다고 한다면, identifier 값이 할당되어 있을 것이고, identifier만으로 두 엔티티가 같은지 검사합니다.
   *
   * @param id     현 엔티티의 identifer의 값
   * @param entity 비교 대상 엔티티
   * @param <TId>  엔티티의 identifer의 수형
   * @return 두 엔티티의 identifer 가 같은지 여부
   */
  private static <TId extends Serializable> boolean hasSameNonDefaultIdAs(TId id, HibernateEntity<TId> entity) {
    return entity != null && Objects.equals(id, entity.getId());
  }

  /**
   * transient object 는 아직 identifier 의 값이 없으므로, business logic 상 엔티티를 구분할 수 있는 값으로 비교해야 합니다.
   * 업무로직 상 구분 상 같은지 여부를 판단하는 메소드입니다.
   *
   * @param hash  현 객체의 고유 값
   * @param other 비교할 대상 객체
   * @param <TId> 엔티티의 고유 값을 나타내는 identifier의 수형
   * @return 업무로직 상 두 값이 같은 값을 나타내는지 여부
   */
  private static <TId extends Serializable> boolean hasSameBusinessSignature(int hash, HibernateEntity<TId> other) {
    boolean notNull = (other != null);
    if (notNull) {
      int otherHash = (other.getId() != null) ? Hashx.compute(other.getId()) : other.hashCode();
      return hash == otherHash;
    }
    return false;
  }

  private static final long serialVersionUID = 8683011155609908789L;
}
