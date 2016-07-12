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

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.io.StringReader
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.*
import kotlin.test.assertEquals

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class CSVReaderTest {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test fun constructorWithFile() {
    val res = mutableListOf<String>()
    CSVReader.open(File("src/test/resources/simple.csv")).use { reader ->
      reader.forEach { fields ->
        res.addAll(fields)
      }
    }
    assertEquals("abcdef", res.joinToString(separator = "") { it })
    println(res.joinToString(separator = ""))
  }

  @Test fun constructorWithFilename() {
    val res = mutableListOf<String>()
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      reader.forEach { fields ->
        res.addAll(fields)
      }
    }
    assertEquals("abcdef", res.joinToString(separator = "") { it })
    println(res.joinToString(separator = ""))
  }

  @Test fun constructedWithCSVFormat() {
    val format = object : DefaultCSVFormat() {
      override val delimiter: Char = '#'
      override val quoteChar: Char = '$'
    }

    CSVReader.open("src/test/resources/hash-separated-dollar-quote.csv",
                   format = format).use { reader ->
      val map: List<Map<String, String>> = reader.allWithHeaders()
      assertThat(map).isNotEmpty()
      assertThat(map[0]["Foo "]).isEqualTo("a")
    }

    CSVReader.open("src/test/resources/hash-separated-dollar-quote.csv",
                   cs = Charsets.UTF_8,
                   format = format).use { reader ->
      val map: List<Map<String, String>> = reader.allWithHeaders()
      assertThat(map).isNotEmpty()
      assertThat(map[0]["Foo "]).isEqualTo("a")
    }
  }

  @Test fun shouldThrowUnsupportedEncodingException() {
    assertThatThrownBy {
      CSVReader.open("src/test/resources/hash-separated-dollor-quote.csv", cs = Charset.forName("unknown")).use { reader ->
        val map = reader.allWithHeaders()
        assertThat(map[0]["Foo "]).isEqualTo("a")
      }
    }.isInstanceOf(UnsupportedCharsetException::class.java)
  }

  @Test fun cnaReadEmptyLine() {
    CSVReader.open("src/test/resources/has-empty-line.csv").use { reader ->
      val lines = reader.all()
      assertThat(lines[1]).isEqualTo(listOf(""))
    }

    val format = object : DefaultCSVFormat() {
      override val treatEmptyLineAsNull = true
    }
    CSVReader.open("src/test/resources/has-empty-line.csv", format = format).use { reader ->
      val lines = reader.all()
      assertThat(lines[1]).isEqualTo(listOf<String>())
    }
  }

  @Test fun readSimpleCSVFile() {
    var res: List<String> = mutableListOf<String>()
    CSVReader.open(FileReader("src/test/resources/simple.csv")).use { reader ->
      reader.forEach { fields ->
        res += fields
      }
    }
    assertThat(res.joinToString(separator = "")).isEqualTo("abcdef")
  }

  @Test fun readSimpleCSVString() {
    val csvString = "a,b,c\nd,e,f\n"
    var res: List<String> = mutableListOf<String>()
    CSVReader.open(StringReader(csvString)).use { reader ->
      reader.forEach { fields ->
        res += fields
      }
    }
    assertThat(res.joinToString(separator = "")).isEqualTo("abcdef")
  }

  @Test fun testIssue22() {
    var res: List<String> = mutableListOf<String>()
    CSVReader.open(FileReader("src/test/resources/issue22.csv")).use { reader ->
      reader.forEach { fields ->
        res += fields
      }
    }
  }

  @Test fun testIssue32() {
    var res: List<String> = mutableListOf<String>()
    val format = object : DefaultCSVFormat() {
      override val escapeChar: Char = '\\'
    }
    CSVReader.open(FileReader("src/test/resources/issue32.csv"), format = format).use { reader ->
      reader.forEach { fields ->
        res += fields
      }
    }
  }

  @Test fun readCSVWithEscapeCharIsBackSlash() {
    var res: List<String> = mutableListOf<String>()
    val format = object : DefaultCSVFormat() {
      override val escapeChar: Char = '\\'
    }
    CSVReader.open(FileReader("src/test/resources/backslash-escape.csv"),
                   format = format).use { reader ->
      reader.forEach { fields ->
        println("fields=$fields")
        res += fields
      }
    }
    assertThat(res).isEqualTo(listOf("field1", "field2", "field3 says, \"escaped with backslash\""))
  }

  @Test fun readSimpleCSVWithEmptyQuotedFields() {
    var res: List<String> = mutableListOf<String>()

    CSVReader.open(FileReader("src/test/resources/issue30.csv")).use { reader ->
      reader.forEach { fields ->
        println("fields=$fields")
        res += fields
      }
    }
    assertThat(res.joinToString(",")).isEqualTo("h1,h2,h3,a1,,a3,b1,b2,b3")
  }

  @Test fun readFileStaringWithBOM() {
    var res: List<String> = mutableListOf<String>()
    CSVReader.open("src/test/resources/bom.csv").use { reader ->
      reader.forEach { fields ->
        res += fields
      }
    }
    assertThat(res).isEqualTo(listOf("a", "b", "c"))
  }

  @Test fun malformedInput() {
    assertThatThrownBy {
      CSVReader.open("src/test/resources/malformed.csv").use { reader ->
        reader.all()
      }
    }.isInstanceOf(MalformedCSVException::class.java)
  }

  @Test fun readCSVIncludingEscapedFields() {
    var res: List<String> = mutableListOf<String>()
    CSVReader.open("src/test/resources/escape.csv").use { reader ->
      reader.forEach { fields ->
        res += fields
      }
    }
    assertThat(res.joinToString(separator = "")).isEqualTo("""abcd"ef""")
  }

  @Test fun correctlyParseFieldsWithLineBreaksEnclosedInDoubleQuotes() {
    var res = mutableListOf<List<String>>()
    CSVReader.open("src/test/resources/line-breaks.csv").use { reader ->
      reader.forEach { fields ->
        println("fields=$fields")
        res.add(fields)
      }
    }
    assertThat(res[0]).isEqualTo(listOf("a", "b\nb", "c"))
    assertThat(res[1]).isEqualTo(listOf("\nd", "e", "f"))
  }

  @Test fun readTSVFile() {
    var res = mutableListOf<List<String>>()
    CSVReader.open("src/test/resources/simple.tsv", format = TSVFormat()).use { reader ->
      reader.forEach { fields ->
        res.add(fields)
      }
    }
    assertThat(res[0]).isEqualTo(listOf("a", "b", "c"))
    assertThat(res[1]).isEqualTo(listOf("d", "e", "f"))
  }

  @Test fun testToSequence() {
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      val sequence = reader.toSequence()
      assertThat(sequence.drop(1).first().joinToString(separator = "")).isEqualTo("def")
    }
  }

  @Test fun readNext() {
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      reader.readNext()
      assertThat(reader.readNext()?.joinToString(separator = "")).isEqualTo("def")
    }
  }

  @Test fun hasAll() {
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      assertThat(reader.all()).isEqualTo(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun hasNext() {
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      val it = reader.iterator()
      assertThat(it.hasNext()).isTrue()
      assertThat(it.hasNext()).isTrue()
      assertThat(it.hasNext()).isTrue()
      it.forEach { x -> }
      assertThat(it.hasNext()).isFalse()
    }
  }

  @Test fun shouldReturnTheNextLine() {
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      val it = reader.iterator()
      assertThat(it.next()).isEqualTo(listOf("a", "b", "c"))
      assertThat(it.next()).isEqualTo(listOf("d", "e", "f"))
    }
  }

  @Test fun shouldThrowNoSuchElementException() {

    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      val it = reader.iterator()
      it.next()
      it.next()
      assertThatThrownBy {
        it.next()
      }.isInstanceOf(NoSuchElementException::class.java)
    }
  }

  @Test fun iterateAllLines() {
    var lineCount = 0
    CSVReader.open("src/test/resources/simple.csv").use { reader ->
      val it = reader.iterator()
      it.forEach { line -> lineCount++ }
    }
    assertThat(lineCount).isEqualTo(2)
  }

  @Test fun parseHugeFile() {
    val lineCount = 100000
    val tmpfile = File.createTempFile("csv", "test")
    tmpfile.deleteOnExit()
    PrintWriter(tmpfile).use { writer ->
      (1 .. lineCount).forEach { i -> writer.println(i) }
    }
    var count = 0
    CSVReader.open(tmpfile).use { reader ->
      reader.forEach { row -> count++ }
    }
    assertThat(count).isEqualTo(lineCount)
  }

  @Test fun iteratorWithHeadersWithEmpty() {
    CSVReader.open("src/test/resources/empty.csv").use { reader ->
      assertThat(reader.iteratorWithHeaders()).isEmpty()
    }
  }

  @Test fun iteratorWithHeadersOnlyOneLine() {
    CSVReader.open("src/test/resources/only-header.csv").use { reader ->
      assertThat(reader.iteratorWithHeaders()).isEmpty()
    }
  }

  @Test fun iteratorWithHeadersWithManyLines() {
    CSVReader.open("src/test/resources/with-headers.csv").use { reader ->
      val iterator = reader.iteratorWithHeaders()
      assertThat(iterator.next()).isEqualTo(mapOf("Foo" to "a", "Bar" to "b", "Baz" to "c"))
      assertThat(iterator.next()).isEqualTo(mapOf("Foo" to "d", "Bar" to "e", "Baz" to "f"))
    }
  }

  @Test fun allHeadersWithEmpty() {
    CSVReader.open("src/test/resources/empty.csv").use { reader ->
      assertThat(reader.allWithHeaders()).isEmpty()
    }
  }

  @Test fun allHeadersOnlyOneLine() {
    CSVReader.open("src/test/resources/only-header.csv").use { reader ->
      assertThat(reader.allWithHeaders()).isEmpty()
    }
  }

  @Test fun allHeadersWithManyLines() {
    CSVReader.open("src/test/resources/with-headers.csv").use { reader ->
      val list = reader.allWithHeaders()
      assertThat(list[0]).isEqualTo(mapOf("Foo" to "a", "Bar" to "b", "Baz" to "c"))
      assertThat(list[1]).isEqualTo(mapOf("Foo" to "d", "Bar" to "e", "Baz" to "f"))
    }
  }

  @Test fun allOrderedHeadersWithEmpty() {
    CSVReader.open("src/test/resources/empty.csv").use { reader ->
      assertThat(reader.allWithOrderedHeaders()).isEqualTo(Pair(listOf<String>(), listOf<Map<String, String>>()))
    }
  }

  @Test fun allOrderedHeadersOnlyOneLine() {
    CSVReader.open("src/test/resources/only-header.csv").use { reader ->
      assertThat(reader.allWithOrderedHeaders()).isEqualTo(Pair(listOf("foo", "bar"), listOf<Map<String, String>>()))
    }
  }

  @Test fun allOrderedHeadersWithManyLines() {
    CSVReader.open("src/test/resources/with-headers.csv").use { reader ->
      val pair = reader.allWithOrderedHeaders()

      assertThat(pair.first).isEqualTo(listOf("Foo", "Bar", "Baz"))
      assertThat(pair.second[0]).isEqualTo(mapOf("Foo" to "a", "Bar" to "b", "Baz" to "c"))
      assertThat(pair.second[1]).isEqualTo(mapOf("Foo" to "d", "Bar" to "e", "Baz" to "f"))
    }
  }
}