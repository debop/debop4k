/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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
@file:JvmName("Completables")

package debop4k.reactive

import rx.Completable
import rx.Observable
import rx.Single
import rx.functions.Action0
import java.util.concurrent.*
import kotlin.reflect.KCallable

fun <T> completableOf(f: () -> T): Completable = Completable.fromAction { f.invoke() }
fun <T> completableOf(f: KCallable<T>): Completable = Completable.fromCallable { f.call() }

fun Action0.toCompletable(): Completable = Completable.fromAction(this)
fun <T> Function0<T>.toCompletable(): Completable = Completable.fromCallable { this.invoke() }

fun <T> KCallable<T>.toCompletable(): Completable = Completable.fromCallable { this.call() }

fun <T> Callable<T>.toCompletable(): Completable = Completable.fromCallable { this.call() }
fun <T> Future<T>.toCompletable(): Completable = Completable.fromFuture(this)
fun <T> Single<T>.toCompletable(): Completable = Completable.fromSingle(this)
fun <T> Observable<T>.toCompletable(): Completable = Completable.fromObservable(this)