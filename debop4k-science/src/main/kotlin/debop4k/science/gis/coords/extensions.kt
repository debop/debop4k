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

import debop4k.core.asDouble
import org.slf4j.LoggerFactory
import java.awt.Point
import java.awt.Polygon
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.lang.Math.*

private val log = LoggerFactory.getLogger("extensions")

const val Px2M = 0.0002645833333333

const val MIN_X = -20037508.3427892
const val MAX_X = 20037508.3427892
const val MIN_Y = -44927335.4270971
const val MAX_Y = 44927335.4270966

const val MIN_Y_ENC = -147730762.669922
const val MAX_Y_ENC = 147730758.194568

val TOP_LEFT = Point2D.Double(MIN_X, 147730758.194568) // lon : -180, lat :  89.99999999
val BOTTOM_RIGHT = Point2D.Double(MAX_X, -147730762.669922) // lon :  180, lat : -89.99999999

val WorldBBox = BoundingBox(MIN_X, MIN_Y, MAX_X, MAX_Y)
val EncBBox = BoundingBox(MIN_X, MIN_Y_ENC, MAX_X, MAX_Y_ENC) // lon: -180 ~ 180, lat = 90 ~ -90

const val PI_M2 = PI * 2.0
const val PI_D2 = PI / 2.0


fun Double.toDMS(): DMS = DMS.of(this)

fun Double.isValidLatitude(): Boolean = this >= -90.0 && this <= 90.0
fun Double.isValidLongitude(): Boolean = this >= -180.0 && this <= 180.0


/**
 * 사각형 영역 (Left-Top, Bottom-Right) 의 좌표를 가진 문자열을 파싱해서 최대 영역 안에 존재하는 영역인지 파악합니다.
 *
 * @param bboxStr  x1,y1,x2,y2 죄표를 표현한 문자열
 * @param limitBox 좌표계의 최대 / 최소값
 * @return {@link BoundingBox} 인스턴스, 파싱 실패 시에는 null 값을 반환합니다.
 */
@JvmOverloads
fun parseBbox(bboxStr: String?, limitBox: BoundingBox = WorldBBox): BoundingBox? {
  log.trace("bbox 문자열을 파싱합니다. bboxStr={}", bboxStr)

  if (bboxStr.isNullOrBlank())
    return null

  try {
    val array = bboxStr?.split(debop4k.core.TAB, limit = 4) ?: listOf<String>()
    assert(array.size == 4, { "잘못된 bbox 문자열입니다. bboxStr=$bboxStr" })

    val x1 = array[0].asDouble(limitBox.x1)
    val y1 = array[1].asDouble(limitBox.y1)
    val x2 = array[2].asDouble(limitBox.x2)
    val y2 = array[3].asDouble(limitBox.y2)

    return BoundingBox(x1, y1, x2, y2)
  } catch (ignored: Exception) {
    log.error("bbox 파싱에 실패했습니다. bboxStr=$bboxStr")
    log.error("예외정보:", ignored)
    return null
  }
}

/**
 * {@link Polygon} 각 꼭지점을 {@link java.awt.geom.Point2D.Double} 배열로 변환합니다.
 *
 * @return {@link java.awt.geom.Point2D.Double} 배열
 */
fun Polygon?.toPointArray(): Array<Point2D.Double> {
  if (this == null || this.npoints == 0) {
    return arrayOf<Point2D.Double>()
  }

  val count = this.npoints
  val points = Array<Point2D.Double>(count) { i ->
    Point2D.Double(this.xpoints[i].toDouble(), this.ypoints[i].toDouble())
  }
  return points
}

fun Array<Point2D.Double>?.toPolygon(): Polygon {
  if (this == null || this.size == 0)
    return Polygon()

  val polygon = Polygon()
  this.forEach { p ->
    polygon.addPoint(p.x.toInt(), p.y.toInt())
  }
  return polygon
}

fun Polygon?.area(): Double {
  return this.toPointArray().area()
}

