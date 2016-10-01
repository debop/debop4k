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

package debop4k.science.gis.projections

import com.jhlabs.map.proj.ProjectionFactory

/**
 * GIS의 다양한 좌표계에 대한 변환을 제공합니다.
 * proj4j (https://github.com/Proj4J/proj4j) 라이브러리를 사용합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
object Projections {

  private val SPEC_UTM_WGS84 = arrayOf("+proj=utm", "+ellps=WGS84", "+datum=WGS84", "+units=m", "+no_defs")

  private val proj_Utm2Wgs84 = ProjectionFactory.fromPROJ4Specification(SPEC_UTM_WGS84)

}