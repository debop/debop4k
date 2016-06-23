@file:JvmName("single")

package debop4k.reactive.kotlin

import rx.Single
import rx.SingleSubscriber
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.function.Supplier

fun <T> single(body: (SingleSubscriber<in T>) -> Unit): Single<T> = Single.create(body)
fun <T> singleOf(value: T): Single<T> = Single.just(value)
fun <T> Future<out T>.toSingle(): Single<out T> = Single.from(this)
fun <T> Callable<out T>.toSingle(): Single<out T> = Single.fromCallable { this.call() }

fun <T> (() -> T).toSingle(): Single<T> = Single.fromCallable { this() }

// for Java 8
fun <T> Supplier<T>.toSingle(): Single<T> = Single.fromCallable { this.get() }
