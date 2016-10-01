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

import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.ITimeRange;
import debop4k.timeperiod.TimeRange;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class TimeRangePeriodRelationTestData {

  List<ITimePeriod> allPeriods = FastList.newList();
  ITimeRange reference = null;
  ITimeRange before = null;
  ITimeRange startTouching = null;
  ITimeRange startInside = null;
  ITimeRange insideStartTouching = null;
  ITimeRange enclosingStartTouching = null;
  ITimeRange inside = null;
  ITimeRange enclosingEndTouching = null;
  ITimeRange exactMatch = null;
  ITimeRange enclosing = null;
  ITimeRange insideEndTouching = null;
  ITimeRange endInside = null;
  ITimeRange endTouching = null;
  ITimeRange after = null;

  public TimeRangePeriodRelationTestData(DateTime start, DateTime end, Duration duration) {
    assert duration.compareTo(Duration.ZERO) >= 0 : "duration 은 0 이상의 값을 가져야 합니다.";
    reference = new TimeRange(start, end, true);

    val beforeEnd = start.minus(duration);
    val beforeStart = beforeEnd.minus(reference.getDuration());
    val insideStart = start.plus(duration);
    val insideEnd = end.minus(duration);
    val afterStart = end.plus(duration);
    val afterEnd = afterStart.plus(reference.getDuration());

    after = new TimeRange(beforeStart, beforeEnd, true);
    startTouching = new TimeRange(beforeStart, start, true);
    startInside = new TimeRange(beforeStart, insideStart, true);
    insideStartTouching = new TimeRange(start, afterStart, true);
    enclosingStartTouching = new TimeRange(start, insideEnd, true);
    enclosing = new TimeRange(insideStart, insideEnd, true);
    enclosingEndTouching = new TimeRange(insideStart, end, true);
    exactMatch = new TimeRange(start, end, true);
    inside = new TimeRange(beforeStart, afterEnd, true);
    insideEndTouching = new TimeRange(beforeStart, end, true);
    endInside = new TimeRange(insideEnd, afterEnd, true);
    endTouching = new TimeRange(end, afterEnd, true);
    before = new TimeRange(afterStart, afterEnd, true);

    allPeriods.addAll(Arrays.asList(reference,
                                    after,
                                    startTouching,
                                    startInside,
                                    insideStartTouching,
                                    enclosingStartTouching,
                                    enclosing,
                                    enclosingEndTouching,
                                    exactMatch,
                                    inside,
                                    insideEndTouching,
                                    endInside,
                                    endTouching,
                                    before));
  }
}

