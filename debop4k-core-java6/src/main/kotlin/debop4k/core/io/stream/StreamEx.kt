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

package debop4k.core.io.stream

import debop4k.core.emptyByteArray
import org.slf4j.LoggerFactory
import org.springframework.util.FastByteArrayOutputStream
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

private val log = LoggerFactory.getLogger("streams")

fun emptyInputStream(): InputStream = ByteArrayInputStream(emptyByteArray)

/**
 * [InputStream] 을 읽어 [OutputStream] 에 씁니다.
 */
fun InputStream.copyToOutputStream(output: OutputStream): InputStream {
  val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

  do {
    val read = this.read(buffer, 0, DEFAULT_BUFFER_SIZE)
    if (read > 0) {
      output.write(buffer, 0, read)
    }
  }
  while (read > 0)
  output.flush()

  return this
}

fun InputStream.toOutputStream(): FastByteArrayOutputStream {
  val bos = FastByteArrayOutputStream()
  this.copyToOutputStream(bos)
  return bos
}

fun InputStream.toByteArray(): ByteArray {
  this.toOutputStream().use { bos ->
    return bos.toByteArrayUnsafe()
  }
}

fun InputStream.toString(cs: Charset = Charsets.UTF_8): String
    = this.toByteArray().toString(cs)

/**
 * Input Stream 전체를 읽어 문자열 리스트로 반환합니다.
 */
fun InputStream.readLines(charset: Charset = Charsets.UTF_8): List<String> {
  return this.bufferedReader(charset).readLines()
}

/**
 * Input Stream 전체를 읽어 문자열로 반환합니다.
 */
fun InputStream.readText(cs: Charset = Charsets.UTF_8): String = this.bufferedReader(cs).readText()

fun InputStream.toByteBuffer(): ByteBuffer = ByteBuffer.wrap(this.toByteArray())

fun ByteArray.toInputStream(): InputStream {
  if (this.isEmpty())
    return emptyInputStream()

  return BufferedInputStream(ByteArrayInputStream(this), DEFAULT_BUFFER_SIZE)
}

fun ByteArray.toOutputStream(): FastByteArrayOutputStream {
  val bos = FastByteArrayOutputStream()
  this.toInputStream().use { input -> input.copyToOutputStream(bos) }
  return bos
}

fun ByteArray.readLines(cs: Charset = Charsets.UTF_8): List<String>
    = this.toInputStream().readLines(cs)

fun String.toInputStream(cs: Charset = Charsets.UTF_8): InputStream
    = this.toByteArray(cs).toInputStream()

fun String.toOutputStream(cs: Charset = Charsets.UTF_8): FastByteArrayOutputStream
    = this.toByteArray(cs).toOutputStream()



