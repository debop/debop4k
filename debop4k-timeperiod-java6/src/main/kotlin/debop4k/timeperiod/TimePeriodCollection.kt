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
import debop4k.timeperiod.utils.hasInsideWith
import debop4k.timeperiod.utils.intersectWith
import org.eclipse.collections.api.list.MutableList
import org.joda.time.DateTime

/**
 * @author sunghyouk.bae@gmail.com
 */
open class TimePeriodCollection : TimePeriodContainer(), ITimePeriodCollection {

  override fun hasInsidePeriods(that: ITimePeriod): Boolean {
    return periods.anySatisfy { it.hasInsideWith(that) }
  }

  override fun hasOverlapPeriods(that: ITimePeriod): Boolean {
    return periods.anySatisfy { it.overlapsWith(that) }
  }

  override fun hasIntersectionPeriods(moment: DateTime): Boolean {
    return periods.anySatisfy { it.hasInsideWith(moment) }
  }

  override fun hasIntersectionPeriods(target: ITimePeriod): Boolean {
    return periods.anySatisfy { it.intersectWith(target) }
  }

  override fun insidePeriods(target: ITimePeriod): MutableList<ITimePeriod> {
    return periods.select { it.hasInsideWith(target) }
  }

  override fun overlapPeriods(target: ITimePeriod): MutableList<ITimePeriod> {
    return periods.select { it.overlapsWith(target) }
  }

  override fun intersectionPeriods(moment: DateTime): MutableList<ITimePeriod> {
    return periods.select { it.hasInsideWith(moment) }
  }

  override fun intersectionPeriods(target: ITimePeriod): MutableList<ITimePeriod> {
    return periods.select { it.intersectWith(target) }
  }

  override fun relationPeriods(target: ITimePeriod, vararg relations: PeriodRelation): MutableList<ITimePeriod> {
    return periods.select { p ->
      relations.contains(p.relation(target))
    }
  }

  companion object {
    @JvmStatic fun of(c: Collection<ITimePeriod>): TimePeriodCollection {
      val collection = TimePeriodCollection()
      collection.addAll(c)
      return collection
    }

    @JvmStatic fun of(vararg elements: ITimePeriod): TimePeriodCollection {
      val collection = TimePeriodCollection()
      collection.addAll(*elements)
      return collection
    }
  }
}