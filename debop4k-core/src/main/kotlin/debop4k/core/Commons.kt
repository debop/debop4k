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

@file:JvmName("Commons")

package debop4k.core

private val log = loggerOf("Commons")


/**
 * var 로 선언된 필드 중 non null 수형에 대해 초기화 값을 지정하고자 할 때 사용합니다.
 * 특히 ```@Autowired```, ```@Inject``` var 수형에 사용하기 좋다.
 * @see lateinit
 * @see Delegates.nonNull
 */
fun <T> uninitialized(): T = null as T

/** 두 인스턴스가 같은가? */
fun areEquals(a: Any?, b: Any?): Boolean {
  return (a === b) || (a != null && a == b)
}

infix inline fun <T> T.initializedBy(initializer: (T) -> Unit): T {
  initializer(this)
  return this
}

infix inline fun <T> T.initializeWith(initialize: T.() -> Unit): T {
  this.initialize()
  return this
}

infix inline fun <T> T.with(block: T.() -> Unit): T {
  this.block()
  return this
}

infix inline fun <T : Any, R : Any> T?.whenNotNull(thenDo: (T) -> R?): R?
    = if (this == null) null else thenDo(this)

infix inline fun <T : Any, R : Any> T?.withNotNull(thenDo: T.() -> R?): R?
    = if (this == null) null else this.thenDo()

fun <T : Any, R : Any> Collection<T?>.whenAllNotNull(block: (Collection<T>) -> R) {
  if (this.all { it != null }) {
    block(this.filterNotNull())
  }
}

fun <T : Any, R : Any> Collection<T?>.whenAnyNotNull(block: (Collection<T>) -> R) {
  if (this.any { it != null }) {
    block(this.filterNotNull())
  }
}


