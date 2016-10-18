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

@file:JvmName("ZipFilex")

package debop4k.core.compress

import debop4k.core.collections.isNullOrEmpty
import debop4k.core.io.copy
import debop4k.core.io.removeExtension
import debop4k.core.loggerOf
import debop4k.core.utils.EMPTY_STRING
import debop4k.core.utils.Wildcard
import org.slf4j.Logger
import java.io.*
import java.util.zip.*

const val ZIP_EXT = ".zip"
const val GZIP_EXT = ".gz"
const val ZLIB_EXT = ".zlib"

private val log: Logger = loggerOf("ZipFilex")

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

/**
 * Extract zip file to the target directory.
 */
fun unzip(zipFilename: String, destDirName: String, vararg patterns: String): Unit {
  unzip(File(zipFilename), File(destDirName), *patterns)
}

/**
 * Extract zip file to the target directory.
 */
fun unzip(zipFile: File, destDir: File?, vararg patterns: String): Unit {
  val zip = ZipFile(zipFile)
  val entries = zip.entries()

  while (entries.hasMoreElements()) {
    val entry = entries.nextElement()
    val entryName = entry.name

    if (!patterns.isNullOrEmpty) {
      if (Wildcard.matchPathOne(entryName, patterns.asList()) == -1) {
        continue
      }
    }
    val file = File(destDir, entryName)
    if (entry.isDirectory) {
      if (!file.mkdirs()) {
        if (!file.isDirectory) {
          throw IOException("Fail to create directory: $file")
        }
      }
    } else {
      val parent = file.parentFile
      if (parent != null && !parent.exists()) {
        if (!parent.mkdirs()) {
          if (!file.isDirectory) {
            throw IOException("Failed to create directory: $parent")
          }
        }
      }
      zip.getInputStream(entry).use { input ->
        FileOutputStream(file).buffered().use { output ->
          input.copy(output)
        }
      }
    }
  }

  zip.closeSafe()
}

/**
 * Adds single entry to ZIP output stream.
 */
@JvmOverloads
fun addToZip(zos: ZipOutputStream,
             file: File,
             path: String?,
             comment: String? = null,
             recursive: Boolean = true): Unit {
  if (!file.exists()) {
    FileNotFoundException(file.toString())
  }
  var _path: String? = path
  if (_path == null) {
    _path = file.name
  }

  while (_path!!.length != 0 && _path[0] == '/') {
    _path = _path.substring(1)
  }

  val isDir = file.isDirectory

  if (isDir) {
    if (!_path.endsWith("/")) {
      _path += '/'
    }
  }

  val entry = ZipEntry(_path).apply {
    time = file.lastModified()

    if (!comment.isNullOrEmpty()) {
      this.comment = comment!!
    }
    if (isDir) {
      this.size = 0
      this.crc = 0
    }
  }

  zos.putNextEntry(entry)

  if (!isDir) {
    FileInputStream(file).buffered().use { input ->
      input.copy(zos)
    }
  }

  zos.closeEntry()

  if (recursive && isDir) {
    val noRelativePath = _path.isNullOrEmpty()
    val children = file.listFiles()

    if (children != null && children.size > 0) {
      children.forEach { child ->
        val childRelativePath = (if (noRelativePath) EMPTY_STRING else _path) + child.name
        addToZip(zos, child, childRelativePath, comment, recursive)
      }
    }
  }
}

@JvmOverloads
fun addToZip(zos: ZipOutputStream,
             content: ByteArray,
             path: String,
             comment: String? = null): Unit {
  var _path: String = path
  while (!_path.isEmpty() && _path[0] == '/') {
    _path = _path.substring(1)
  }

  // add folder record
  if (_path.endsWith('/')) {
    _path += _path.substring(0, _path.length - 1)
  }

  val zipEntry = ZipEntry(_path).apply {
    time = System.currentTimeMillis()
    if (!comment.isNullOrEmpty()) {
      this.comment = comment
    }
  }
  zos.putNextEntry(zipEntry)

  ByteArrayInputStream(content).buffered().use { input ->
    input.copy(zos)
  }

  zos.closeEntry()
}

@JvmOverloads
fun addFolderToZip(zos: ZipOutputStream,
                   path: String,
                   comment: String? = null): Unit {
  var _path: String = path
  while (!_path.isEmpty() && _path[0] == '/') {
    _path = _path.substring(1)
  }

  // add folder record
  if (!_path.endsWith('/')) {
    _path += '/'
  }

  val entry = ZipEntry(_path).apply {
    time = System.currentTimeMillis()
    if (!comment.isNullOrEmpty()) {
      this.comment = comment
    }
    size = 0
    crc = 0
  }

  zos.putNextEntry(entry)
  zos.closeEntry()
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
