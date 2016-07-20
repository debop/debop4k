/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.science.gis.coords

import debop4k.core.max
import debop4k.core.min
import java.io.Serializable

/**
 * Created by debop
 */
data class BoundingBox(private val _left: Double,
                       private val _top: Double,
                       private val _right: Double,
                       private val _bottom: Double) : Comparable<BoundingBox>, Serializable {

  constructor(topLeft: GeoLocation, bottomRight: GeoLocation) :
  this(topLeft.lon, topLeft.lat, bottomRight.lon, bottomRight.lat)

  val left: Double
  val top: Double
  val right: Double
  val bottom: Double

  init {
    this.left = _left min _right
    this.right = _left max _right
    this.top = _top max _bottom
    this.bottom = _top min _bottom
  }

  val x1: Double get() = left
  val y1: Double get() = top
  val x2: Double get() = right
  val y2: Double get() = bottom

  val minX: Double by lazy { left min right }
  val maxX: Double by lazy { left max right }
  val minY: Double by lazy { top min bottom }
  val maxY: Double by lazy { top max bottom }

  val width: Double by lazy { Math.abs(right - left) }
  val height: Double by lazy { Math.abs(top - bottom) }

  val meanX: Double by lazy { (left + right) / 2.0 }
  val meanY: Double by lazy { (top + bottom) / 2.0 }

  val topLeft: GeoLocation get() = GeoLocation(top, left)
  val bottomRight: GeoLocation get() = GeoLocation(bottom, right)

  fun contains(loc: GeoLocation): Boolean {
    return contains(loc.lat, loc.lon)
  }

  fun contains(lat: Double, lon: Double): Boolean {
    return minX <= lon && lon <= maxX &&
        minY <= lat && lat <= maxY
  }

  override fun compareTo(other: BoundingBox): Int {
    var result = left.compareTo(other.left)
    if (result == 0)
      result = top.compareTo(other.top)
    if (result == 0)
      result = right.compareTo(other.right)
    if (result == 0)
      result = bottom.compareTo(other.bottom)

    return result
  }
}