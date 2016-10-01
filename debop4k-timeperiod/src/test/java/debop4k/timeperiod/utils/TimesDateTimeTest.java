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

package debop4k.timeperiod.utils;

import debop4k.timeperiod.AbstractTimePeriodTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TimesDateTimeTest extends AbstractTimePeriodTest {

  @Test
  public void getDateTest() {
    assertThat(asDate(testDate)).isEqualTo(testDate.withTimeAtStartOfDay());
    assertThat(asDate(testNow)).isEqualTo(testNow.withTimeAtStartOfDay());
  }

  @Test
  public void setDateTest() {
    assertThat(asDate(setDatepart(testDate, testNow))).isEqualTo(testNow.withTimeAtStartOfDay());
    assertThat(asDate(setDatepart(testNow, testDate))).isEqualTo(testDate.withTimeAtStartOfDay());
  }

  @Test
  public void hasTimeOfDayTest() {
    assertThat(hasTimepart(testDate)).isTrue();
    assertThat(hasTimepart(testNow)).isTrue();
    assertThat(hasTimepart(asDate(testNow))).isFalse();

    assertThat(hasTimepart(setTimepart(testNow, 1))).isTrue();
    assertThat(hasTimepart(setTimepart(testNow, 0, 1))).isTrue();

    assertThat(hasTimepart(setTimepart(testNow, 0, 0, 0, 0))).isFalse();
  }

  @Test
  public void setTimeOfDayTest() {
    assertThat(hasTimepart(setTimepart(testDate, testNow))).isTrue();
    assertThat(setTimepart(testDate, testNow).getMillisOfDay()).isEqualTo(testNow.getMillisOfDay());
    assertThat(setTimepart(testNow, testDate).getMillisOfDay()).isEqualTo(testDate.getMillisOfDay());
  }
}
