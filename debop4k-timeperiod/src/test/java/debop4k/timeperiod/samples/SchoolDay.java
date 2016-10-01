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

package debop4k.timeperiod.samples;


import debop4k.timeperiod.TimePeriodChain;
import debop4k.timeperiod.utils.Durations;
import debop4k.timeperiod.utils.Times;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;


@Getter
public class SchoolDay extends TimePeriodChain {

  public static Duration LessonDuration = Durations.minutes(50);
  public static Duration LargeBreakDuration = Durations.minutes(15);
  public static Duration ShortBreakDuration = Durations.minutes(5);

  private final DateTime moment;
  private final Lesson lesson1;
  private final ShortBreak break1;
  private final Lesson lesson2;
  private final LargeBreak break2;
  private final Lesson lesson3;
  private final ShortBreak break3;
  private final Lesson lesson4;

  public SchoolDay() {
    this(Times.today().plusHours(8));
  }

  public SchoolDay(DateTime moment) {
    this.moment = moment;

    lesson1 = new Lesson(moment);
    moment = moment.plus(lesson1.getDuration());

    break1 = new ShortBreak(moment);
    moment = moment.plus(break1.getDuration());

    lesson2 = new Lesson(moment);
    moment = moment.plus(lesson2.getDuration());

    break2 = new LargeBreak(moment);
    moment = moment.plus(break2.getDuration());

    lesson3 = new Lesson(moment);
    moment = moment.plus(lesson3.getDuration());

    break3 = new ShortBreak(moment);
    moment = moment.plus(break3.getDuration());

    lesson4 = new Lesson(moment);
    moment = moment.plus(lesson4.getDuration());

    super.addAll(lesson1, break1, lesson2, break2, lesson3, break3, lesson4);
  }

  private static final long serialVersionUID = -3027311601657222641L;
}
