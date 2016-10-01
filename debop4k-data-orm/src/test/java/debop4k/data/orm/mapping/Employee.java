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

import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.data.orm.jpa.converters.DateTimeAsTimestamp;
import debop4k.data.orm.model.AbstractHibernateEntity;
import debop4k.data.orm.model.UpdatedTimestampEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Entity
@DynamicInsert
@DynamicUpdate
@SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq")
@Getter
@Setter
public class Employee
    extends AbstractHibernateEntity<Integer>
    implements UpdatedTimestampEntity {

  private static final long serialVersionUID = -4355818208585098282L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
  @Column(name = "employee_id")
  private Integer id;

  @Override
  public void resetIdentifier() {
    this.id = null;
    this.persisted = false;
  }

  @Column(name = "empNo", nullable = false, length = 32)
  private String empNo;

  @Column(name = "empName", nullable = false, length = 32)
  private String name;

  @Column(name = "empEmail", length = 128)
  private String email;

  //@Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.DateTimeUserType")
  @Convert(converter = DateTimeAsTimestamp.class)
  private DateTime birthDay;

  @Embedded
  private Address address = new Address();

  //@Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.DateTimeUserType")
  @Convert(converter = DateTimeAsTimestamp.class)
  private DateTime updatedTimestamp;

  // Spring Data JPA 에서 추가된 annotation 으로 생성된 날짜를 관리합니다.
  @CreatedDate
  private Date createdTime;

  // Spring Data JPA 에서 추가된 annotation 으로 마지막 변경 시각을 관리합니다.
  // 굳이 UpdatedTimestampEntity 를 사용하지 않아도 된다. ㅠ.ㅠ
  @LastModifiedDate
  private Date lastModifiedTime;

  // @CreatedDate, @LastModifiedDate 는 AuditorAware 의 구현체가 Bean 으로 등록되어 있어야 한다.
  @Override
  public void updateUpdatedTimestamp() {
    updatedTimestamp = DateTime.now();
  }

  @Override
  public int hashCode() {
    return Hashx.compute(empNo, name);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("empNo", empNo)
                .add("name", name)
                .add("email", email);
  }
}
