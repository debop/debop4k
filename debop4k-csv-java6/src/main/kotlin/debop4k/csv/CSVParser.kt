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

package debop4k.csv

import org.slf4j.LoggerFactory

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class CSVParser(val format: CSVFormat = DEFAULT_CSVFORMAT) {

  private val log = LoggerFactory.getLogger(javaClass)

  private val emptyLineList = listOf("")
  private val emptyStringList = listOf<String>()

  fun parseLine(input: String): List<String>? {
    val parsedResult = parse(input, format.escapeChar, format.delimiter, format.quoteChar)
    val ignoreLine = parsedResult == emptyLineList && format.treatEmptyLineAsNull
    return if (ignoreLine) emptyStringList else parsedResult
  }

  companion object {

    private val log = LoggerFactory.getLogger(javaClass)

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
          Start -> when (c) {
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

          Delimiter -> when (c) {
            quoteChar -> {
              state = QuoteStart
              pos++
            }
            escapeChar -> {
              if (pos + 1 < buflen && (buf[pos + 1] == escapeChar || buf[pos + 1] == delimiter)) {
                field.append(buf[pos + 1])
                state = Field
                pos += 2
              } else {
                throw MalformedCSVException(buf.joinToString(separator = ""))
              }
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
          Field -> when (c) {
            escapeChar -> {
              if (pos + 1 < buflen) {
                if (buf[pos + 1] == escapeChar || buf[pos + 1] == delimiter) {
                  field.append(buf[pos + 1])
                  state = Field
                  pos += 2
                } else {
                  throw MalformedCSVException(buf.joinToString(separator = ""))
                }
              } else {
                state = QuoteEnd
                pos++
              }
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

          QuoteStart -> when (c) {
            escapeChar -> {
              if (c != quoteChar) {
                if (pos + 1 < buflen) {
                  if (buf[pos + 1] == escapeChar || buf[pos + 1] == quoteChar) {
                    field.append(buf[pos + 1])
                    state = QuotedField
                    pos += 2
                  } else {
                    throw MalformedCSVException(buf.joinToString(separator = ""))
                  }
                } else {
                  throw MalformedCSVException(buf.joinToString(separator = ""))
                }
              } else if (c == quoteChar) {
                if (pos + 1 < buflen && buf[pos + 1] == quoteChar) {
                  field.append(c)
                  state = QuotedField
                  pos += 2
                } else {
                  state = QuoteEnd
                  pos++
                }
              }
            }
            quoteChar -> {
              if (pos + 1 < buflen && buf[pos + 1] == quoteChar) {
                field.append(c)
                state = QuotedField
                pos += 2
              } else {
                state = QuoteEnd
                pos++
              }
            }
            else -> {
              field.append(c)
              state = QuotedField
              pos++
            }
          }

          QuoteEnd -> when (c) {
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
//              log.warn("c={}, buf={}", c, buf.joinToString(separator=""))
              throw MalformedCSVException(buf.joinToString(separator = ""))
            }
          }

          QuotedField -> when (c) {
            escapeChar -> {
              if (c != quoteChar) {
                if (pos + 1 < buflen) {
                  if (buf[pos + 1] == escapeChar || buf[pos + 1] == quoteChar) {
                    field.append(buf[pos + 1])
                    state = QuotedField
                    pos += 2
                  } else {
                    throw MalformedCSVException(buf.joinToString(separator = ""))
                  }
                } else {
                  throw MalformedCSVException(buf.joinToString(separator = ""))
                }
              } else if (c == quoteChar) {
                if (pos + 1 < buflen && buf[pos + 1] == quoteChar) {
                  field.append(c)
                  state = QuotedField
                  pos += 2
                } else {
                  state = QuoteEnd
                  pos++
                }
              }
            }
            quoteChar -> {
              if (pos + 1 < buflen && buf[pos + 1] == quoteChar) {
                field.append(c)
                state = QuotedField
                pos += 2
              } else {
                state = QuoteEnd
                pos++
              }
            }
            else -> {
              field.append(c)
              state = QuotedField
              pos++
            }
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
            }
          }
          fields.toList()
        }
      }
    }
  }
}