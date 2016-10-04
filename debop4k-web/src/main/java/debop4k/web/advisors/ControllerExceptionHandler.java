package debop4k.web.advisors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ControllerExceptionHandler
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2016. 3. 13.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public String runtimeExceptionHandler() {
    return "error/runtime";
  }
}
