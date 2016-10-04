/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.web;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class ApiResult extends AbstractValueObject {

  public static ApiResult of() {
    return of(null);
  }

  public static ApiResult of(Object body) {
    return new ApiResult(ApiHeader.of(), body);
  }

  public static ApiResult of(int code, String message) {
    return new ApiResult(ApiHeader.of(code, message), null);
  }

  private ApiHeader header;
  private Object body;

  public ApiResult() {
    this(ApiHeader.of(), null);
  }

  public ApiResult(ApiHeader header, Object body) {
    this.header = header;
    this.body = body;
  }


  @Override
  public int hashCode() {
    return Hashx.compute(header, body);
  }

  @NotNull
  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("header", header)
                .add("func", body);
  }

  private static final long serialVersionUID = -3551234079686617763L;
}
