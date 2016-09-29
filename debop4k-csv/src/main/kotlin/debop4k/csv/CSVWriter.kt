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
import java.io.*
import java.nio.charset.Charset

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class CSVWriter(val writer: Writer,
                val format: CSVFormat = DEFAULT_CSVFORMAT) : Closeable {

  private val log = LoggerFactory.getLogger(CSVWriter::class.java)

  private val printWriter = PrintWriter(writer)
  private val quoteMinimalSpace = arrayOf('\r', '\n', format.quoteChar, format.delimiter)

  override fun close(): Unit {
    printWriter.close()
  }

  fun flush(): Unit {
    printWriter.flush()
  }

  fun writeAll(allLines: List<List<*>>): Unit {
    allLines.forEach { writeNext(it) }
    if (printWriter.checkError()) {
      throw IOException("Failed to write all.")
    }
  }

  private fun writeNext(fields: List<*>): Unit {

    fun shouldQuote(field: String, quoting: Quoting): Boolean {
      return when (quoting) {
        QUOTE_ALL -> true
        QUOTE_MINIMAL -> {
          var i = 0
          while (i < field.length) {
            val char = field[i]
            var j = 0
            while (j < quoteMinimalSpace.size) {
              val quoteSpec = quoteMinimalSpace[j]
              if (quoteSpec == char) {
                return true
              }
              j++
            }
            i++
          }
          false
        }
        QUOTE_NONE -> false
        QUOTE_NONNUMERIC -> {
          var foundDot = false
          var i = 0
          while (i < field.length) {
            val char = field[i]
            if (char == '.') {
              if (foundDot) {
                return true
              } else {
                foundDot = true
              }
            } else if (char < '0' || char > '9') {
              return true
            }
            i++
          }
          false
        }
        else -> false
      }
    }

    fun printField(field: String): Unit {
      if (shouldQuote(field, format.quoting)) {
        printWriter.print(format.quoteChar)
        var i = 0
        while (i < field.length) {
          val char = field[i]
          if (char == format.quoteChar || (format.quoting == QUOTE_NONE && char == format.delimiter)) {
            printWriter.print(format.quoteChar)
          }
          printWriter.print(char)
          i++
        }
        printWriter.print(format.quoteChar)
      } else {
        printWriter.print(field)
      }
    }

    val iterator = fields.iterator()
    var hasNext = iterator.hasNext()
    while (hasNext) {
      val next = iterator.next()
      if (next != null) {
        log.trace("print field. field={}", next)
        printField(next.toString())
      }
      hasNext = iterator.hasNext()
      if (hasNext) {
        printWriter.print(format.delimiter)
      }
    }
    printWriter.print(format.lineTerminator)
  }

  fun writeRow(fields: List<*>): Unit {
    writeNext(fields)
    if (printWriter.checkError()) {
      throw IOException("Failed to write fields.")
    }
  }


  companion object {
    @JvmOverloads
    @JvmStatic
    fun open(writer: Writer, format: CSVFormat = DEFAULT_CSVFORMAT): CSVWriter {
      return CSVWriter(writer, format)
    }

    @JvmOverloads
    @JvmStatic
    fun open(file: File,
             append: Boolean = false,
             cs: Charset = DEFAULT_CHARSET,
             format: CSVFormat = DEFAULT_CSVFORMAT): CSVWriter {
      val fos = FileOutputStream(file, append)
      return open(fos, cs, format)
    }

    @JvmOverloads
    @JvmStatic
    fun open(filename: String,
             append: Boolean = false,
             cs: Charset = DEFAULT_CHARSET,
             format: CSVFormat = DEFAULT_CSVFORMAT): CSVWriter {
      val fos = FileOutputStream(filename, append)
      return open(fos, cs, format)
    }

    @JvmOverloads
    @JvmStatic
    fun open(fos: OutputStream,
             cs: Charset = DEFAULT_CHARSET,
             format: CSVFormat = DEFAULT_CSVFORMAT): CSVWriter {
      try {
        val writer = OutputStreamWriter(fos, cs)
        return open(writer, format)
      } catch(e: UnsupportedEncodingException) {
        fos.close()
        throw e
      }
    }
  }
}