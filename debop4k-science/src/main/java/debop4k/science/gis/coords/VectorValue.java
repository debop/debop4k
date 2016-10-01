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
//import lombok.Setter;
//
//import java.awt.geom.Point2D;
//
///**
// * Vector 를 표현합니다. ( 각도와 길이 )
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Setter
//public class VectorValue extends AbstractValueObject {
//
//  public static VectorValue of(double degree, double length) {
//    return new VectorValue(degree, length);
//  }
//
//  public static VectorValue of(Point2D.Double start, Point2D.Double end) {
//    return GeometryEx.getVector(start, end);
//  }
//
//  // 각도 ( 단위 : degree )
//  public double degree;
//  // 길이
//  public double length;
//
//  public VectorValue() {}
//
//  public VectorValue(double degree, double length) {
//    this.degree = degree;
//    this.length = length;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(degree, length);
//  }
//
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("degree", degree)
//                .add("length", length);
//  }
//
//  private static final long serialVersionUID = 8008159084093619242L;
//}
