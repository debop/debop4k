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

package debop4k.timeperiod.calendars

/**
 * Created by debop
 */
enum class SeekDirection(val value: Int) {

  /** 미래로 (시간 값을 증가 시키는 방향) */
  Forward(1),

  /** 과거로 (시간 값을 감소 시키는 방향) */
  Backward(-1);

  companion object {
    fun of(value: Int): SeekDirection {
      return if (value > 0) Forward else Backward
    }
  }
}