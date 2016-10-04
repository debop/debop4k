package debop4k.web.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring MVC에서 사용할 Logging Interceptor 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@Aspect
public class LoggingInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
    log.debug("pre handle. request method={}, query={}", request.getMethod(), request.getRequestURL());
    return super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler,
                         ModelAndView modelAndView) throws Exception {
    log.debug("post handle. response status={}, request method={}, query={}",
              response.getStatus(), request.getMethod(), request.getRequestURL());
    super.postHandle(request, response, handler, modelAndView);
  }
}
