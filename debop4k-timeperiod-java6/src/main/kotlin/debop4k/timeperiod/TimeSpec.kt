/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

@file:JvmName("TimeSpec")

package debop4k.timeperiod

import debop4k.timeperiod.models.DayOfWeek
import org.eclipse.collections.api.list.ImmutableList
import org.eclipse.collections.impl.factory.Lists
import org.joda.time.DateTime
import org.joda.time.Duration

/** 1년의 개월 수 (12)  */
const val MonthsPerYear = 12
/** 1년의 반기 수 (2)  */
const val HalfyearsPerYear = 2
/** 1년의 분기 수 (4)  */
const val QuartersPerYear = 4
/** 반기의 분기 수 (2)  */
const val QuartersPerHalfyear = QuartersPerYear / HalfyearsPerYear
/** 반기의 개월 수 (6)  */
const val MonthsPerHalfyear = MonthsPerYear / HalfyearsPerYear
/** 분기의 개월 수 (3)  */
const val MonthsPerQuarter = MonthsPerYear / QuartersPerYear
/** 1년의 최대 주차 (54주)  */
const val MaxWeeksPerYear = 54
/** 한달의 최대 일수 (31)  */
const val MaxDaysPerMonth = 31
/** 한 주의 일 수 (7)  */
const val DaysPerWeek = 7
/** 하루의 시간 (24)  */
const val HoursPerDay = 24
/** 단위 시간의 분 (60)  */
const val MinutesPerHour = 60
/** 단위 분의 초 (60)  */
const val SecondsPerMinute = 60
/** 단위 초의 밀리 초 (1000)  */
const val MillisPerSecond = 1000
/** 분당 밀리초  */
const val MillisPerMinute = MillisPerSecond * 60L
/** 시간당 밀리초  */
const val MillisPerHour = MillisPerMinute * 60L
/** 일당 밀리초  */
const val MillisPerDay = MillisPerHour * 24L

//    public static long TicksPerMillisecond = 10000L;
//    public static long TicksPerSecond = TicksPerMillisecond * 1000L;
//    public static long TicksPerMinute = TicksPerSecond * 60L;
//    public static long TicksPerHour = TicksPerMinute * 60L;
//    public static long TicksPerDay = TicksPerHour * 24L;
/** 1년의 시작 월 (1)  */

const val CalendarYearStartMonth = 1
/** 한 주의 주중 일 수 (5)  */
const val WeekDaysPerWeek = 5
/** 한 주의 주말 일 수 (2)  */
const val WeekEndsPerWeek = 2

/** 주중 요일  */
val Weekdays = arrayOf<DayOfWeek>(DayOfWeek.MONDAY,
                                  DayOfWeek.TUESDAY,
                                  DayOfWeek.WEDNESDAY,
                                  DayOfWeek.THURSDAY,
                                  DayOfWeek.FRIDAY)

