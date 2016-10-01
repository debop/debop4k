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

/**
 * 메타데이터를 표현하는 Value Object 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class SimpleMetaValue extends AbstractValueObject implements MetaValue {

  public static SimpleMetaValue of(Object value) {
    return new SimpleMetaValue(value);
  }

  public static SimpleMetaValue of(Object value, String label) {
    SimpleMetaValue mv = new SimpleMetaValue(value);
    mv.setLabel(label);
    return mv;
  }

  public static SimpleMetaValue of(@NonNull MetaValue src) {
    return new SimpleMetaValue(src);
  }

  private String value;
  private String label;
  private String description;
  private String exAttr;

  public SimpleMetaValue() {}

  public SimpleMetaValue(Object value) {
    this.value = (value == null) ? "" : value.toString();
  }

  public SimpleMetaValue(@NonNull MetaValue src) {
    this.value = src.getValue();
    this.label = src.getLabel();
    this.description = src.getDescription();
    this.exAttr = src.getExAttr();
  }


  @Override
  public int hashCode() {
    return Hashx.compute(value);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("value", value)
                .add("label", label)
                .add("description", description)
                .add("exAttr", exAttr);
  }

  private static final long serialVersionUID = 8933609650760041836L;
}
