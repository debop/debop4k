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

import org.eclipse.collections.impl.list.mutable.FastList
import org.slf4j.LoggerFactory
import java.io.*
import java.nio.charset.Charset
import java.util.*

/**
 * CSV Reader
 * @author debop sunghyouk.bae@gmail.com
 */
class CSVReader(val lineReader: LineReader,
                val format: CSVFormat = DEFAULT_CSVFORMAT) : Closeable {

  private val parser = CSVParser(format)
  private val log = LoggerFactory.getLogger(javaClass)

  private val EMPTY_STRINGLIST = listOf<String>()
  private fun emptyStringList() = FastList.newList<String>()

  fun readNext(): List<String>? {

    tailrec fun parseNext(lineReader: LineReader, leftOver: String? = null): FastList<String>? {
      val nextLine = lineReader.readLineWithTerminator()

      return if (nextLine == null) {
        if (!leftOver.isNullOrBlank()) {
          throw MalformedCSVException("Malformed Input: [$leftOver]")
        } else {
          null
        }
      } else {
        val line = (leftOver ?: "") + nextLine
        val result = parser.parseLine(line)

        if (result == null) {
          return parseNext(lineReader, line)
        } else {
          return result
        }
      }
    }

    return parseNext(lineReader)
  }

  inline fun forEach(f: (List<String>) -> Unit): Unit = iterator().forEach(f)

  fun iterator(): Iterator<List<String>> = object : Iterator<List<String>> {
    private var _next: List<String>? = null

    override fun hasNext(): Boolean = when (_next) {
      null, EMPTY_STRINGLIST -> {
        _next = readNext()
        (_next != null) && (_next != EMPTY_STRINGLIST)
      }
      else -> true
    }

    override fun next(): List<String> = when (_next) {
      null -> readNext() ?: throw NoSuchElementException("next on empty iterator")
      else -> {
        val tmp = _next
        _next = null
        tmp!!
      }
    }
  }

  fun iteratorWithHeaders(): Iterator<Map<String, String>> {
    return toSequenceWithHeaders().iterator()
  }

  fun toSequenceWithHeaders(): Sequence<Map<String, String>> {
    val headers = readNext() ?: emptyStringList()
    return iterator().asSequence().map { line ->
      headers.zip(line).toMap()
    }
  }

  fun toSequence(): Sequence<List<String>> {
    return generateSequence<List<String>> { readNext() }
  }

  fun all(): List<List<String>> = toSequence().toList()

  fun allWithHeaders(): List<Map<String, String>>
      = allWithOrderedHeaders().component2()

  fun allWithOrderedHeaders(): Pair<List<String>, List<Map<String, String>>> {
    val headers = readNext() ?: emptyStringList()
    log.debug("headers={}", headers?.joinToString())

    val lines = iterator().asSequence().map { line ->
      headers.zip(line).toMap()
    }
    return Pair(headers, lines.toList())
  }

  override fun close(): Unit {
    try {
      lineReader.close()
    } catch(ignored: Exception) {
      log.warn("Fail to close.", ignored)
    }
  }

  companion object {
    @JvmOverloads
    fun open(reader: Reader,
             format: CSVFormat = DEFAULT_CSVFORMAT): CSVReader {
      return CSVReader(ReaderLineReader(reader), format)
    }

    @JvmOverloads
    fun open(file: File,
             cs: Charset = DEFAULT_CHARSET,
             format: CSVFormat = DEFAULT_CSVFORMAT): CSVReader {
      val fs = FileInputStream(file)
      try {
        return open(InputStreamReader(fs, cs), format)
      } catch(e: UnsupportedEncodingException) {
        fs.close()
        throw e
      }
    }

    @JvmOverloads
    fun open(filename: String,
             cs: Charset = DEFAULT_CHARSET,
             format: CSVFormat = DEFAULT_CSVFORMAT): CSVReader {
      return open(File(filename), cs, format)
    }
  }

}