val WeekdayList: ImmutableList<DayOfWeek> = Lists.immutable.of(*Weekdays)
/** 주말 요일  */
val Weekends = arrayOf<DayOfWeek>(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
val WeekendList: ImmutableList<DayOfWeek> = Lists.immutable.of(*Weekends)

fun DayOfWeek.isWeekday(): Boolean = WeekdayList.contains(this)
fun DayOfWeek.isWeekend(): Boolean = WeekendList.contains(this)

/** 한 주의 첫번째 주중 요일 (월요일)  */
val FirstWorkingDayOfWeek = DayOfWeek.MONDAY

/** 한 주의 첫번째 요일 (월요일) - ISO8601을 따른다.  */
val FirstDayOfWeek = DayOfWeek.MONDAY

/** 전반기에 속하는 월 (1월~6월)  */
val FirstHalfyearMonths = intArrayOf(1, 2, 3, 4, 5, 6)

/** 후반기에 속하는 월 (7월~12월)  */
val SecondHalfyearMonths = intArrayOf(7, 8, 9, 10, 11, 12)
/** 1분기 시작 월 (1월)  */
val FirstQuarterMonth = 1
/** 2분기 시작 월 (4월)  */
val SecondQuarterMonth = FirstQuarterMonth + MonthsPerQuarter
/** 3분기 시작 월 (7월)  */
val ThirdQuarterMonth = SecondQuarterMonth + MonthsPerQuarter
/** 4분기 시작 월 (10월)  */
val FourthQuarterMonth = ThirdQuarterMonth + MonthsPerQuarter
/** 1분기에 속하는 월 (1월~3월)  */
val FirstQuarterMonths = intArrayOf(1, 2, 3)
/** 2분기에 속하는 월 (4월~6월)  */
val SecondQuarterMonths = intArrayOf(4, 5, 6)
/** 3분기에 속하는 월 (7월~9월)  */
val ThirdQuarterMonths = intArrayOf(7, 8, 9)
/** 4분기에 속하는 월 (10월~12월)  */
val FourthQuarterMonths = intArrayOf(10, 11, 12)
/**
 * Number of daysView in a non-leap year
 */
const val DaysPerYear = 365L
/** Number of daysView in 4 yearsView  */
const val DaysPer4Years = DaysPerYear * 4 + 1
/** Number of daysView in 100 yearsView  */
const val DaysPer100Years = DaysPer4Years * 25 - 1
/** Number of daysView in 400 yearsView  */
const val DaysPer400Years = DaysPer100Years * 4 + 1
/** Number of daysView from 1/1/0001 pudding 12/31/1600  */
const val DaysTo1601 = DaysPer400Years * 4
/** Number of daysView from 1/1/0001 pudding 12/30/1899  */
const val DaysTo1899 = DaysPer400Years * 4 + DaysPer100Years * 3 - 367
/** Number of daysView from 1/1/0001 pudding 12/31/9999  */
const val DaysTo10000 = DaysPer400Years * 25 - 366

const val ZeroMillis: Long = 0L
const val MinMillis: Long = 0L
const val OneMillis: Long = 1L
const val MaxMillis: Long = DaysTo10000 * MillisPerDay - 1

/** 기간 없음 (Duration.ZERO)  */
val NoDuration: Duration = Duration.ZERO

/** 기간 없음 Duration.ZERO)  */
val EmptyDuration: Duration = Duration.ZERO

/** 기간 없음 Duration.ZERO)  */
val ZeroDuration: Duration = Duration.ZERO

/** 양(Positive)의 최소 기간 (Duration.millis(1L))  */
val MinPositiveDuration: Duration = Duration.millis(1L)

/** 음(Negative)의 최소 기간 (TimeSpan(-1))  */
val MinNegativeDuration: Duration = Duration.millis(-1L)

/** 최소 기간에 해당하는 일자  */
val MinPeriodTime: DateTime = DateTime(MinMillis)

/** 최대 기간에 해당하는 일자  */
val MaxPeriodTime: DateTime = DateTime(MaxMillis)

/** 최소 기간 (0입니다)  */
val MinPeriodDuration: Long = ZeroMillis

/** 최대 기간 MaxMillis - MinMillis  */
val MaxPeriodDuration: Long = MaxMillis

/** 최소 기간 (0입니다. Duration.ZERO)  */
val MinDuration: Duration = Duration.millis(MinPeriodDuration)

/** 최대 기간 MaxPeriodDuration - MinPeriodDuration  */
val MaxDuration: Duration = Duration.millis(MaxPeriodDuration)

val DefaultStartOffset: Duration = EmptyDuration

val DefaultEndOffset: Duration = MinNegativeDuration

val DefaultTimeCalendar: TimeCalendar = TimeCalendar.DEFAULT

val EmptyOffsetTimeCalendar: TimeCalendar = TimeCalendar.EMPTY_OFFSET


//val dateTimeComparator: Comparator<DateTime> = Comparator { dt1, dt2 -> dt1.compareTo(dt2) }
//
//val dateTimeComparatorDesc: Comparator<DateTime> = Comparator { dt1, dt2 -> -dt1.compareTo(dt2) }
//
//val timePeriodStartComparator: Comparator<ITimePeriod> = Comparator { dt1, dt2 -> dt1.start.compareTo(dt2.start) }
//
//val timePeriodStartComparatorDesc: Comparator<ITimePeriod> = Comparator { dt1, dt2 -> -dt1.start.compareTo(dt2.start) }
//
//val timePeriodEndComparator: Comparator<ITimePeriod> = Comparator { dt1, dt2 -> dt1.end.compareTo(dt2.end) }
//
//val timePeriodEndComparatorDesc: Comparator<ITimePeriod> = Comparator { dt1, dt2 -> -dt1.end.compareTo(dt2.end) }
//
//val timePeriodDurationComparator: Comparator<ITimePeriod> = Comparator { dt1, dt2 -> dt1.duration.compareTo(dt2.duration) }
//
//val timePeriodDurationComparatorDesc: Comparator<ITimePeriod> = Comparator { dt1, dt2 -> -dt1.duration.compareTo(dt2.duration) }