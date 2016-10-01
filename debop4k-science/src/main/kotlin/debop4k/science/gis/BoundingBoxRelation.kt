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

package debop4k.science.gis

/**
 * 두 개의 BoundingBox의 영역의 관계 종류
 *
 * @author sunghyouk.bae@gmail.com
 */
enum class BoundingBoxRelation {

  /**
   * 관계 없음
   */
  None,

  /**
   * 한 영역이 다른 영역을 포함함
   */
  Include,

  /**
   * 두 영역이 겹침
   */
  Intersection,


  /**
   * 두 영역이 정확히 같음
   */
  ExactMatch,


  /**
   * 한 영역이 다른 영역에 포함됨
   */
  Contain

}