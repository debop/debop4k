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

@file:JvmName("CompletableFuturex")

package debop4k.core.asyncs.java8

import java.util.concurrent.*
import java.util.function.*


inline fun runAsync(crossinline body: () -> Unit): CompletableFuture<Void>
    = CompletableFuture.runAsync { Runnable { body() } }

inline fun <T> supplyAsync(crossinline body: () -> T): CompletableFuture<T>
    = CompletableFuture.supplyAsync(Supplier { body() })

fun <T> completedFuture(result: T): CompletableFuture<T>
    = CompletableFuture.completedFuture(result)

fun allOfFutures(vararg futures: CompletableFuture<*>): CompletableFuture<Void>
    = CompletableFuture.allOf(*futures)

fun anyOfFutures(vararg futures: CompletableFuture<*>): CompletableFuture<Any>
    = CompletableFuture.anyOf(*futures)

