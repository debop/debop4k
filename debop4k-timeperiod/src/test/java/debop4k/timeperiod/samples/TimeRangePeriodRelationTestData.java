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
import lombok.val;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;
import java.util.List;

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

  public List<ITimePeriod> getAllPeriods() {return this.allPeriods;}

  public ITimeRange getReference() {return this.reference;}

  public ITimeRange getBefore() {return this.before;}

  public ITimeRange getStartTouching() {return this.startTouching;}

  public ITimeRange getStartInside() {return this.startInside;}

  public ITimeRange getInsideStartTouching() {return this.insideStartTouching;}

  public ITimeRange getEnclosingStartTouching() {return this.enclosingStartTouching;}

  public ITimeRange getInside() {return this.inside;}

  public ITimeRange getEnclosingEndTouching() {return this.enclosingEndTouching;}

  public ITimeRange getExactMatch() {return this.exactMatch;}

  public ITimeRange getEnclosing() {return this.enclosing;}

  public ITimeRange getInsideEndTouching() {return this.insideEndTouching;}

  public ITimeRange getEndInside() {return this.endInside;}

  public ITimeRange getEndTouching() {return this.endTouching;}

  public ITimeRange getAfter() {return this.after;}

  public void setAllPeriods(List<ITimePeriod> allPeriods) {this.allPeriods = allPeriods; }

  public void setReference(ITimeRange reference) {this.reference = reference; }

  public void setBefore(ITimeRange before) {this.before = before; }

  public void setStartTouching(ITimeRange startTouching) {this.startTouching = startTouching; }

  public void setStartInside(ITimeRange startInside) {this.startInside = startInside; }

  public void setInsideStartTouching(ITimeRange insideStartTouching) {this.insideStartTouching = insideStartTouching; }

  public void setEnclosingStartTouching(ITimeRange enclosingStartTouching) {this.enclosingStartTouching = enclosingStartTouching; }

  public void setInside(ITimeRange inside) {this.inside = inside; }

  public void setEnclosingEndTouching(ITimeRange enclosingEndTouching) {this.enclosingEndTouching = enclosingEndTouching; }

  public void setExactMatch(ITimeRange exactMatch) {this.exactMatch = exactMatch; }

  public void setEnclosing(ITimeRange enclosing) {this.enclosing = enclosing; }

  public void setInsideEndTouching(ITimeRange insideEndTouching) {this.insideEndTouching = insideEndTouching; }

  public void setEndInside(ITimeRange endInside) {this.endInside = endInside; }

  public void setEndTouching(ITimeRange endTouching) {this.endTouching = endTouching; }

  public void setAfter(ITimeRange after) {this.after = after; }
}

