@file:JvmName("CompletableFutureExtensions")

package debop4k.core.java8.async

import java.util.concurrent.*
import java.util.function.*

fun runAsync(action: () -> Unit, executor: Executor = ForkJoinPool.commonPool()): CompletableFuture<Void>
    = CompletableFuture.runAsync(Runnable(action), executor)

fun <T> supplyAsync(supplier: () -> T, executor: Executor = ForkJoinPool.commonPool()): CompletableFuture<T>
    = CompletableFuture.supplyAsync(Supplier(supplier), executor)

fun <T> completedFuture(result: T): CompletableFuture<T>
    = CompletableFuture.completedFuture(result)




