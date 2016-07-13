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

package debop4k.timeperiod.utils

import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.api.set.MutableSet
import org.eclipse.collections.impl.factory.Maps
import org.eclipse.collections.impl.factory.Sets
import org.joda.time.DateTimeZone

/**
 * @author debop sunghyouk.bae@gmail.com
 */
object TimeZones {

  val ZoneIds: MutableSet<String> = Sets.mutable.ofAll(DateTimeZone.getAvailableIDs())!!
  val ZoneOffsets: MutableMap<String, Int> = buildZoneOffsets()
  val Offsets: MutableSet<Int> = Sets.mutable.withAll(ZoneOffsets.values)!!
  val Zones = buildZone()


  fun getTimeZoneIds(offset: Int): Set<String> {
    val results = Sets.mutable.of<String>()

    for ((key, value) in ZoneOffsets.entries) {
      if (value === offset) {
        results.add(key)
      }
    }
    return results
  }

  fun getSameOffsetTimeZoneIds(zoneId: String): Set<String> {
    val zone = DateTimeZone.forID(zoneId)
    return getTimeZoneIds(zone.getOffset(0))
  }

  private fun buildZoneOffsets(): MutableMap<String, Int> {
    val map = Maps.mutable.of<String, Int>()
    for (id in ZoneIds) {
      map.put(id, DateTimeZone.forID(id).getOffset(0))
    }
    return map
  }

  private fun buildZone(): MutableSet<DateTimeZone> {
    val sets = Sets.mutable.of<DateTimeZone>()
    for (id in DateTimeZone.getAvailableIDs()) {
      sets.add(DateTimeZone.forID(id))
    }
    return sets
  }
}