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

package debop4k.timeperiod.base;

import debop4k.core.kodatimes.KodaTimes;
import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.models.Timepart;
import debop4k.timeperiod.utils.Durations;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TimepartTest extends AbstractTimePeriodTest {

  @Test
  public void timeConstructorTest() {
    DateTime now = DateTime.now();
    Timepart time = new Timepart(now);

    log.debug("now=[{}], time=[{}]", now, time);

    assertThat(time.getHourOfDay()).isEqualTo(now.getHourOfDay());
    assertThat(time.getMinuteOfHour()).isEqualTo(now.getMinuteOfHour());
    assertThat(time.getSecondOfMinute()).isEqualTo(now.getSecondOfMinute());
    assertThat(time.getMillisOfSecond()).isEqualTo(now.getMillisOfSecond());

    assertThat(time.getTotalMillis()).isEqualTo(now.getMillisOfDay());
  }

  @Test
  public void emptyDateTimeConstructor() {
    DateTime today = KodaTimes.today();
    Timepart time = new Timepart(today);

    assertThat(time.getTotalMillis()).isEqualTo(0);

    assertThat(time.getHourOfDay()).isEqualTo(0);
    assertThat(time.getMinuteOfHour()).isEqualTo(0);
    assertThat(time.getSecondOfMinute()).isEqualTo(0);
    assertThat(time.getMillisOfSecond()).isEqualTo(0);

    assertThat(time.getTotalHours()).isEqualTo(0);
    assertThat(time.getTotalMinutes()).isEqualTo(0);
    assertThat(time.getTotalSeconds()).isEqualTo(0);
    assertThat(time.getTotalMillis()).isEqualTo(0);
  }

  @Test
  public void constructorTest() {
    Timepart time = Timepart.of(18, 23, 56, 344);

    assertThat(time.getHourOfDay()).isEqualTo(18);
    assertThat(time.getMinuteOfHour()).isEqualTo(23);
    assertThat(time.getSecondOfMinute()).isEqualTo(56);
    assertThat(time.getMillisOfSecond()).isEqualTo(344);
  }

  @Test
  public void emptyConstructorTest() {
    Timepart time = new Timepart();

    assertThat(time.getHourOfDay()).isEqualTo(0);
    assertThat(time.getMinuteOfHour()).isEqualTo(0);
    assertThat(time.getSecondOfMinute()).isEqualTo(0);
    assertThat(time.getMillisOfSecond()).isEqualTo(0);

    assertThat(time.getTotalHours()).isEqualTo(0);
    assertThat(time.getTotalMinutes()).isEqualTo(0);
    assertThat(time.getTotalSeconds()).isEqualTo(0);
    assertThat(time.getTotalMillis()).isEqualTo(0);
  }

  @Test
  public void durationTest() {
    Duration test = Durations.hourOf(18, 23, 56, 344);
    Timepart time = Timepart.of(test);

    assertThat(time.getHourOfDay()).isEqualTo(18);
    assertThat(time.getMinuteOfHour()).isEqualTo(23);
    assertThat(time.getSecondOfMinute()).isEqualTo(56);
    assertThat(time.getMillisOfSecond()).isEqualTo(344);

    assertThat(time.getTotalMillis()).isEqualTo(test.getMillis());
  }

  @Test
  public void getDateTimeTest() {
    DateTime now = KodaTimes.now();
    Duration test = Durations.hourOf(18, 23, 56, 344);
    Timepart time = Timepart.of(test);

    assertThat(time.toDateTime(now)).isEqualTo(now.withTimeAtStartOfDay().plus(test));
  }

  @Test
  public void getEmptyDateTimeTest() {
    DateTime today = KodaTimes.today();
    Timepart time = new Timepart();

    assertThat(time.toDateTime(today)).isEqualTo(today);
    assertThat(time.toDateTime(today).getMillisOfDay()).isEqualTo(0);
  }

}
