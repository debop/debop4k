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

package debop4k.web.advisors

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.RuntimeException

/**
 * Spring MVC의 Controller 에서 발생하는 예외를 처리하는 Handler 입니다.
 * @author sunghyouk.bae@gmail.com
 */
@ControllerAdvice
class ControllerExceptionAdvice {

  @ExceptionHandler(RuntimeException::class)
  fun runtimeExceptionHandler(): String = "error/runtime"

}