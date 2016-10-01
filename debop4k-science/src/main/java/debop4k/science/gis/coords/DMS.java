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
//import debop4k.core.utils.Convertx;
//import debop4k.core.utils.Hashx;
//import debop4k.core.utils.Stringx;
//import lombok.Getter;
//import lombok.NonNull;
//
//import java.util.List;
//
///**
// * 경위도를 도/분/초 형식으로 나타내는 클래스
// *
// * @author sunghyouk.bae@gmail.com
// * @see DM
// * @since 2015. 12. 12.
// */
//@Getter
//public class DMS extends AbstractValueObject implements Comparable<DMS> {
//
//  /**
//   * 복제용 Constructor
//   *
//   * @param src 원본 DMS 인스턴스
//   * @return 복제된 DMS 인스턴스
//   */
//  public static DMS of(@NonNull DMS src) {
//    return of(src.d, src.m, src.s);
//  }
//
//  /**
//   * 각도를 도/분/초 로 나타내는 DMS로 빌드합니다.
//   *
//   * @param degree 각도 (degree)
//   * @return DMS 인스턴스
//   */
//  public static DMS of(double degree) {
//    int d = (int) degree;
//    int m = (int) ((degree - d) * 60);
//    double s = ((degree - d) * 60 - m) * 60;
//
//    return of(d, m, s);
//  }
//  /**
//   * 도/분 값으로 DMS를 생성합니다.
//   *
//   * @param d 도 (degree)
//   * @param m 분 (minute)
//   * @return DMS 인스턴스
//   */
//  public static DMS of(int d, double m) {
//    int mm = (int) m;
//    double s = (m - mm) * 60;
//    return new DMS(d, mm, s);
//  }
//  /**
//   * 도/분 값으로 DMS를 생성합니다.
//   *
//   * @param d 도 (degree)
//   * @param m 분 (minute = 1/60 degree)
//   * @param s 초 (seconds = 1/60 minute = 1/3600 degree)
//   * @return DMS 인스턴스
//   */
//  public static DMS of(int d, int m, double s) {
//    return new DMS(d, m, s);
//  }
//
//  /**
//   * 기본 생성자
//   */
//  public DMS() {
//    this(0, 0, 0D);
//  }
//  /**
//   * 생성자
//   *
//   * @param d 도 (degree)
//   * @param m 분 (minute = 1/60 degree)
//   * @param s 초 (seconds = 1/60 minute = 1/3600 degree)
//   */
//  public DMS(int d, int m, double s) {
//    this.d = d;
//    this.m = m;
//    this.s = s;
//  }
//
//  /**
//   * Degree
//   */
//  private final int d;
//  /**
//   * Minute ( 1 minute = 1/60 degree)
//   */
//  private final int m;
//  /**
//   * Second ( 1 second = 1/60 minute = 1/3600 degree )
//   */
//  private final double s;
//
//  /**
//   * 도/분/초 값을 각도 값으로 변환합니다.
//   *
//   * @return 각도 값
//   */
//  public double toDegree() {
//    return d + m / 60.0 + s / 3600.0;
//  }
//
//  @Override
//  public int compareTo(DMS o) {
//    if (o == null)
//      return 1;
//
//    int diff = d - o.d; // Integer.compare(d, o.d);
//    if (diff == 0) {
//      diff = m - o.m; // Integer.compare(m, o.m);
//    }
//    if (diff == 0) {
//      diff = Double.compare(s, o.s);
//    }
//    return diff;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(d, m, s);
//  }
//
//  @Override
//  public String toString() {
//    return String.format("DMS(%d %d %.4f)", d, m, s);
//  }
//
//  /**
//   * 도/분/초 단위로 된 문자열을 파싱하여 double 수형의 경위도 값으로 변환한다.
//   *
//   * @param dmsStr 도/분/초를 나타내는 문자열 (예: 15 4 93.4)
//   * @return degree 값
//   */
//  public static DMS parse(String dmsStr) {
//    List<String> items = Stringx.splits(dmsStr, ' ');
//
//    int d = 0;
//    int m = 0;
//    double s = 0D;
//
//    if (items.size() > 0) d = ConvertEx.asInt(items.get(0));
//    if (items.size() > 1) m = ConvertEx.asInt(items.get(1));
//    if (items.size() > 2) s = ConvertEx.asDouble(items.get(2));
//
//    return DMS.of(d, m, s);
//  }
//  private static final long serialVersionUID = -501448027742319584L;
//}
