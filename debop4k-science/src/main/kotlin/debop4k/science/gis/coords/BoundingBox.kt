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

package debop4k.science.gis.coords

import debop4k.core.AbstractValueObject
import debop4k.core.utils.hashOf
import debop4k.core.utils.max
import debop4k.core.utils.min

/**
 * 지구를 위경도 좌표를 이용하여, 사각형 격자로 영역을 표현하는 클래스입니다.
 * @author sunghyouk.bae@gmail.com
 *
 * @param left   최소 경도
 * @param top    최대 위도
 * @param right  최대 경도
 * @param bottom 최소 위도
 */
class BoundingBox
@JvmOverloads constructor(private val _left: Double = 0.0,
                          private val _top: Double = 0.0,
                          private val _right: Double = 0.0,
                          private val _bottom: Double = 0.0) : AbstractValueObject(), Comparable<BoundingBox> {

  constructor(topLeft: GeoLocation, bottomRight: GeoLocation)
  : this(topLeft.longitude, topLeft.latitude, bottomRight.longitude, bottomRight.latitude)

  val left: Double
  val top: Double
  val right: Double
  val bottom: Double

  init {
    this.left = _left min _right
    this.top = _top max _bottom
    this.right = _left max _right
    this.bottom = _top min _bottom
  }

  val x1: Double get() = left
  val y1: Double get() = top
  val x2: Double get() = right
  val y2: Double get() = bottom

  // NOTE: lazy 를 사용하면 MongoDB 에서 MongoDB Converter에서 예외가 발생합니다!!!
  val minX: Double get() = x1 min x2
  val maxX: Double get() = x1 max x2
  val minY: Double get() = y1 min y2
  val maxY: Double get() = y1 max y2

  val width: Double get() = Math.abs(right - left)
  val height: Double get() = Math.abs(top - bottom)

  val meanX: Double get() = (left + right) / 2.0
  val meanY: Double get() = (top + bottom) / 2.0

  val topLeft: GeoLocation get() = GeoLocation(top, left)
  val bottomRight: GeoLocation get() = GeoLocation(bottom, right)

  fun contains(loc: GeoLocation): Boolean {
    return contains(loc.latitude, loc.longitude)
  }

  fun contains(lat: Double, lon: Double): Boolean {
    return minX <= lon && lon <= maxX &&
           minY <= lat && lat <= maxY
  }

  @JvmOverloads
  fun copy(left: Double = this.left,
           top: Double = this.top,
           right: Double = this.right,
           bottom: Double = this.bottom): BoundingBox {
    return BoundingBox(left, top, right, bottom)
  }

  override fun compareTo(other: BoundingBox): Int {
    var diff = left.compareTo(other.left)
    if (diff == 0)
      diff = top.compareTo(other.top)
    if (diff == 0)
      diff = right.compareTo(other.right)
    if (diff == 0)
      diff = bottom.compareTo(other.bottom)

    return diff
  }

  override fun hashCode(): Int {
    return hashOf(left, top, right, bottom)
  }

  override fun toString(): String {
    return "BoundingBox(left=$left, top=$top, right=$right, bottom=$bottom)"
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(left: Double = 0.0,
           top: Double = 0.0,
           right: Double = 0.0,
           bottom: Double = 0.0): BoundingBox {
      return BoundingBox(left min right,
                         top max bottom,
                         left max right,
                         top min bottom)
    }

    @JvmStatic
    fun of(leftTop: GeoLocation, rightBottom: GeoLocation): BoundingBox {
      return BoundingBox(leftTop, rightBottom)
    }

    @JvmStatic
    fun of(src: BoundingBox): BoundingBox = src.copy()
  }

}