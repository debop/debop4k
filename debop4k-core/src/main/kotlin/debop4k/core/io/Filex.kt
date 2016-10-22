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
@file:JvmName("Filex")

package debop4k.core.io

import com.google.common.io.Files
import debop4k.core.collections.emptyByteArray
import debop4k.core.loggerOf
import debop4k.core.utils.max
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.apache.commons.io.FileUtils
import org.eclipse.collections.impl.list.mutable.FastList
import java.io.*
import java.nio.charset.Charset
import kotlin.concurrent.thread

private val log = loggerOf("Filex")

const val EXTENSION_SEPARATOR = '.'
const val UNIX_SEPARATOR = '/'
const val WINDOWS_SEPARATOR = '\\'

@JvmField val SYSTEM_SEPARATOR = File.separatorChar

/**
 * 디렉토리 생성
 * NOTE: 여러 Depth의 directory 라면 분해해서 재귀호출로 생성해야 한다
 */
fun createDirectory(dir: String): File? {
  log.trace("create directory. dir={}", dir)
  try {
    val file = File(dir)
    val created = file.mkdirs()

    return if (created) file else null
  } catch(e: Exception) {
    log.error("디렉토리 생성에 실패했습니다. dir=$dir")
    return null
  }
}

/** 파일 생성 */
fun createFile(path: String): File {
  log.trace("create file. path={}", path)

  val file = File(path)
  Files.createParentDirs(file)
  file.createNewFile()
  return file
}

/** 임시 디렉토리 생성 */
@JvmOverloads
fun createTempDirectory(deleteAtExit: Boolean = true): File {
  val dir = File.createTempFile("temp", "dir")
  deleteDirectory(dir.absolutePath)
  dir.mkdirs()

  if (deleteAtExit) {
    // HINT: shutdown hook 를 추가한다
    Runtime.getRuntime().addShutdownHook(thread(start = false) {
      deleteDirectory(dir.absolutePath)
    })
  }
  return dir
}

/** 파일 복사 */
fun copy(src: File, dest: File): File = src.copyTo(dest, true)

/** 비동기 파일 복사 */
fun copyAsync(src: File, dest: File): Promise<File, Exception> = task { copy(src, dest) }

/** 파일 이동 */
fun move(src: File, dest: File): Unit = FileUtils.moveFile(src, dest)

/** 비동기 파일 이동 */
fun moveAsync(src: File, dest: File): Promise<Unit, Exception> = task { move(src, dest) }

/** 파일 삭제 */
fun delete(file: File): Unit {
  log.debug("delete file... file={}", file)
  FileUtils.forceDelete(file)
}

/** 파일이 존재할 때 파일 삭제 */
fun deleteIfExists(path: String): Unit {
  log.debug("delete file... path={}", path)
  val file = File(path)
  if (file.exists()) {
    FileUtils.deleteQuietly(file)
  }
}

/** 디렉토리 및 하위 디렉토리 전체를 삭제 */
fun deleteDirectoryRecursively(dir: String): Boolean {
  val file = File(dir)

  try {
    return file.deleteRecursively()
  } catch(e: Exception) {
    log.warn("Directory를 삭제하는데 실패했습니다. dir=$dir", e)
    return false
  }
}

/** 디렉토리 삭제 */
@JvmOverloads
fun deleteDirectory(dir: String, recursive: Boolean = true): Unit {
  if (!recursive)
    FileUtils.deleteDirectory(File(dir))
  else
    deleteDirectoryRecursively(dir)
}

/** 비동기 방식으로 디렉토리 삭제 */
@JvmOverloads
fun deleteDirectoryAsync(dir: String, recursive: Boolean = true): Promise<Unit, Exception> = task {
  deleteDirectory(dir, recursive)
}

/** 파일 존재 여부 */
fun exists(path: String): Boolean = File(path).exists()

/**
 * Unit "touch" utility를 구현한 함수입니다.
 * 파일이 존재하지 않는 경우 크기가 0 인 파일을 새로 만듭니다.
 */
fun File.touch() {
  if (!this.exists()) {
    FileOutputStream(this).close()
  }
  setLastModified(System.currentTimeMillis())
}

fun removeExtension(filename: String): String {
  val index = indexOfExtension(filename)

  return if (index >= 0) filename.substring(0, index)
  else filename
}

