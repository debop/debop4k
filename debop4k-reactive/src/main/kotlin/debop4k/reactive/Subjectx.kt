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
@file:JvmName("Subjectx")

package debop4k.reactive

import rx.schedulers.TestScheduler
import rx.subjects.*

fun <T> BehaviorSubject(): BehaviorSubject<T> = rx.subjects.BehaviorSubject.create()
fun <T> BehaviorSubject(default: T): BehaviorSubject<T> = rx.subjects.BehaviorSubject.create(default)
fun <T> AsyncSubject(): AsyncSubject<T> = rx.subjects.AsyncSubject.create()
fun <T> PublishSubject(): PublishSubject<T> = rx.subjects.PublishSubject.create()
fun <T> ReplaySubject(capacity: Int = 16): ReplaySubject<T> = rx.subjects.ReplaySubject.create(capacity)

fun <F, T> Subject<F, T>.synchronized(): Subject<F, T> = SerializedSubject(this)

fun <T> TestSubject(scheduler: TestScheduler = TestScheduler()): TestSubject<T> = rx.subjects.TestSubject.create(scheduler)