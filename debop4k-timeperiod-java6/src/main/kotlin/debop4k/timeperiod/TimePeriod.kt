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

package debop4k.timeperiod

import debop4k.timeperiod.models.PeriodRelation
import org.joda.time.DateTime
import org.joda.time.Duration


/**
 * Time Period
 */
class TimePeriod(override val start: DateTime,
                 override val end: DateTime,
                 override val readOnly: Boolean = false) : ITimePeriod {

  companion object {

    fun of(moment: DateTime, readOnly: Boolean = false): TimePeriod
        = TimePeriod(moment, moment, readOnly)

    fun of(start: DateTime, duration: Duration, readOnly: Boolean = false): TimePeriod
        = TimePeriod(start, start.plus(duration), readOnly)
  }

  override fun hasStart(): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hasEnd(): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hasPeriod(): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun isMoment(): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun isAnyTime(): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setup(newStart: DateTime, newEnd: DateTime) {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun copy(): ITimePeriod {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun copy(offset: Duration): ITimePeriod {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun move() {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun move(offset: Duration) {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun isSamePeriod(other: ITimePeriod): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hasInside(moment: DateTime): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hasInside(other: ITimePeriod): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun intersectsWith(other: ITimePeriod): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun overlapsWith(other: ITimePeriod): Boolean {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun reset() {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun relation(other: ITimePeriod): PeriodRelation {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun intersection(other: ITimePeriod): ITimePeriod {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun union(other: ITimePeriod): ITimePeriod {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun compareTo(other: ITimePeriod): Int
      = start.compareTo(other.start)

  operator fun rangeTo(endIncluded: ITimePeriod): ClosedRange<ITimePeriod> = TODO()


}
