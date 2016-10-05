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


import debop4k.core.kodatimes.KodaTimex;
import debop4k.timeperiod.TimePeriodChain;
import debop4k.timeperiod.utils.Durations;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;


public class SchoolDay extends TimePeriodChain {

  public static Duration LessonDuration = Durations.minuteOf(50);
  public static Duration LargeBreakDuration = Durations.minuteOf(15);
  public static Duration ShortBreakDuration = Durations.minuteOf(5);

  private final DateTime moment;
  private final Lesson lesson1;
  private final ShortBreak break1;
  private final Lesson lesson2;
  private final LargeBreak break2;
  private final Lesson lesson3;
  private final ShortBreak break3;
  private final Lesson lesson4;

  public SchoolDay() {
    this(KodaTimex.today().plusHours(8));
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

    super.addAll(Arrays.asList(lesson1, break1, lesson2, break2, lesson3, break3, lesson4));
  }

  private static final long serialVersionUID = -3027311601657222641L;

  public DateTime getMoment() {return this.moment;}

  public Lesson getLesson1() {return this.lesson1;}

  public ShortBreak getBreak1() {return this.break1;}

  public Lesson getLesson2() {return this.lesson2;}

  public LargeBreak getBreak2() {return this.break2;}

  public Lesson getLesson3() {return this.lesson3;}

  public ShortBreak getBreak3() {return this.break3;}

  public Lesson getLesson4() {return this.lesson4;}
}
