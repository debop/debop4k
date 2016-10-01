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

package debop4k.science.gis.coords;//package debop4k.science.gis.coords;
//
//import debop4k.core.AbstractValueObject;
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//
///**
// * 위경도 정보 (Geographical Location)
// *
// * @author sunghyouk.bae@gmail.com
// * @see org.jscience.geography.coordinates.LatLong
// */
//@Getter
//@Setter
//public class GeoLocation extends AbstractValueObject implements Comparable<GeoLocation> {
//
//  /**
//   * Static 생성자
//   *
//   * @return GeoLocation 인스턴스
//   */
//  public static GeoLocation of() {
//    return new GeoLocation();
//  }
//
//  /**
//   * Copy constructor
//   *
//   * @param src 원본 GeoLocation 인스턴스
//   * @return 복사된 GeoLocation 인스턴스
//   */
//  public static GeoLocation of(@NonNull GeoLocation src) {
//    return of(src.latitude, src.longitude);
//  }
//
//  /**
//   * Static 생성자
//   *
//   * @param latitude  위도
//   * @param longitude 경도
//   * @return GeoLocation 인스턴스
//   */
//  public static GeoLocation of(double latitude, double longitude) {
//    return new GeoLocation(latitude, longitude);
//  }
//
//  /**
//   * 위도
//   */
//  private final double latitude;
//
//  /**
//   * 경도
//   */
//  private final double longitude;
//
//  /**
//   * 기본 생성자
//   */
//  public GeoLocation() { this(0, 0); }
//  /**
//   * 생성자
//   *
//   * @param latitude  위도
//   * @param longitude 경도
//   */
//  public GeoLocation(double latitude, double longitude) {
//    this.latitude = latitude;
//    this.longitude = longitude;
//  }
//
//  @Override
//  public int compareTo(GeoLocation o) {
//
//    int diff = Double.compare(latitude, o.latitude);
//
//    if (diff == 0) {
//      diff = Double.compare(longitude, o.longitude);
//    }
//    return diff;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(latitude, longitude);
//  }
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("latitude", latitude)
//                .add("longitude", longitude);
//  }
//  private static final long serialVersionUID = -7579024415351299553L;
//
//}
