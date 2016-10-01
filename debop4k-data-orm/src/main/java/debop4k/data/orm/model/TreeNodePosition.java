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

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 트리 형태의 구조 상에서 위치 (Level - Depth, Order - 순서) 를 나타냅니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Embeddable
@Getter
@Setter
public class TreeNodePosition extends AbstractValueObject {

  /**
   * Static 생성자
   *
   * @return {@link TreeNodePosition} 인스턴스
   */
  public static TreeNodePosition of() {
    return of(0, 0);
  }

  /**
   * Static 생성자
   *
   * @param level 루트 노드로부터의 자손 세대 수
   * @param order 정렬 순서
   * @return {@link TreeNodePosition} 인스턴스
   */
  public static TreeNodePosition of(int level, int order) {
    return new TreeNodePosition(level, order);
  }

  /**
   * 원본 {@link TreeNodePosition} 객체의 속성을 복사하여 새로운 인스턴스를 생성합니다.
   *
   * @param src 원본 객체
   * @return 복제한 객체
   */
  public static TreeNodePosition of(@NonNull TreeNodePosition src) {
    return new TreeNodePosition(src);
  }

  /**
   * 트리 상의 자식 레벨 (depth) 를 나타냅니다 ( 루트 노드로부터의 자손 세대 수 )
   */
  @Column(name = "treeLevel")
  private int level;

  /**
   * 같은 형제끼리의 순서를 나타냅니다.
   */
  @Column(name = "treeOrder")
  private int order;

  /**
   * 기본 생성자
   */
  public TreeNodePosition() { this(0, 0); }

  /**
   * 생성자
   *
   * @param level 루트 노드로부터의 자손 세대 수
   * @param order 정렬 순서
   */
  public TreeNodePosition(int level, int order) {
    this.level = level;
    this.order = order;
  }

  public TreeNodePosition(@NonNull TreeNodePosition src) {
    this.level = src.getLevel();
    this.order = src.getOrder();
  }

  @Override
  public int hashCode() {
    return Hashx.compute(level, order);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("level", level)
                .add("order", order);
  }

  private static final long serialVersionUID = -4189294469958966790L;
}
