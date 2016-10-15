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

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuditableConfiguration.class})
@Transactional
@Slf4j
public class AuditableUserSampleTest {

  @Inject AuditableUserRepository repository;
  @Inject AuditorAwareImpl auditorAware;

  // TODO: resources/META-INF/orm.xml 에 정의되어 있는데, java-config 로 옮기자!!!
  @Inject AuditingEntityListener listener;

  @Test
  public void auditEntityCreation() throws Exception {
    assertThat(ReflectionTestUtils.getField(listener, "handler")).isNotNull();

    AuditableUser user = new AuditableUser();
    user.setUsername("debop");

    auditorAware.setAuditor(user);

    user = repository.save(user);
    user = repository.save(user);

    assertThat(user).isEqualTo(user.getCreatedBy());
    assertThat(user).isEqualTo(user.getLastModifiedBy());
  }
}
