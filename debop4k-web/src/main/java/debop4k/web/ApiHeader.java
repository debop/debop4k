package debop4k.web;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Restful API 결과의 Header 정보입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class ApiHeader extends AbstractValueObject {

  public static ApiHeader of() {
    return new ApiHeader(200, "Success");
  }

  public static ApiHeader of(int code, String message) {
    return new ApiHeader(code, message);
  }

  private int code;
  private String message;

  public ApiHeader() {
    this(200, "Success");
  }

  public ApiHeader(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int hashCode() {
    return Hashx.compute(code);
  }

  @NotNull
  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("code", code)
                .add("message", message);
  }

  private static final long serialVersionUID = -730877857860406654L;
}
