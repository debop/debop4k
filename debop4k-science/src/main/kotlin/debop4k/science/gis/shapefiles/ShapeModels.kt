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

package debop4k.science.gis.shapefiles

import com.vividsolutions.jts.geom.Geometry
import debop4k.core.collections.fastListOf
import debop4k.science.gis.coords.BoundingBox
import debop4k.science.gis.coords.GeoLocation
import org.eclipse.collections.impl.list.mutable.FastList
import org.geotools.data.shapefile.shp.ShapeType

data class ShapeRecord(val number: Int,
                       val type: ShapeType,
                       val bounds: BoundingBox,
                       val geom: Geometry) {
}

data class ShapeHeader(val size: Int,
                       val version: Int,
                       val shapeType: ShapeType,
                       val bounds: BoundingBox)

data class ShapeAttribute
@JvmOverloads constructor(val fields: MutableMap<String, Any?> = mutableMapOf<String, Any?>()) {

  fun addField(name: String, value: Any?) {
    fields.put(name, value)
  }
}

data class Shape
@JvmOverloads constructor(val header: ShapeHeader? = null,
                          val records: FastList<ShapeRecord> = fastListOf<ShapeRecord>(),
                          val attributes: FastList<ShapeAttribute> = fastListOf<ShapeAttribute>()) {

  fun getRecordCount(): Int = records.size

  fun getGeometry(index: Int): Geometry = records[index].geom

  fun getLocations(index: Int): FastList<GeoLocation> {
    val geom = getGeometry(index)

    return FastList.newList<GeoLocation>().apply {
      geom.coordinates.forEach {
        add(GeoLocation(it.y, it.x))
      }
    }
  }
}