fun indexOfExtension(filename: String): Int {
  val extensionPos = filename.lastIndexOf('.')
  val lastSeparator = indexOfLastSeparator(filename)
  return if (lastSeparator > extensionPos) -1 else extensionPos
}

fun indexOfLastSeparator(filename: String): Int {
  val lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR)
  val lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR)
  return lastUnixPos max lastWindowsPos
}

/** 파일을 읽어 바이트 배열로 반환한다 */
fun readAllBytes(path: String): ByteArray {
  if (path.isNullOrBlank() || !exists(path))
    return emptyByteArray

  return FileUtils.readFileToByteArray(File(path))
}

/** 비동기 방식으로 파일을 읽어 바이트 배열로 반환한다 */
fun readAllBytesAsync(path: String): Promise<ByteArray, Exception> = task { readAllBytes(path) }


/** TEXT 파일을 읽어 [FastList<String>] 으로 반환 */
@JvmOverloads
fun readAllLines(path: String, cs: Charset = Charsets.UTF_8): FastList<String>
    = BufferedInputStream(FileInputStream(path)).toStringList(cs)

/** [InputStream]을 읽어 [FastList<String>] 으로 반환 */
@JvmOverloads
fun readAllLines(input: InputStream, cs: Charset = Charsets.UTF_8): FastList<String>
    = input.toStringList(cs)

/** 바이트 배열을 읽어 [FastList<String>] 으로 반환 */
@JvmOverloads
fun readAllLines(input: ByteArray, cs: Charset = Charsets.UTF_8): FastList<String>
    = readAllLines(input.toInputStream(), cs)

/** 비동기 방식으로 TEXT 파일을 읽어 [FastList<String>] 으로 반환 */
@JvmOverloads
fun readAllLinesAsync(path: String, cs: Charset = Charsets.UTF_8): Promise<FastList<String>, Exception>
    = task { readAllLines(path, cs) }

/** 비동기 방식으로 [InputStream]을 읽어 [FastList<String>] 으로 반환 */
@JvmOverloads
fun readAllLinesAsync(input: InputStream, cs: Charset = Charsets.UTF_8): Promise<FastList<String>, Exception>
    = task { readAllLines(input, cs) }

/** 비동기 방식으로 바이트 배열을 읽어 [FastList<String>] 으로 반환 */
@JvmOverloads
fun readAllLinesAsync(input: ByteArray, cs: Charset = Charsets.UTF_8): Promise<FastList<String>, Exception>
    = task { readAllLines(input, cs) }

/** 지정한 파일에 바이트 배열을 씁니다 */
@JvmOverloads
fun write(file: File, data: ByteArray, append: Boolean = false): Unit {
  FileUtils.writeByteArrayToFile(file, data, append)
}

/** 지정한 파일에 문자열 컬렉션을 씁니다 */
@JvmOverloads
fun writeLines(file: File, lines: Collection<String>, append: Boolean = false, cs: Charset = Charsets.UTF_8): Unit {
  FileUtils.writeLines(file, cs.name(), lines, append)
}

/** 비동기 방식으로 지정한 파일에 바이트 배열을 씁니다 */
@JvmOverloads
fun writeAsync(file: File, data: ByteArray, append: Boolean = false): Promise<Unit, Exception>
    = task { FileUtils.writeByteArrayToFile(file, data, append) }

/** 비동기 방식으로 지정한 파일에 문자열 컬렉션을 씁니다 */
@JvmOverloads
fun writeLinesAsyncs(file: File,
                     lines: Collection<String>,
                     append: Boolean = false,
                     cs: Charset = Charsets.UTF_8): Promise<Unit, Exception>
    = task { FileUtils.writeLines(file, cs.name(), lines, append) }

/** 지정한 경로의 파일에 대해 [BufferedReader] 를 생성합니다. */
@JvmOverloads
fun newBufferedReader(path: String,
                      cs: Charset = Charsets.UTF_8,
                      bufferSize: Int = DEFAULT_BUFFER_SIZE): BufferedReader
    = InputStreamReader(FileInputStream(path), cs).buffered(bufferSize)

/** 지정한 경로의 파일에 대해 [BufferedWriter] 를 생성합니다. */
@JvmOverloads
fun newBufferedWriter(path: String,
                      cs: Charset = Charsets.UTF_8,
                      bufferSize: Int = DEFAULT_BUFFER_SIZE): BufferedWriter
    = OutputStreamWriter(FileOutputStream(path), cs).buffered(bufferSize)