fun Array<Point2D.Double>?.area(): Double {
  if (this == null || this.size == 0)
    return 0.0

  val n = this.size
  var area = 0.0
  for (i in 0..n - 1) {
    val j = (i + 1) % n
    area += this[i].x * this[j].y
    area -= this[j].x * this[i].y
  }
  area /= 2.0
  log.trace("calc area. area={}, points size={}", area, this.size)
  return area
}

fun Polygon?.centerOfGravity(): Point2D.Double? {
  return this.toPointArray().centerOfGravity()
}

fun Iterable<Point2D.Double>?.centerOfGravity(): Point2D.Double? {
  return this?.toList()?.toTypedArray().centerOfGravity() ?: null
}

fun Array<Point2D.Double>?.centerOfGravity(): Point2D.Double? {
  if (this == null || this.size == 0)
    return Point2D.Double()

  var cx = 0.0
  var cy = 0.0
  val area = this.area()
  val n = this.size
  var factor: Double

  for (i in 0..n - 1) {
    val j = (i + 1) % n
    factor = this[i].x * this[j].y - this[j].x * this[i].y
    cx += (this[i].x + this[j].x) * factor
    cy += (this[i].y + this[j].y) * factor
  }
  factor = 1.0 / (area * 6.0)
  cx *= factor
  cy *= factor

  log.trace("center of gravity. cx={}, cy={}", cx, cy)
  return Point2D.Double(cx, cy)
}

fun angleOf(p1: Point2D.Double, p2: Point2D.Double): Double {
  return angleOf(p1.x, p1.y, p2.x, p2.y)
}

fun angleOf(x1: Double, y1: Double, x2: Double, y2: Double): Double {
  try {
    val dx = x2 - x1
    val dy = y2 - y1

    // +X 축 방향이 0도이고, 시계반대방향으로 증가한다.
    if (dx == 0.0) {
      return 90.0
    }
    val rad: Double = atan2(dy, dx)

    // -Y 축 (South 방향) 방향이 각도 0 이고 시계 반대 방향으로 계산한다.
    //      double rad = atan2(dy, dx) + PI / 2;

    var degree = toDegrees(rad)
    if (degree < 0)
      degree += 360.0
    if (degree >= 360)
      degree -= 360.0

    return degree
  } catch (ignored: Exception) {
    log.error("각도 구하기 실패.", ignored)
    return java.lang.Double.NaN
  }
}

fun distanceOf(p1: Point2D.Double, p2: Point2D.Double): Double {
  return distanceOf(p1.x, p1.y, p2.x, p2.y)
}

fun distanceOf(x1: Double, y1: Double, x2: Double, y2: Double): Double {
  val dx = x2 - x1
  val dy = y2 - y1

  return sqrt(dx * dx + dy * dy)
}

fun vectorOf(s: Point2D.Double, e: Point2D.Double): Vector {
  return Vector(angleOf(s, e), distanceOf(s, e))
}

fun vectorEndPoint(base: Point2D.Double, degree: Double, length: Double): Point2D.Double {
  val rad = toRadians(degree)
  val x = length * cos(rad) + base.x
  val y = length * sin(rad) - base.y

  return Point2D.Double(x, y)
}

@JvmOverloads
fun rotateXYPoint(point: Point2D.Double,
                  degree: Double,
                  basePoint: Point2D.Double = Point2D.Double(0.0, 0.0)): Point2D.Double {
  val rad = toRadians(degree % 360.0)
  val cos = cos(rad)
  val sin = sin(rad)

  val dx = point.x - basePoint.x
  val dy = point.y - basePoint.y

  val x = dx * cos + dy * sin + basePoint.x
  val y = dx * sin - dy * cos + basePoint.y

  return Point2D.Double(x, y)
}

fun rotateXY(baseX: Int, baseY: Int, x: Int, y: Int, degree: Double): Point2D.Double {
  return rotateXYPoint(Point2D.Double(x.toDouble(), y.toDouble()),
                       degree,
                       Point2D.Double(baseX.toDouble(), baseY.toDouble()))
}

fun rotateXY(p: Point, degree: Double): Point2D.Double {
  return rotateXY(0, 0, p.x, p.y, degree)
}

