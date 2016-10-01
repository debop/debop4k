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

package debop4k.data.orm.model;

/**
 * 메타 정보중 메타 값을 표현하는 인터페이스 (java 의 enum 과 유사)
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface MetaValue {

  /**
   * 메타 값
   *
   * @return 메타 값
   */
  String getValue();

  /**
   * 메타 값 설정
   *
   * @param value 메타 값
   */
  void setValue(String value);

  /**
   * 메타 값의 라벨
   *
   * @return 메타 값의 라벨
   */
  String getLabel();

  /**
   * 메타 값의 라벨을 설정합니다.
   *
   * @param label 라벨
   */
  void setLabel(String label);

  /**
   * 설명
   *
   * @return 메타 값의 설명
   */
  String getDescription();

  /**
   * 메타 값에 대한 설명을 설정합니다.
   *
   * @param description 설명
   */
  void setDescription(String description);

  /**
   * 메타 값에 대한 추가 속성 정보
   *
   * @return 메타 정보의 추가 속성
   */
  String getExAttr();

  /**
   * 메타 값에 대한 추가 속성 정보를 설정합니다.
   *
   * @param exAttr 추가 속성 정보
   */
  void setExAttr(String exAttr);

}
