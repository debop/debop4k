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