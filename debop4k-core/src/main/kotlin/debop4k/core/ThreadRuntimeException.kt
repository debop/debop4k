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

package debop4k.core

import java.lang.RuntimeException

/**
 * ThreadRuntimeException
 * @author debop sunghyouk.bae@gmail.com
 */
open class ThreadRuntimeException : RuntimeException {
  constructor() : super()
  constructor(msg: String) : super(msg)
  constructor(msg: String, cause: Throwable) : super(msg, cause)
  constructor(cause: Throwable) : super(cause)
}