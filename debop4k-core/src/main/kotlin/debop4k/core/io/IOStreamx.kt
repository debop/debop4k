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
@file:JvmName("IOStreamx")

package debop4k.core.io

import debop4k.core.collections.*
import debop4k.core.loggerOf
import debop4k.core.utils.EMPTY_STRING
import org.eclipse.collections.impl.list.mutable.FastList
import org.springframework.util.FastByteArrayOutputStream
import java.io.*
import java.nio.ByteBuffer
import java.nio.charset.Charset

private val log = loggerOf("IOStreamx")

const val DEFAULT_BLOCK_SIZE = 1024 * 8

/** Empty [InputStream] */
val emptyInputStream: InputStream
  get() = ByteArrayInputStream(emptyByteArray)

/** Empty [OutputStream] */
val emptyOutputStream: FastByteArrayOutputStream
  get() = FastByteArrayOutputStream()

@JvmOverloads
fun fastByteArrayOutputStreamOf(blockSize: Int = DEFAULT_BLOCK_SIZE): FastByteArrayOutputStream
    = FastByteArrayOutputStream(blockSize)

/**
 * [InputStream]의 정보를 읽어, [OutputStream]에 씁니다.
 */
@JvmOverloads
fun InputStream.copy(output: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
  val buffer = ByteArray(bufferSize)
  var readBytes = 0L

  do {
    val bytes = this.read(buffer, 0, bufferSize)
    if (bytes > 0) {
      output.write(buffer, 0, bytes)
      readBytes += bytes
    }
  } while (bytes > 0)

//  output.flush()
  return readBytes
}

@JvmOverloads
fun InputStream.copy(writer: Writer, cs: Charset = Charsets.UTF_8): Long {
  return this.reader(cs).copy(writer)
}

/**
 * Copy [Reader] data to [Writer]
 */
fun Reader.copy(writer: Writer): Long {
  val buffer = CharArray(DEFAULT_BUFFER_SIZE)
  var readChars = 0L

  do {
    val chars = this.read(buffer, 0, DEFAULT_BUFFER_SIZE)
    writer.write(buffer, 0, chars)
    readChars += chars
  } while (chars > 0)

  return readChars
}

@JvmOverloads
fun Reader.copy(output: OutputStream, cs: Charset = Charsets.UTF_8): Long {
  val writer = OutputStreamWriter(output, cs)
  val count = copy(writer)
  output.flush()
  return count
}


/** [ByteArray] 를 [InputStream]으로 빌드합니다 */
fun ByteArray?.toInputStream(): InputStream {
  if (this.isNullOrEmpty)
    return emptyInputStream

  return BufferedInputStream(ByteArrayInputStream(this!!))
}

/** 문자열을 [InputStream]으로 빌드합니다 */
@JvmOverloads
fun String?.toInputStream(cs: Charset = Charsets.UTF_8): InputStream {
  if (this.isNullOrBlank())
    return emptyInputStream

  return this!!.toByteArray(cs).toInputStream()
}

/** [InputStream]의 정보를 읽어 [OutputStream]으로 빌드합니다 */
@JvmOverloads
fun InputStream?.toOutputStream(blockSize: Int = DEFAULT_BLOCK_SIZE): FastByteArrayOutputStream {
  if (this == null)
    return emptyOutputStream

  val output = fastByteArrayOutputStreamOf(blockSize)
  this.copy(output)
  return output
}

/** 바이트 배열을 [OutputStream]으로 빌드합니다 */
@JvmOverloads
fun ByteArray?.toOutputStream(blockSize: Int = DEFAULT_BLOCK_SIZE): FastByteArrayOutputStream {
  return this?.toInputStream()?.use { it.toOutputStream(blockSize) } ?: emptyOutputStream
}

/** 문자열을 [OutputStream]으로 빌드합니다 */
@JvmOverloads
fun String?.toOutputStream(blockSize: Int = DEFAULT_BLOCK_SIZE,
                           cs: Charset = Charsets.UTF_8): FastByteArrayOutputStream {
  return this?.toByteArray(cs)?.toOutputStream(blockSize) ?: emptyOutputStream
}

fun InputStream?.availableBytes(): ByteArray {
  if (this == null)
    return emptyByteArray

  val result = ByteArray(available())
  read(result)
  return result
}

/** [InputStream]을 읽어 바이트배열을 만듭니다 */
fun InputStream?.toByteArray(): ByteArray {
  return this?.toOutputStream()?.use { it.toByteArrayUnsafe() } ?: emptyByteArray
}

/** [InputStream]을 읽어 [ByteBuffer]를 빌드합니다 */
fun InputStream.toByteBuffer(): ByteBuffer = ByteBuffer.wrap(this.toByteArray())

/** [InputStream]을 읽어 문자열로 빌드합니다 */
@JvmOverloads
fun InputStream?.toString(cs: Charset = Charsets.UTF_8): String {
  return this?.toByteArray()?.toString(cs) ?: EMPTY_STRING
}

/**
 * NOTE: 되도록 toLineSequence 보다는 [toStringList] 를 사용하세요.
 */
@JvmOverloads
fun InputStream?.toLineSequence(cs: Charset = Charsets.UTF_8): Sequence<String> {
  return this?.bufferedReader(cs)?.lineSequence() ?: emptySequence()
}

/** [InputStream]을 읽어 문자열 컬렉션으로 빌드합니다 */
@JvmOverloads
fun InputStream?.toStringList(cs: Charset = Charsets.UTF_8): FastList<String> {
  return this?.bufferedReader(cs)?.useLines { it.toFastList() } ?: fastListOf()
}

/**
 * NOTE: 되도록 toLineSequence 보다는 [toStringList] 를 사용하세요.
 */
@JvmOverloads
fun ByteArray?.toLineSequence(cs: Charset = Charsets.UTF_8): Sequence<String> {
  return this?.toInputStream()?.toLineSequence(cs) ?: emptySequence()
}

/**
 * 바이트 배열을 문자열 컬렉션으로 빌드합니다
 * NOTE: 되도록 toLineSequence 보다는 [toStringList] 를 사용하세요.
 */
@JvmOverloads
fun ByteArray?.toStringList(cs: Charset = Charsets.UTF_8): FastList<String> {
  return this?.toInputStream()?.use { it.toStringList(cs) } ?: fastListOf()
}

