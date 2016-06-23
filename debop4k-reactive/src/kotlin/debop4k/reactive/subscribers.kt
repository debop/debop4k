@file:JvmName("subscribers")

package debop4k.reactive

import rx.Subscriber
import rx.exceptions.OnErrorNotImplementedException
import rx.functions.Action0
import rx.observers.SerializedSubscriber
import rx.subscriptions.Subscriptions
import java.util.*

class FunctionSubscriber<T>() : Subscriber<T>() {

  private val onCompletedFunctions = ArrayList<() -> Unit>()
  private val onErrorFunctions = ArrayList<(e: Throwable) -> Unit>()
  private val onNextFunctions = ArrayList<(value: T) -> Unit>()
  private val onStartFunctions = ArrayList<() -> Unit>()

  override fun onCompleted() = onCompletedFunctions.forEach { it() }

  override fun onNext(t: T) = onNextFunctions.forEach { it(t) }

  override fun onError(e: Throwable?) = (e ?: RuntimeException("Unknown exception")).let { ex ->
    if (onErrorFunctions.isEmpty()) {
      throw OnErrorNotImplementedException(ex)
    } else {
      onErrorFunctions.forEach { it(ex) }
    }
  }

  override fun onStart() = onStartFunctions.forEach { it() }

  fun onCompleted(onCompletedFunc: () -> Unit): FunctionSubscriber<T> = copy {
    onCompletedFunctions.add(onCompletedFunc)
  }

  fun onError(onErrorFunc: (Throwable) -> Unit): FunctionSubscriber<T> = copy {
    onErrorFunctions.add(onErrorFunc)
  }

  fun onNext(onNextFunc: (T) -> Unit): FunctionSubscriber<T> = copy {
    onNextFunctions.add(onNextFunc)
  }

  fun onStart(onStartFunc: () -> Unit): FunctionSubscriber<T> = copy {
    onStartFunctions.add(onStartFunc)
  }

  private fun copy(block: FunctionSubscriber<T>.() -> Unit): FunctionSubscriber<T> {
    val newSubscriber = FunctionSubscriber<T>()
    newSubscriber.onCompletedFunctions.addAll(this.onCompletedFunctions)
    newSubscriber.onErrorFunctions.addAll(this.onErrorFunctions)
    newSubscriber.onNextFunctions.addAll(this.onNextFunctions)
    newSubscriber.onStartFunctions.addAll(this.onStartFunctions)

    newSubscriber.block()

    return newSubscriber
  }
}

class FunctionSubscriberModifier<T>(functionSubscriber: FunctionSubscriber<T> = subscriber()) {
  var subscriber: FunctionSubscriber<T> = functionSubscriber
    private set

  fun onCompleted(onCompletedFunc: () -> Unit): Unit {
    subscriber = subscriber.onCompleted(onCompletedFunc)
  }

  fun onError(onErrorFunc: (t: Throwable) -> Unit): Unit {
    subscriber = subscriber.onError(onErrorFunc)
  }

  fun onNext(onNextFunc: (t: T) -> Unit): Unit {
    subscriber = subscriber.onNext(onNextFunc)
  }

  fun onStart(onStartFunc: () -> Unit): Unit {
    subscriber = subscriber.onStart(onStartFunc)
  }
}

fun <T> subscriber(): FunctionSubscriber<T> = FunctionSubscriber()
fun <T> Subscriber<T>.synchronized(): Subscriber<T> = SerializedSubscriber(this)
fun Subscriber<*>.add(unsubscribe: () -> Unit) = add(Subscriptions.create(unsubscribe))
fun Subscriber<*>.add(unsubscribe: Action0) = add(Subscriptions.create(unsubscribe))