fun rotateXY(base: Point, p: Point, degree: Double): Point2D.Double {
  return rotateXYPoint(Point2D.Double(p.x.toDouble(), p.y.toDouble()),
                       degree,
                       Point2D.Double(base.x.toDouble(), base.y.toDouble()))
}

/**
 * 사각 영역안에 Point 를 위치시킵니다.

 * @param point       대상 Point
 * @param leftTop     Left-Top Point
 * @param rightBottom Bottom-Right Point
 * @return 대상 Point 를 사각 영역안에 위치 시킨 새로운 Point
 */
fun checkPositionPoint(point: Point2D.Double,
                       leftTop: Point2D.Double,
                       rightBottom: Point2D.Double): Point2D.Double {
  return checkPositionPoint(point,
                            BoundingBox(GeoLocation(leftTop.x, leftTop.y),
                                        GeoLocation(rightBottom.x, rightBottom.y)))
}

/**
 * Point 가 BoundingBox 안에 위치하도록 값을 조정합니다.

 * @param point 조정할 Point 값
 * *
 * @param bbox  영역을 나타내는 [BoundingBox]
 * *
 * @return 새롭게 조정된 Point
 */
fun checkPositionPoint(point: Point2D.Double, bbox: BoundingBox): Point2D.Double {
  val x = min(max(point.x, bbox.minX), bbox.maxX)
  val y = min(max(point.y, bbox.minY), bbox.maxY)

  return Point2D.Double(x, y)
}

/**
 * 지정한 라인을 n 등분한 위치 좌표들을 반환한다.

 * @param line 라인 정보
 * *
 * @param n    등분할 수
 * *
 * @return 선분을 N 등분한 Point 중보
 */
fun getLineNPoints(line: Line2D.Double, n: Int): Array<Point2D.Double> {
  assert(n > 0)

  if (n < 2) {
    return arrayOf(line.p1 as Point2D.Double, line.p2 as Point2D.Double)
  }

  val start = line.p1 as Point2D.Double
  val end = line.p2 as Point2D.Double

  val vector = vectorOf(start, end)
  log.debug("vector={}", vector)

  val points = Array<Point2D.Double>(n + 1) { i -> Point2D.Double() }

  points[0] = line.p1 as Point2D.Double
  points[n] = line.p2 as Point2D.Double

  val length = vector.length
  val nlen = length / n.toDouble()
  // vector의 각도는 -Y 축이 0 도이다. 그래서 +X 축으로 환산한 경우에는 -90 을 해줘야 한다.
  val degree = vector.degree

  for (i in 1..n - 1) {
    points[i] = vectorEndPoint(start, degree, nlen * i)
  }

  return points
}

/**
 * 두 선분의 교차점을 구한다. 교차점이 없을 때에는 null 을 반환합니다.

 * @param l1 선분 1
 * *
 * @param l2 선분 2
 * *
 * @return 두 선분의 교차점.
 */
fun getIntersectPoint(l1: Line2D.Double, l2: Line2D.Double): Point2D.Double? {
  try {
    val under = (l2.y2 - l2.y1) * (l1.x2 - l1.x1) - (l2.x2 - l2.x1) * (l1.y2 - l1.y1)
    if (under == 0.0)
      return null

    var t = (l2.x2 - l2.x1) * (l1.y1 - l2.y1) - (l2.y2 - l2.y1) * (l1.x1 - l2.x1)
    var s = (l1.x2 - l1.x1) * (l1.y1 - l2.y1) - (l1.y2 - l1.y1) * (l1.x1 - l2.x1)

    if (t == 0.0 && s == 0.0)
      return null

    t /= under
    s /= under

    if (t < 0.0 || t > 1.0 || s < 0.0 || s > 1.0)
      return null

    val x = l1.x1 + t * (l1.x2 - l1.x1)
    val y = l1.y1 + t * (l1.y2 - l1.y1)

    return Point2D.Double(x, y)

  } catch (ignored: Exception) {
    log.error("두 선분 교차점 찾기 실패.", ignored)
    return null
  }

}
