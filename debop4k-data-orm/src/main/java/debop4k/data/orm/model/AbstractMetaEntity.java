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

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 메타데이터를 가지는 엔티티를 표현합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class AbstractMetaEntity<TId extends Serializable>
    extends AbstractHibernateEntity<TId> implements MetaEntity {

  private Map<String, MetaValue> metaMap = new LinkedHashMap<String, MetaValue>();

  @Override
  public MetaValue getMetaValue(String key) {
    return metaMap.get(key);
  }

  @Override
  public Set<String> getMetaKeys() {
    return metaMap.keySet();
  }

  @Override
  public void addMeta(String key, MetaValue metaValue) {
    metaMap.put(key, metaValue);
  }

  @Override
  public void addMeta(String key, Object value) {
    metaMap.put(key, SimpleMetaValue.of(value));
  }

  @Override
  public void removeMeta(String key) {
    metaMap.remove(key);
  }

  private static final long serialVersionUID = 3467173957072617225L;
}
