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

/**
 * LongTreeEntity
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 11.
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class LongTreeEntity<T extends TreeEntity<T>> extends AbstractTreeEntity<T, Long> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetIdentifier() {
    this.setId(null);
    this.persisted = false;
  }

  private static final long serialVersionUID = 5497373717396770983L;
}
