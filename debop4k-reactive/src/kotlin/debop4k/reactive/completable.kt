package debop4k.reactive

import rx.Completable
import rx.Observable
import rx.Single
import rx.functions.Action0
import java.util.concurrent.Callable
import java.util.concurrent.Future

fun <T> completableOf(f: () -> T): Completable = Completable.fromAction { f.invoke() }

fun Action0.toCompletable(): Completable = Completable.fromAction(this)
fun <T> Callable<T>.toCompletable(): Completable = Completable.fromCallable { this.call() }
fun <T> Future<T>.toCompletable(): Completable = Completable.fromFuture(this)
fun <T> Single<T>.toCompletable(): Completable = Completable.fromSingle(this)
fun <T> Observable<T>.toCompletable(): Completable = Completable.fromObservable(this)