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

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.AfterClass
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


class CSVWriterTest {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    @AfterClass
    @JvmStatic fun cleanUp() {
      Files.deleteIfExists(Paths.get("test.csv"))
    }
  }

  fun String.readFileAsString(): String {
    return File(this).readLines().joinToString(separator = "\n")
  }

  @Test fun testOpenWithOutputStream() {
    CSVWriter.open(FileOutputStream("test.csv")).use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun testOpenWithOutputStreamAndCharsets() {
    CSVWriter.open(FileOutputStream("test.csv"), cs = Charsets.UTF_8).use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun testOpenWithFile() {
    CSVWriter.open(File("test.csv")).use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun testOpenWithFileAndCharsets() {
    CSVWriter.open(File("test.csv"), cs = Charsets.UTF_8).use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun testOpenWithFilename() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun testOpenWithFilenameAndCharsets() {
    CSVWriter.open("test.csv", cs = Charsets.UTF_8).use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun testAppendWithFilenameAndCharsets() {
    CSVWriter.open("test.csv", append = true, cs = Charsets.UTF_8).use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
  }

  @Test fun writeAllLineToFile() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }
    val expected = """
        |a,b,c
        |d,e,f
        """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)


    val writer = CSVWriter.open("test.csv")
    writer.close()
    assertThatThrownBy {
      writer.writeAll(listOf(listOf("a", "b", "c"), listOf("d", "e", "f")))
    }.isInstanceOf(IOException::class.java)

  }

  @Test fun testWriteNext() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeRow(listOf("a", "b", "c"))
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected = """
        |a,b,c
        |d,e,f
        """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)

  }

  @Test fun writeSingleLineWithNullFieldstoFile() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeRow(listOf("a", null, "c"))
      writer.writeRow(listOf("d", "e", null))
    }
    val expected = """
        |a,,c
        |d,e,
        """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun whenAFieldContainsQuoteCharInIt() {
    CSVWriter.open(FileWriter("test.csv")).use { writer ->
      writer.writeRow(listOf("a", "b\"", "c"))
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected = "a,\"b\"\"\",c\nd,e,f"

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun whenFieldContainsDelimiterInIt() {
    CSVWriter.open(FileWriter("test.csv")).use { writer ->
      writer.writeRow(listOf("a", "b,", "c"))
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected = "a,\"b,\",c\nd,e,f"

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun whenQuotingIsSetToQUOTE_ALL() {
    val quoteAllFormat = object : DefaultCSVFormat() {
      override val quoting: Quoting = QUOTE_ALL
    }

    CSVWriter.open(FileWriter("test.csv"), format = quoteAllFormat).use { writer ->
      writer.writeRow(listOf("a", "b", "c"))
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected = """
      |"a","b","c"
      |"d","e","f"
      """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun whenQuotingIsSetToQUOTE_NONE() {
    val quoteAllFormat = object : DefaultCSVFormat() {
      override val quoting: Quoting = QUOTE_NONE
    }

    CSVWriter.open(FileWriter("test.csv"), format = quoteAllFormat).use { writer ->
      writer.writeRow(listOf("a", "b", "c"))
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected = """
        |a,b,c
        |d,e,f
        """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun whenQuotingIsSetToQUOTE_NONNUMERIC() {
    val quoteAllFormat = object : DefaultCSVFormat() {
      override val quoting: Quoting = QUOTE_NONNUMERIC
    }

    CSVWriter.open(FileWriter("test.csv"), format = quoteAllFormat).use { writer ->
      writer.writeRow(listOf("a", "b", "1"))
      writer.writeRow(listOf("2.0", "e", "f"))
    }
    val expected = """
        |"a","b",1
        |2.0,"e","f"
        """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun flushFile() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeRow(listOf("a", "b", "c"))
      writer.flush()
      val content = CSVReader.open("test.csv").use { reader ->
        reader.all()
      }
      assertThat(content).isEqualTo(listOf(listOf("a", "b", "c")))
    }
  }

  @Test fun whenAppendTrue() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeRow(listOf("a", "b", "c"))
    }
    CSVWriter.open("test.csv", append = true).use { writer ->
      writer.writeRow(listOf("d", "e", "f"))
    }
    CSVWriter.open("test.csv", append = true).use { writer ->
      writer.writeRow(listOf("h", "i", "j"))
    }

    val expected = """
        |a,b,c
        |d,e,f
        |h,i,j
        """.trimMargin()

    assertThat("test.csv".readFileAsString()).isEqualTo(expected)
  }

  @Test fun whenAppendFalse() {
    CSVWriter.open("test.csv").use { writer ->
      writer.writeRow(listOf("a", "b", "c"))
    }
    CSVWriter.open("test.csv", append = false).use { writer ->
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected = "d,e,f"
    assertThat("test.csv".readFileAsString()).isEqualTo(expected)


    CSVWriter.open("test.csv", append = false).use { writer ->
      writer.writeRow(listOf("d", "e", "f"))
    }
    val expected2 = "d,e,f"
    assertThat("test.csv".readFileAsString()).isEqualTo(expected2)
  }
}