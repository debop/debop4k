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

@file:JvmName("UtmZonex")

package debop4k.science.gis.coords

import org.eclipse.collections.impl.factory.Lists
import org.slf4j.LoggerFactory
import java.util.concurrent.*

private val log = LoggerFactory.getLogger("UtmZonex")

/** UTM Zone의 경도 크기 (6 degree) */
const val UTM_LONGITUDE_SIZE = 6

/** UTM Zone의 위도 크기 (8 degree) */
const val UTM_LATITUDE_SIZE = 8

/** UTM Zone 의 최소 Longitude Zone 값 ( 1 ) */
const val UTM_LONGITUDE_MIN = 1

/** UTM Zone 의 최대 Longitude Zone 값 ( 60 ) */
const val UTM_LONGITUDE_MAX = 60

/**
 * UTM Zone 의 위도를 구분한 '구역 코드 - 시작 위도'
 */
val UtmLatitudes: Map<Char, Double> by lazy {
  linkedMapOf('C' to -80.0,
              'D' to -72.0,
              'E' to -64.0,
              'F' to -56.0,
              'G' to -48.0,
              'H' to -40.0,
              'J' to -32.0,
              'K' to -24.0,
              'L' to -16.0,
              'M' to -8.0,
              'N' to 0.0,
              'P' to 8.0,
              'Q' to 16.0,
              'R' to 24.0,
              'S' to 32.0,
              'T' to 40.0,
              'U' to 48.0,
              'V' to 56.0,
              'W' to 64.0,
              'X' to 72.0)
}

/**
 * UTM Zone 의 영역과 영역을 위경도 좌표로 표현한 BondingBox 의 맵
 */
val UtmZoneBoundingBoxes: Map<UtmZone, BoundingBox> by lazy {
  val map = ConcurrentSkipListMap<UtmZone, BoundingBox>()

  for (lon in UTM_LONGITUDE_MIN..UTM_LONGITUDE_MAX) {
    for (lat in UtmLatitudes.keys) {
      val zone = UtmZone(lon, lat)

      val longitude = getLongitudeByUtm(lon)
      val latitude = getLatitudeByUtm(lat)

      val bbox = BoundingBox.of(longitude,
                                latitude + UTM_LATITUDE_SIZE,
                                longitude + UTM_LONGITUDE_SIZE,
                                latitude)

      map.put(zone, bbox)
    }
  }
  map
}

/** UTM 문자열 (예: 51N) 을 파싱하여, UtmZone 인스턴스를 만듭니다 */
fun utmZoneOf(utmZoneStr: String?): UtmZone {
  if (utmZoneStr.isNullOrBlank() || utmZoneStr?.length != 3) {
    throw IllegalArgumentException("UtmZone 형식이 아닙니다. utmZoneStr=$utmZoneStr")
  }
  val longitudeZone = utmZoneStr?.substring(0, 2)?.toInt()
  val latitudeZone = utmZoneStr?.substring(2, 3)?.toUpperCase()?.first()

  return UtmZone(longitudeZone!!, latitudeZone!!)
}

fun utmZoneOf(longitudeZone: Int, latitudeZone: Char): UtmZone {
  return UtmZone(longitudeZone, latitudeZone.toUpperCase())
}

/** 해당 Location 이 속한 [UtmZone] 인스턴스를 만듭니다 */
@JvmOverloads
fun utmZoneOf(longitude: Double = 0.0, latitude: Double = 0.0): UtmZone {
  return UtmZone(longitude.toUtmLongitude(), latitude.toUtmLatitude())
}

/** 해당 [GeoLocation] 이 속한 [UtmZone] 인스턴스를 만듭니다 */
fun utmZoneOf(location: GeoLocation): UtmZone {
  return utmZoneOf(location.longitude, location.latitude)
}

val Char.isUtmLatitude: Boolean get() = UtmLatitudes.keys.contains(this.toUpperCase())

fun Int.toLongitudeByUtm(): Double {
  return (this - 31) * UTM_LONGITUDE_SIZE.toDouble()
}

@Deprecated("use toLongitudeByUtm()", ReplaceWith("utmLongitude.toLongitudeByUtm()"))
fun getLongitudeByUtm(utmLongitude: Int): Double {
  return utmLongitude.toLongitudeByUtm()
}

fun Char.toLatitudeByUtm(): Double {
  return UtmLatitudes[this.toUpperCase()]!!
}

