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

package debop4k.data.orm.springdata.examples.auditing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.Entity;

/**
 * 감사 대상 정보 ( 생성자, 생성일자, 갱신자, 갱신일자 ) 를 관리할 수 있습니다.
 * <pre>
 * AbstractAuditable<User, PK> 에서 User 는 감시자가 나타냅니다. 예제에서는 간단하게 하기위해 같은 클래스로 구현했습니다.
 * </pre>
 */
@Entity
@Getter
@Setter
public class AuditableUser extends AbstractAuditable<AuditableUser, Integer> {

  private String username;

  private static final long serialVersionUID = 8419926917897062660L;
}
