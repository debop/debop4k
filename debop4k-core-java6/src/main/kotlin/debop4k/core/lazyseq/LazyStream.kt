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

package debop4k.core.lazyseq

import java.io.Closeable
import java.util.*


interface BaseLazyStream<T, S : BaseLazyStream<T, S>> : Closeable {
  /**
   * Returns an iterator for the elements of this stream.

   *
   * This is a [terminal
       * operation](package-summary.html#StreamOps).

   * @return the element iterator for this stream
   */
  abstract operator fun iterator(): Iterator<T>

  /**
   * Returns a spliterator for the elements of this stream.

   *
   * This is a [terminal
       * operation](package-summary.html#StreamOps).

   * @return the element spliterator for this stream
   */
  abstract fun spliterator(): Spliterator<T>

  /**
   * Returns whether this stream, if a terminal operation were to be executed,
   * would execute in parallel.  Calling this method after invoking an
   * terminal stream operation method may yield unpredictable results.

   * @return `true` if this stream would execute in parallel if executed
   */
  abstract fun isParallel(): Boolean

  /**
   * Returns an equivalent stream that is sequential.  May return
   * itself, either because the stream was already sequential, or because
   * the underlying stream state was modified to be sequential.

   *
   * This is an [intermediate
       * operation](package-summary.html#StreamOps).

   * @return a sequential stream
   */
  abstract fun sequential(): S

  /**
   * Returns an equivalent stream that is parallel.  May return
   * itself, either because the stream was already parallel, or because
   * the underlying stream state was modified to be parallel.

   *
   * This is an [intermediate
       * operation](package-summary.html#StreamOps).

   * @return a parallel stream
   */
  abstract fun parallel(): S

  /**
   * Returns an equivalent stream that is
   * [unordered](package-summary.html#Ordering).  May return
   * itself, either because the stream was already unordered, or because
   * the underlying stream state was modified to be unordered.

   *
   * This is an [intermediate
       * operation](package-summary.html#StreamOps).

   * @return an unordered stream
   */
  abstract fun unordered(): S

  /**
   * Returns an equivalent stream with an additional close handler.  Close
   * handlers are run when the [.close] method
   * is called on the stream, and are executed in the order they were
   * added.  All close handlers are run, even if earlier close handlers throw
   * exceptions.  If any close handler throws an exception, the first
   * exception thrown will be relayed to the caller of `close()`, with
   * any remaining exceptions added to that exception as suppressed exceptions
   * (unless one of the remaining exceptions is the same exception as the
   * first exception, since an exception cannot suppress itself.)  May
   * return itself.

   *
   * This is an [intermediate
       * operation](package-summary.html#StreamOps).

   * @param closeHandler A task to execute when the stream is closed
   * *
   * @return a stream with a handler that is run if the stream is closed
   */
  abstract fun onClose(closeHandler: Runnable): S

  /**
   * Closes this stream, causing all close handlers for this stream pipeline
   * to be called.

   * @see AutoCloseable.close
   */
  abstract override fun close()
}

/**
 * LazyStream
 * @author sunghyouk.bae@gmail.com
 */
interface LazyStream<T> : BaseLazyStream<T, LazyStream<T>> {
}