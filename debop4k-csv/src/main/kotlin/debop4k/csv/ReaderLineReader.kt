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

package debop4k.csv

import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.Reader

/**
 * [Reader] 로부터 CSV 를 파싱하는 Line Reader
 *
 * @author debop sunghyouk.bae@gmail.com
 */
class ReaderLineReader(val reader: Reader) : LineReader {

  private val log = LoggerFactory.getLogger(javaClass)

  val bufferedReader = BufferedReader(reader)

  private fun Char.isLineFeed(): Boolean = CSV_SEPARATORS.contains(this)

  override fun readLineWithTerminator(): String? {
    val sb = StringBuilder()

    loop@ do {
      val c = bufferedReader.read()

      if (c == -1) {
        if (sb.length == 0) {
          return null
        } else {
          break@loop
        }
      }

      sb.append(c.toChar())
      if (c.toChar() in CSV_SEPARATORS)
        break@loop

      if (c.toChar() == '\r') {
        bufferedReader.mark(1)
        val c2 = bufferedReader.read()
        when (c2) {
          -1 -> break@loop
          '\n'.toInt() -> sb.append('\n')
          else -> bufferedReader.reset()
        }
      }
    } while (true)

    return sb.toString()
  }

  override fun close() {
    try {
      bufferedReader.close()
      reader.close()
    } catch(ignored: Exception) {
      log.warn("Fail to close reader.", ignored)
    }
  }
}