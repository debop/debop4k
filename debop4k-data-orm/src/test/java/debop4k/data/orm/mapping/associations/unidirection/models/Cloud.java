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

package debop4k.data.orm.mapping.associations.unidirection.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Cloud
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 6.
 */
@Entity
@Getter
@Setter
public class Cloud extends IntEntity {

  protected Cloud() {}

  public Cloud(String kind, Double length) {
    this.kind = kind;
    this.length = length;
  }

  String kind;
  Double length;

  // One To Many 연결. (Cloud_Snowflake 테이블에 연결 정보를 둔다)
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "CloudSnakeflakes",
             joinColumns = {@JoinColumn(name = "cloudId")},
             inverseJoinColumns = {@JoinColumn(name = "snowflakeId")})
  @Fetch(FetchMode.SUBSELECT)
  Set<Snowflake> producedSnowflakes = new HashSet<Snowflake>();

  @Override
  public int hashCode() {
    return Hashx.compute(kind, length);
  }

  private static final long serialVersionUID = 8614884793854473244L;
}
