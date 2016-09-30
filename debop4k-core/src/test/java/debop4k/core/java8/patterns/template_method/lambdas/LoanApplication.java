/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.java8.patterns.template_method.lambdas;

import debop4k.core.java8.patterns.template_method.ApplicationDenied;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class LoanApplication {

  private final Criteria identity;
  private final Criteria creditHistory;
  private final Criteria incomeHistory;

  public LoanApplication(Criteria identity,
                         Criteria creditHistory,
                         Criteria incomeHistory) {
    this.identity = identity;
    this.creditHistory = creditHistory;
    this.incomeHistory = incomeHistory;
  }

  public void checkLoanApplication() throws ApplicationDenied {
    identity.check();
    creditHistory.check();
    incomeHistory.check();
    reportFindings();
  }

  private void reportFindings() {
    log.debug("report findings...");
  }
}
