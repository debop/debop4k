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

package debop4k.science.gis.coords;///*
// * Copyright 2015-2020 KESTI s.r.o.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.science.gis.coords;
//
//import debop4k.core.AbstractValueObject;
//import debop4k.core.utils.Hashx;
//import debop4k.core.utils.Stringx;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//
//import static debop4k.core.Guard.shouldBe;
//import static debop4k.core.Guard.shouldBeBetween;
//
///**
// * UTM Zone 을 표현합니다.
// * <p>
// * 참고: http://wiki.kesti.co.kr/pages/viewpage.action?pageId=3703027
// * 참고: https://ko.wikipedia.org/wiki/UTM_%EC%A2%8C%ED%91%9C%EA%B3%84
// *
// * @author sunghyouk.bae @gmail.com
// * @see org.jscience.geography.coordinates.UTM
// * @see ucar.nc2.dataset.transform.UTM
// */
//@Getter
//@Setter
//public class UtmZone extends AbstractValueObject implements Comparable<UtmZone> {
//
//  /**
//   * UTM Zone의 경도 크기 (6 degree)
//   */
//  public static int UTM_LONGITUDE_SIZE = 6;
//  /**
//   * UTM Zone의 위도 크기 (8 degree)
//   */
//  public static int UTM_LATITUDE_SIZE = 8;
//
//  /**
//   * UTM Zone 의 최소 Longitude Zone 값 ( 1 )
//   */
//  public static int UTM_LONGITUDE_MIN = 1;
//  /**
//   * UTM Zone 의 최대 Longitude Zone 값 ( 60 )
//   */
//  public static int UTM_LONGITUDE_MAX = 60;
//
//  public static UtmZone of(@NonNull String utmZoneStr) {
//    if (Stringx.trim(utmZoneStr).length() != 3) {
//      throw new RuntimeException("UtmZone 형식이 아닙니다. utmZoneStr=" + utmZoneStr);
//    }
//
//    int longitudeZone = Integer.parseInt(utmZoneStr.substring(0, 2));
//    char latitudeZone = utmZoneStr.substring(2, 3).toUpperCase().charAt(0);
//
//    shouldBeBetween(longitudeZone, UTM_LONGITUDE_MIN, UTM_LONGITUDE_MAX, "longitude");
//    shouldBe(UtmZoneEx.isUtmLatitude(latitudeZone), "latitudeZone");
//
//    return of(longitudeZone, latitudeZone);
//  }
//
//  /**
//   * create UtmZone instance.
//   *
//   * @param longitudeZone the longitude zone
//   * @param latitudeZone  the latitude zone
//   * @return the utm zone
//   */
//  public static UtmZone of(int longitudeZone, char latitudeZone) {
//    shouldBeBetween(longitudeZone, UTM_LONGITUDE_MIN, UTM_LONGITUDE_MAX, "longitude");
//    shouldBe(UtmZoneEx.isUtmLatitude(latitudeZone), "latitudeZone");
//
//    return new UtmZone(longitudeZone, latitudeZone);
//  }
//
//  /**
//   * Instantiates a new UtmZone.
//   */
//  public UtmZone() {
//  }
//
//  /**
//   * Instantiates a new UtmZone.
//   *
//   * @param longitudeZone the longitude zone
//   * @param latitudeZone  the latitude zone
//   */
//  public UtmZone(int longitudeZone, char latitudeZone) {
//    shouldBeBetween(longitudeZone, UTM_LONGITUDE_MIN, UTM_LONGITUDE_MAX, "longitudeZone");
//    shouldBe(UtmZoneEx.isUtmLatitude(latitudeZone), "latitudeZone");
//
//    this.longitudeZone = longitudeZone;
//    this.latitudeZone = Character.toUpperCase(latitudeZone);
//  }
//
//  /**
//   * 경도 기준의 Zone (6도 간격) (한국은 51, 52 에 걸쳐 있다)
//   * <p>
//   * UTM 51: 120~126
//   * UTM 52: 126~132
//   * UTM 53: 132~138
//   */
//  private int longitudeZone;
//
//  /**
//   * 위도 기준의 Zone (8도 간격) (한국은 S, T 에 걸쳐 있다)
//   * R: 24~32
//   * S: 32~40
//   * T: 40~48
//   */
//  private char latitudeZone;
//
//  /**
//   * 위경도 값 비교 시, 위도, 경도 순으로 값을 비교합니다.
//   *
//   * @param o 비교 대상 GeoLocation 인스턴스
//   * @return 비교 값 (양수면 현재 인스턴스가 크고, 음수면 대상 인스턴스가 크다. 0 이면 두 값은 같다)
//   */
//  @Override
//  public int compareTo(UtmZone o) {
//    if (o == null)
//      return 1;
//
//    int diff = latitudeZone - o.latitudeZone; // Integer.compare(latitudeZone, o.latitudeZone);
//    if (diff == 0) {
//      diff = longitudeZone - o.longitudeZone; //Integer.compare(longitudeZone, o.longitudeZone);
//    }
//    return diff;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(longitudeZone, latitudeZone);
//  }
//
//  @Override
//  public String toString() {
//    return String.valueOf(longitudeZone) + latitudeZone;
//  }
//
//  private static final long serialVersionUID = -7597129566597877615L;
//
//}
