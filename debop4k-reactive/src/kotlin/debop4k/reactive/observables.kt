@file:JvmName("observables")

package debop4k.reactive.kotlin

import rx.Observable
import rx.Observable.OnSubscribe
import rx.Subscriber
import rx.functions.Func0
import rx.observables.BlockingObservable

fun <T> emptyObservable(): Observable<T> = Observable.empty()
fun <T> observable(body: OnSubscribe<T>): Observable<T> = Observable.create(body)
fun <T> observable(body: (s: Subscriber<in T>) -> Unit): Observable<T> = Observable.create(body)

fun <T> defferedObservable(observableFactory: () -> Observable<T>): Observable<T> =
    Observable.defer(observableFactory)

fun <T> defferedObservable(observableFactory: Func0<Observable<T>>): Observable<T> =
    Observable.defer(observableFactory)

private fun <T> Iterator<T>.toIterable() = object : Iterable<T> {
  override fun iterator(): Iterator<T> = this@toIterable
}

fun <T> Iterable<T>.toObservable(): Observable<T> = Observable.from(this)
fun <T> Iterator<T>.toObservable(): Observable<T> = toIterable().toObservable()
fun <T> Sequence<T>.toObservable(): Observable<T> = Observable.from(object : Iterable<T> {
  override fun iterator(): Iterator<T> = this@toObservable.iterator()
})

fun BooleanArray.toObservable(): Observable<Boolean> = this.toList().toObservable()
fun ByteArray.toObservable(): Observable<Byte> = this.toList().toObservable()
fun ShortArray.toObservable(): Observable<Short> = this.toList().toObservable()
fun IntArray.toObservable(): Observable<Int> = this.toList().toObservable()
fun LongArray.toObservable(): Observable<Long> = this.toList().toObservable()
fun FloatArray.toObservable(): Observable<Float> = this.toList().toObservable()
fun DoubleArray.toObservable(): Observable<Double> = this.toList().toObservable()

fun IntProgression.toObservable(): Observable<Int> {
  if (step == 1 && last.toLong() - first < Integer.MAX_VALUE)
    return Observable.range(first, Math.max(0, last - first + 1))
  else
    return Observable.from(this@toObservable)
}

fun <T> T.toSingletonObservable(): Observable<T> = Observable.just(this)
fun <T> Throwable.toObservable(): Observable<T> = Observable.error(this)

fun <T> Iterable<Observable<out T>>.merge(): Observable<T> = Observable.merge(this.toObservable())
fun <T> Iterable<Observable<out T>>.mergeDelayError(): Observable<T> = Observable.mergeDelayError(this.toObservable())

fun <T, R> Observable<T>.fold(initial: R, body: (R, T) -> R): Observable<R> {
  return reduce(initial, { a, e -> body(a, e) })
}

fun <T> Observable<T>.onError(block: (Throwable) -> Unit): Observable<T> = doOnError(block)
fun <T> Observable<T>.firstOrNull(): Observable<T?> = this.firstOrDefault(null)
fun <T> BlockingObservable<T>.firstOrNull(): T = this.firstOrDefault(null)

fun <T> Observable<T>.onErrorReturnNull(): Observable<T?> = this.onErrorReturn<T> { null }

fun <T, R> Observable<T>.lift(operator: (Subscriber<in R>) -> Subscriber<in T>): Observable<R> {
  return this.lift { operator(it!!) }
}

fun <T : Any> Observable<T?>.requireNoNulls(): Observable<T> {
  return this.map { it ?: throw NullPointerException("null element found in rx observable") }
}

@Suppress("CAST_NEVER_SUCCEEDS")
fun <T : Any> Observable<T?>.filterNotNull(): Observable<T> = filter { it != null } as Observable<T>

fun <T> Observable<T>.withIndex(): Observable<IndexedValue<T>> {
  return this.zipWith(Observable.range(0, Int.MAX_VALUE)) { value: T, index: Int ->
    IndexedValue(index, value)
  }
}

fun <T, R> Observable<T>.flatMapSequence(body: (T) -> Sequence<R>): Observable<R> {
  return flatMap { body(it).toObservable() }
}

//inline fun <T> Observable<T>.subscribeWith(body: FunctionSubscriberModifier<T>.() -> Unit): Subscription {
//
//}

fun <T> Observable<Observable<T>>.switchOnNext(): Observable<T> = Observable.switchOnNext(this)

@Suppress("UNCHECKED_CAST")
fun <T, R> List<Observable<T>>.combineLatest(combineFunction: (args: List<T>) -> R): Observable<R> {
  return Observable.combineLatest(this, { combineFunction(it.asList() as List<T>) })
}

@Suppress("UNCHECKED_CAST")
fun <T, R> List<Observable<T>>.zip(zipFunction: (args: List<T>) -> R): Observable<R> {
  return Observable.zip(this, { zipFunction(it.asList() as List<T>) })
}

inline fun <reified R : Any> Observable<*>.cast(): Observable<R> = cast(R::class.java)






