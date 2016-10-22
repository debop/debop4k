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

package debop4k.web.interceptors

import debop4k.core.loggerOf
import org.aspectj.lang.annotation.Aspect
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Slf4jInterceptor
 * @author sunghyouk.bae@gmail.com
 */
@Aspect
class Slf4jInterceptor : HandlerInterceptorAdapter() {

  private val log = loggerOf(javaClass)

  override fun preHandle(request: HttpServletRequest,
                         response: HttpServletResponse,
                         handler: Any?): Boolean {
    log.trace("pre handle. request method={}, query={}", request.method, request.requestURL)
    return super.preHandle(request, response, handler)
  }

  override fun postHandle(request: HttpServletRequest,
                          response: HttpServletResponse,
                          handler: Any?,
                          modelAndView: ModelAndView?) {
    log.trace("post handle. response status={}, request method={}, query={}",
              response.status, request.method, request.requestURL)

    super.postHandle(request, response, handler, modelAndView)
  }
}