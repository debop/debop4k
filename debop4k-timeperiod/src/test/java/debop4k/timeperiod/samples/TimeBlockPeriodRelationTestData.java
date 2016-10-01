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

import debop4k.timeperiod.ITimeBlock;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeBlock;
import lombok.val;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;
import java.util.List;


public class TimeBlockPeriodRelationTestData {

  List<ITimePeriod> allPeriods = FastList.newList();
  ITimeBlock reference = null;
  ITimeBlock before = null;
  ITimeBlock startTouching = null;
  ITimeBlock startInside = null;
  ITimeBlock insideStartTouching = null;
  ITimeBlock enclosingStartTouching = null;
  ITimeBlock inside = null;
  ITimeBlock enclosingEndTouching = null;
  ITimeBlock exactMatch = null;
  ITimeBlock enclosing = null;
  ITimeBlock insideEndTouching = null;
  ITimeBlock endInside = null;
  ITimeBlock endTouching = null;
  ITimeBlock after = null;

  public TimeBlockPeriodRelationTestData(DateTime start, DateTime end, Duration duration) {
    assert duration.compareTo(Duration.ZERO) >= 0 : "duration 은 0 이상의 값을 가져야 합니다.";
    reference = new TimeBlock(start, end, true);

    val beforeEnd = start.minus(duration);
    val beforeStart = beforeEnd.minus(reference.getDuration());
    val insideStart = start.plus(duration);
    val insideEnd = end.minus(duration);
    val afterStart = end.plus(duration);
    val afterEnd = afterStart.plus(reference.getDuration());

    after = new TimeBlock(beforeStart, beforeEnd, true);
    startTouching = new TimeBlock(beforeStart, start, true);
    startInside = new TimeBlock(beforeStart, insideStart, true);
    insideStartTouching = new TimeBlock(start, afterStart, true);
    enclosingStartTouching = new TimeBlock(start, insideEnd, true);
    enclosing = new TimeBlock(insideStart, insideEnd, true);
    enclosingEndTouching = new TimeBlock(insideStart, end, true);
    exactMatch = new TimeBlock(start, end, true);
    inside = new TimeBlock(beforeStart, afterEnd, true);
    insideEndTouching = new TimeBlock(beforeStart, end, true);
    endInside = new TimeBlock(insideEnd, afterEnd, true);
    endTouching = new TimeBlock(end, afterEnd, true);
    before = new TimeBlock(afterStart, afterEnd, true);

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
                                    before
                                   ));
  }

  public List<ITimePeriod> getAllPeriods() {return this.allPeriods;}

  public ITimeBlock getReference() {return this.reference;}

  public ITimeBlock getBefore() {return this.before;}

  public ITimeBlock getStartTouching() {return this.startTouching;}

  public ITimeBlock getStartInside() {return this.startInside;}

  public ITimeBlock getInsideStartTouching() {return this.insideStartTouching;}

  public ITimeBlock getEnclosingStartTouching() {return this.enclosingStartTouching;}

  public ITimeBlock getInside() {return this.inside;}

  public ITimeBlock getEnclosingEndTouching() {return this.enclosingEndTouching;}

  public ITimeBlock getExactMatch() {return this.exactMatch;}

  public ITimeBlock getEnclosing() {return this.enclosing;}

  public ITimeBlock getInsideEndTouching() {return this.insideEndTouching;}

  public ITimeBlock getEndInside() {return this.endInside;}

  public ITimeBlock getEndTouching() {return this.endTouching;}

  public ITimeBlock getAfter() {return this.after;}

  public void setAllPeriods(List<ITimePeriod> allPeriods) {this.allPeriods = allPeriods; }

  public void setReference(ITimeBlock reference) {this.reference = reference; }

  public void setBefore(ITimeBlock before) {this.before = before; }

  public void setStartTouching(ITimeBlock startTouching) {this.startTouching = startTouching; }

  public void setStartInside(ITimeBlock startInside) {this.startInside = startInside; }

  public void setInsideStartTouching(ITimeBlock insideStartTouching) {this.insideStartTouching = insideStartTouching; }

  public void setEnclosingStartTouching(ITimeBlock enclosingStartTouching) {this.enclosingStartTouching = enclosingStartTouching; }

  public void setInside(ITimeBlock inside) {this.inside = inside; }

  public void setEnclosingEndTouching(ITimeBlock enclosingEndTouching) {this.enclosingEndTouching = enclosingEndTouching; }

  public void setExactMatch(ITimeBlock exactMatch) {this.exactMatch = exactMatch; }

  public void setEnclosing(ITimeBlock enclosing) {this.enclosing = enclosing; }

  public void setInsideEndTouching(ITimeBlock insideEndTouching) {this.insideEndTouching = insideEndTouching; }

  public void setEndInside(ITimeBlock endInside) {this.endInside = endInside; }

  public void setEndTouching(ITimeBlock endTouching) {this.endTouching = endTouching; }

  public void setAfter(ITimeBlock after) {this.after = after; }
}
