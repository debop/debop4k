/*
 * Copyright (c) 2017-2020 by Coupang. Some rights reserved.
 * See the project homepage at: http://gitlab.coupang.net
 * Licensed under the Coupang License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://coupang.net/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.data.orm.jinq.mapping.bidirection;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Team
 *
 * @author debop
 * @since 2017. 2. 15.
 */
@Entity
@Table(name = "team")
@Data
class Team {

  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String name;

  @OneToMany(mappedBy = "team")
  private List<Member> members = Lists.newArrayList();
}
