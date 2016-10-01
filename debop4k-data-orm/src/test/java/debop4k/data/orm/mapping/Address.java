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

package debop4k.data.orm.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class Address implements Serializable {

  @Column(length = 255)
  private String street;

  @Column(length = 64)
  private String city;

  @Column(length = 24)
  private String state;

  @Column(length = 24)
  private String country;

  @Column(columnDefinition = "CHAR(6)")
  private String zipcode;

  private static final long serialVersionUID = -1349808777345762694L;
}
