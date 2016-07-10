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

package debop4k.csv

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class CSVParser(val format: CSVFormat = DEFAULT_CSVFORMAT) {

  fun parseLine(input: String): List<String>? {
    val parsedResult = parse(input, format.escapeChar, format.delimiter, format.quoteChar)
    val isEmpty = parsedResult?.isEmpty() ?: true
    return if (isEmpty && format.treatEmptyLineAsNull) null
    else parsedResult
  }

  companion object {
    private const val Start = 0
    private const val Field = 1
    private const val Delimiter = 2
    private const val End = 3
    private const val QuoteStart = 4
    private const val QuoteEnd = 5
    private const val QuotedField = 6

    fun parse(input: String, escapeChar: Char, delimiter: Char, quoteChar: Char): List<String>? {
      val buf = input.toCharArray()
      var fields = mutableListOf<String>()
      var field = StringBuilder()
      var state = Start
      var pos: Int = 0
      val buflen = buf.size

      if (buf.size > 0 && buf[0] == '\uFEFF') {
        pos++
      }

      while (state != End && pos < buflen) {
        val c = buf[pos]

        when (state) {
          Start -> {
            when (c) {
              quoteChar -> {
                state = QuoteStart
                pos++
              }
              delimiter -> {
                fields.add(field.toString())
                field = StringBuilder()
                state = Delimiter
                pos++
              }
              in CSV_SEPARATORS -> {
                fields.add(field.toString())
                field = StringBuilder()
                state = End
                pos++
              }
              '\r' -> {
                if (pos + 1 < buflen && buf[1] == '\n') {
                  pos++
                }
                fields.add(field.toString())
                field = StringBuilder()
                state = End
                pos++
              }
              else -> {
                field.append(c)
                state = Field
                pos++
              }
            }
          }
          Delimiter -> {
            TODO()
          }
          Field -> {
            TODO()
          }
          QuoteStart -> {
            TODO()
          }
          QuoteEnd -> {
            TODO()
          }
          QuotedField -> {
            TODO()
          }
          End -> {
            RuntimeException("unexpected error")
          }
        }
      }

      return when (state) {
        Delimiter -> {
          fields.add("")
          fields.toList()
        }
        QuotedField -> {
          null
        }
        else -> {
          if (!field.isEmpty()) {
            when (state) {
              Field, QuoteEnd -> fields.add(field.toString())
              else -> {
              }
            }
          }
          fields.toList()
        }
      }
    }
  }
}