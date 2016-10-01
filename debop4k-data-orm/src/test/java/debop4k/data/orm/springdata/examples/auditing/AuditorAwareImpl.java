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

import lombok.Setter;
import org.springframework.data.domain.AuditorAware;

/**
 * 보안을 요하는 데이터에 대해 감사를 수행할 수 있도록 합니다.
 */
public class AuditorAwareImpl implements AuditorAware<AuditableUser> {

  @Setter
  private AuditableUser auditor;

  @Override
  public AuditableUser getCurrentAuditor() {
    return this.auditor;
  }
}
