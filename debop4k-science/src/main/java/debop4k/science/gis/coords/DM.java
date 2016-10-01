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
// * 도 (Degree) 와 분(Decimal Minutes) 으로 좌표를 표현하는 방식을 표현하는 클래스입니다.
// *
// * @author sunghyouk.bae@gmail.com
// * @see DMS
// */
//@Getter
//public class DM extends AbstractValueObject implements Comparable<DM> {
//
//  /**
//   * 복제용 Constructor
//   *
//   * @param src 원본 DM 인스턴스
//   * @return 복제된 DM 인스턴스
//   */
//  public static DM of(@NonNull DM src) {
//    return of(src.d, src.m);
//  }
//
//  /**
//   * Static Constructor
//   *
//   * @param degree 도 (decimal degree)
//   * @return {@link DM} 인스턴스
//   */
//  public static DM of(double degree) {
//    int d = (int) degree;
//    double m = (degree - d) * 60.0D;
//    return of(d, m);
//  }
//
//  /**
//   * Static Constructor
//   *
//   * @param d 도 (degree)
//   * @param m 분 (decimal minute)
//   * @return {@link DM} 인스턴스
//   */
//  public static DM of(int d, double m) {
//    return new DM(d, m);
//  }
//
//  /**
//   * 도 (degree)
//   */
//  private final int d;
//  /**
//   * 분 (decimal minute)
//   */
//  private final double m;
//
//  /**
//   * 기본 생성자
//   */
//  public DM() { this(0, 0D);}
//  /**
//   * 생성자
//   *
//   * @param d 도 (degree)
//   * @param m 분 (decimal minute)
//   */
//  public DM(int d, double m) {
//    this.d = d;
//    this.m = m;
//  }
//
//  /**
//   * 도/분/초 값을 각도(degree) 값으로 변환합니다.
//   *
//   * @return 각도 값
//   */
//  public double toDegree() {
//    return d + m / 60.0D;
//  }
//
//  /**
//   * 도/분 단위로 된 문자열을 파싱하여 double 수형의 경위도 값으로 변환한다.
//   *
//   * @param dmStr 도/분을 나타내는 문자열 (예: 15 4.53)
//   * @return degree 값
//   */
//  public static DM parse(String dmStr) {
//    List<String> items = Stringx.splits(dmStr, ' ');
//
//    int d = 0;
//    double m = 0;
//
//    if (items.size() > 0) d = ConvertEx.asInt(items.get(0));
//    if (items.size() > 1) m = ConvertEx.asDouble(items.get(1));
//
//    return DM.of(d, m);
//  }
//
//  @Override
//  public int compareTo(DM o) {
//    if (o == null)
//      return 1;
//
//    int diff = d - o.d; //  Integer.compare(d, o.d);
//    if (diff == 0) {
//      diff = Double.compare(m, o.m);
//    }
//    return diff;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(d, m);
//  }
//  @Override
//  public String toString() {
//    return String.format("DM(%d %.4f)'", d, m);
//  }
//
//  private static final long serialVersionUID = -5807612363038778955L;
//
//}
