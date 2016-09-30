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

package debop4k.core.utils

import java.util.concurrent.*

/**
 * 코드 실행 시간을 측정하는 Stopwatch 입니다.
 * 내부적으로 nanotime 을 측정하고, milliseconds 단위로 표시합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2013. 12. 10. 오후 1:40
 */
class Stopwatch(val msg: String = "", val runGC: Boolean = false) {

  @Volatile private var _isRunning: Boolean = false
  private var _elapsedNanos = 0L
  private var _startTick = 0L

  private fun cleanUp() {
    System.gc()
  }

  val isRunning: Boolean get() = _isRunning
  val elapsedNanos: Long
    get() {
      return if (_isRunning) System.nanoTime() - _startTick + _elapsedNanos
      else _elapsedNanos
    }


  fun start(): Stopwatch {
    require(!_isRunning) { "This stopwatch is already running." }
    _isRunning = true
    _startTick = System.nanoTime()
    return this
  }

  fun stop(): Stopwatch {
    require(isRunning) { "This stopwatch is already stopped." }
    val tick = System.nanoTime()
    _isRunning = false
    _elapsedNanos += tick - _startTick
    return this
  }

  fun reset(): Stopwatch {
    _elapsedNanos = 0L
    _startTick = 0L
    return this
  }

  fun elapsed(desireUnit: TimeUnit): Long {
    return desireUnit.convert(elapsedNanos, TimeUnit.NANOSECONDS)
  }

  override fun toString(): String {
    val nanos = elapsedNanos
    val value = nanos.toDouble() / TimeUnit.NANOSECONDS.convert(1, TimeUnit.MILLISECONDS)

    return "%.4g msec".format(value)
  }

  companion object {

    @JvmOverloads
    @JvmStatic
    fun createStarted(msg: String = "", runGC: Boolean = false): Stopwatch {
      val sw = Stopwatch(msg, runGC)
      sw.start()
      return sw
    }
  }
}