@Deprecated("use toLatitudeByUtm", ReplaceWith("utmLatitude.toLatitudeByUtm()"))
fun getLatitudeByUtm(utmLatitude: Char): Double {
  return utmLatitude.toLatitudeByUtm()
}

/**
 * 경도가 속한 UTM Longitude Zone을 구한다.
 * @return UTM Longitude Zone
 */
fun Double.toUtmLongitude(): Int {
  return (this / UTM_LONGITUDE_SIZE + 31).toInt()
}

/**
 * 경도가 속한 UTM Longitude Zone을 구한다.
 * @param longitude 경도
 * @return UTM Longitude Zone
 */
@Deprecated("use toUtmLongitude", ReplaceWith("longitude.toUtmLongitude()"))
fun getUtmLongitude(longitude: Double): Int {
  return longitude.toUtmLongitude()
}

/**
 * 지정한 위도가 속한 UTM Latitude Zone 을 구합니다.
 * @return UTM Latitude Zone (예 : 'S', 'T')
 */
fun Double.toUtmLatitude(): Char {
  val latitudes = Lists.mutable.withAll<Char>(UtmLatitudes.keys)
  latitudes.sortThis { c1, c2 -> -c1!!.compareTo(c2!!) }

  for (c in latitudes) {
    val utm = UtmLatitudes[c] ?: Double.NaN
    if (utm != Double.NaN && utm <= this) {
      return c
    }
  }

  throw RuntimeException("해당 UTM Zone Latitude 를 찾지 못했습니다. latitude=$this")
}

/**
 * 지정한 위도가 속한 UTM Latitude Zone 을 구합니다.
 * @return UTM Latitude Zone (예 : 'S', 'T')
 */
@Deprecated("use toUtmLatitude", ReplaceWith("latitude.toUtmLatitude()"))
fun getUtmLatitude(latitude: Double): Char {
  return latitude.toUtmLatitude()
}


/**
 * UTM Zone에 해당하는 영역을 위경도 좌표로 표현한 Bounding Box 을 반환합니다.
 * @param utm UtmZone
 * @return UTM Zone 영역을 위경도 좌표로 표현한 Bounding Box
 */
fun UtmZone.boundingBox(): BoundingBox {
  return UtmZoneBoundingBoxes[this] ?: throw RuntimeException("Not found bounding box. utm=$this")
}

/**
 * UTM Zone에 해당하는 영역을 위경도 좌표로 표현한 Bounding Box 을 반환합니다.
 * @param utm UtmZone
 * @return UTM Zone 영역을 위경도 좌표로 표현한 Bounding Box
 */
@Deprecated("use boundingBox", ReplaceWith("utm.boundingBox()"))
fun getBoundingBox(utm: UtmZone): BoundingBox {
  return utm.boundingBox()
}

/**
 * UTM Zone 의 특정 Cell의 Bounding Box 를 계산합니다.

 * @param utm  UTM Zone
 * @param size Cell 의 크기 (경위도의 단위)
 * @param row  Cell 의 row index (0부터 시작)
 * @param col  Cell의 column index (0부터 시작)
 * @return UtmZone의 특정 cell의 Bounding Box를 구합니다.
 */
@JvmOverloads
fun UtmZone.cellBbox(size: Double, row: Int = 0, col: Int = 0): BoundingBox {
  log.trace("utm={}, size={}, row={}, col={}", this, size, row, col)
  val utmBbox = this.boundingBox()

  val left = utmBbox.left + size * col.toDouble()
  val top = utmBbox.top - size * row.toDouble()
  val right = left + size
  val bottom = top - size

  val cellBbox = BoundingBox.of(left, top, right, bottom)

  log.trace("utm bbox={}, size={}, row={}, col={}, cell bbox={}", utmBbox, size, row, col, cellBbox)
  return cellBbox
}

/**
 * UTM Zone 의 특정 Cell의 Bounding Box 를 계산합니다.

 * @param utm  UTM Zone
 * @param size Cell 의 크기 (경위도의 단위)
 * @param row  Cell 의 row index (0부터 시작)
 * @param col  Cell의 column index (0부터 시작)
 * @return UtmZone의 특정 cell의 Bounding Box를 구합니다.
 */
@JvmOverloads
@Deprecated("use cellBox", ReplaceWith("utm.cellBbox(size, row, col)"))
fun getCellBoundingBox(utm: UtmZone, size: Double, row: Int = 0, col: Int = 0): BoundingBox {
  return utm.cellBbox(size, row, col)
}