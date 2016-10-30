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
 */
@file:JvmName("Singlex")

package debop4k.reactive

import rx.*
import java.util.concurrent.*
import java.util.function.*

fun <T> single(body: (SingleSubscriber<in T>) -> Unit): Single<T> = Single.create(body)
fun <T> singleOf(value: T): Single<T> = Single.just(value)
fun <T> Future<out T>.toSingle(): Single<out T> = Single.from(this)
fun <T> Callable<out T>.toSingle(): Single<out T> = Single.fromCallable { this.call() }
fun <T> Function0<T>.toSingle(): Single<T> = Single.fromCallable { this.invoke() }
fun <T> Throwable.toSingle(): Single<T> = Single.error(this)

// for Java 8
fun <T> Supplier<T>.toSingle(): Single<T> = Single.fromCallable { this.get() }

inline fun <T> Single<T>.subscribeWith(body: FunctionSingleSubscriberModifier<T>.() -> Unit): Subscription {
  val modifier = FunctionSingleSubscriberModifier(singleSubscriber<T>())
  modifier.body()
  return this.subscribe(modifier.subscriber)
}
