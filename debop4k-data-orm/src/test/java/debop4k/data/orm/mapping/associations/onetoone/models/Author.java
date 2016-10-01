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

package debop4k.data.orm.mapping.associations.onetoone.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Author
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 6.
 */
@Entity(name = "OneToOne_Author")
@Getter
@NoArgsConstructor
public class Author extends IntEntity {

  public Author(String name) {
    this.name = name;
  }

  @Setter
  private String name;

  @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn(name = "authorId")
  Biography biography = new Biography(this);

  @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn(name = "authorId")
  AuthorPicture picture = new AuthorPicture(this);

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 4954009719307178592L;
}
