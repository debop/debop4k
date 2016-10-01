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
@file:JvmName("subscribers")

package debop4k.reactive

import rx.SingleSubscriber
import rx.Subscriber
import rx.exceptions.OnErrorNotImplementedException
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

class FunctionSingleSubscriber<T>() : SingleSubscriber<T>() {
  private val onSuccessFunctions = arrayListOf<(T) -> Unit>()
  private val onErrorFunctions = arrayListOf<(Throwable) -> Unit>()

  override fun onSuccess(value: T): Unit = onSuccessFunctions.forEach { it(value) }
  override fun onError(error: Throwable?): Unit {
    val err = error ?: RuntimeException("Unknown exception")
    return err.let { ex ->
      if (onErrorFunctions.isEmpty()) {
        throw OnErrorNotImplementedException(ex)
      } else {
        onErrorFunctions.forEach { it(ex) }
      }
    }
  }

  fun onSuccess(onSuccessFunc: (T) -> Unit): FunctionSingleSubscriber<T> = copy {
    onSuccessFunctions.add(onSuccessFunc)
  }

  fun onError(onErrorFunc: (Throwable) -> Unit): FunctionSingleSubscriber<T> = copy {
    onErrorFunctions.add(onErrorFunc)
  }

  private fun copy(block: FunctionSingleSubscriber<T>.() -> Unit): FunctionSingleSubscriber<T> {
    val newSubscriber = FunctionSingleSubscriber<T>()
    newSubscriber.onSuccessFunctions.addAll(this.onSuccessFunctions)
    newSubscriber.onErrorFunctions.addAll(this.onErrorFunctions)

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

class FunctionSingleSubscriberModifier<T>(functionSubscriber: FunctionSingleSubscriber<T> = singleSubscriber()) {
  var subscriber: FunctionSingleSubscriber<T> = functionSubscriber
    private set

  fun onSuccess(onSuccessFunc: (T) -> Unit): Unit {
    subscriber = subscriber.onSuccess(onSuccessFunc)
  }

  fun onError(onErrorFunc: (Throwable) -> Unit): Unit {
    subscriber = subscriber.onError(onErrorFunc)
  }
}

fun <T> subscriber(): FunctionSubscriber<T> = FunctionSubscriber<T>()
fun <T> singleSubscriber(): FunctionSingleSubscriber<T> = FunctionSingleSubscriber<T>()

fun <T> Subscriber<T>.synchronized(): Subscriber<T> = SerializedSubscriber(this)
fun Subscriber<*>.add(unsubscribe: () -> Unit) = add(Subscriptions.create(unsubscribe))
