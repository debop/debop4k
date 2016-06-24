/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.utils

/**
 * kotlin 수형 중 nullable 이 아니게 선언된 변수의 초기화를 수행할 때 사용합니다.
 * <code>
 *   var person:Person = uninitialized()
 * </code>
 */
public fun <T> uninitialized() = null as T
