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

package debop4k.core.compress

import debop4k.core.closeSafe
import debop4k.core.io.touch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.*

/**
 * ZIP builder class for building both files or in-memory zips
 *
 * @author sunghyouk.bae@gmail.com
 */
class ZipBuilder @JvmOverloads constructor(val targetZipFile: File? = null) {

  companion object {
    @JvmStatic fun of(zipFile: File): ZipBuilder = ZipBuilder(zipFile)
    @JvmStatic fun of(zipFilename: String): ZipBuilder = of(File(zipFilename))
    @JvmStatic fun ofInMemory(): ZipBuilder = ZipBuilder(null)
  }

  private val zos: ZipOutputStream
  private val targetBos: ByteArrayOutputStream?

  init {
    if (targetZipFile == null) {
      targetBos = ByteArrayOutputStream()
      zos = ZipOutputStream(targetBos)
    } else {

      if (targetZipFile.exists()) {
        targetZipFile.touch()
      }
      targetBos = null
      zos = ZipOutputStream(FileOutputStream(targetZipFile))
    }
  }

  fun toZipFile(): File? {
    zos.closeSafe()
    return targetZipFile
  }

  fun toBytes(): ByteArray {
    zos.closeSafe()

    return try {
      targetZipFile?.readBytes() ?:
      targetBos?.toByteArray() ?:
      byteArrayOf()
    } catch(e: Throwable) {
      byteArrayOf()
    }
  }

  fun add(source: File): AddFileToZip? = AddFileToZip(this, source)
  fun add(content: String): AddContentToZip = AddContentToZip(this, content.toUtf8Bytes())
  fun add(content: ByteArray): AddContentToZip? = AddContentToZip(this, content)
  fun addFolder(folderName: String): ZipBuilder {
    addFolderToZip(zos, folderName)
    return this
  }

  class AddFileToZip(val zipBuilder: ZipBuilder, val file: File) {
    var path: String = ""
    var comment: String = ""
    var recursive: Boolean = true

    fun save(): ZipBuilder {
      addToZip(zipBuilder.zos, file, path, comment, recursive)
      return zipBuilder
    }
  }

  class AddContentToZip(val zipBuilder: ZipBuilder, val content: ByteArray) {
    var path: String = ""
    var comment: String = ""

    fun save(): ZipBuilder {
      addToZip(zipBuilder.zos, content, path, comment)
      return zipBuilder
    }
  }
}