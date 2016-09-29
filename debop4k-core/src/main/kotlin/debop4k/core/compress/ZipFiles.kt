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

@file:JvmName("zipfiles")

package debop4k.core.compress

import debop4k.core.io.copy
import debop4k.core.io.removeExtension
import debop4k.core.loggerOf
import org.slf4j.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.*

const val ZIP_EXT = ".zip"
const val GZIP_EXT = ".gz"
const val ZLIB_EXT = ".zlib"

private val log: Logger = loggerOf("zipFiles")

/**
 * 파일을 압축하여 zlib 파일로 만든다.
 */
fun zlib(filename: String): File = zlib(File(filename))

/**
 * 파일을 압축하여 zlib 파일로 만든다.
 */
fun zlib(file: File): File {
  if (file.isDirectory) {
    throw IOException("Can't zlib folder. file=$file")
  }

  val zlibName = file.absolutePath + ZLIB_EXT
  log.debug("파일을 zlib 압축합니다. 원본={}, 압축={}", file.absolutePath, zlibName)

  val deflater = Deflater(Deflater.BEST_COMPRESSION)
  FileInputStream(file).use { fis ->
    DeflaterOutputStream(FileOutputStream(zlibName), deflater).use { dos ->
      fis.copy(dos)
    }
  }
  return File(zlibName)
}

/**
 * 파일을 압축하여 gzip 파일로 만든다.
 */
fun gzip(filename: String): File = gzip(File(filename))

/**
 * 파일을 압축하여 gzip 파일로 만든다.
 */
fun gzip(file: File): File {
  if (file.isDirectory) {
    throw IOException("Can't gzip folder. file=$file")
  }

  val gzipName = file.absolutePath + GZIP_EXT
  log.debug("파일을 gzip 압축합니다. 원본={}, 압축={}", file.absolutePath, gzipName)

  FileInputStream(file).use { fis ->
    GZIPOutputStream(FileOutputStream(gzipName)).use { gzos ->
      fis.copyTo(gzos)
    }
  }
  return File(gzipName)
}

/**
 * gzip 압축을 풉니다
 */
fun ungzip(filename: String): File = ungzip(File(filename))

/**
 * gzip 압축을 풉니다
 */
fun ungzip(file: File): File {
  log.debug("gzip 파일의 압축을 풉니다. file={}", file.absolutePath)
  val outFilename = removeExtension(file.absolutePath)
  val out = File(outFilename)
  out.createNewFile()

  FileOutputStream(out).use { fos ->
    GZIPInputStream(FileInputStream(file)).use { gzip ->
      gzip.copyTo(fos)
    }
  }
  return out
}

/**
 * 파일을 zip 압축합니다.
 */
fun zip(filename: String): File? = zip(File(filename))

/**
 * 파일을 zip 압축합니다.
 */
fun zip(file: File): File? {
  val zipFilename = file.absolutePath + ZIP_EXT

  return ZipBuilder.of(zipFilename)
             .add(file)?.apply { recursive = true }
             ?.save()
             ?.toZipFile()
         ?: null
}

fun unzip(zipFilename: String, destDirName: String, vararg patterns: String): Unit {
  unzip(File(zipFilename), File(destDirName), *patterns)
}

fun unzip(zipFile: File, destDir: File, vararg patterns: String): Unit {
  TODO()
}

@JvmOverloads
fun addToZip(zos: ZipOutputStream,
             file: File,
             path: String,
             comment: String = "",
             recursive: Boolean = true): Unit {
  TODO()
}

@JvmOverloads
fun addToZip(zos: ZipOutputStream,
             content: ByteArray,
             path: String,
             comment: String = ""): Unit {
  TODO()
}

@JvmOverloads
fun addFolderToZip(zos: ZipOutputStream,
                   path: String,
                   comment: String = ""): Unit {
  TODO()
}

/**
 * [ZipFile] 을 안전하게 닫습니다.
 */
fun ZipFile?.closeSafe(): Unit {
  if (this != null) {
    try {
      close()
    } catch(ignored: IOException) {
      // Ignore exception
    }
  }
}
