/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package debop4k.science.netcdf

import org.slf4j.LoggerFactory
import ucar.nc2.NetcdfFile
import ucar.nc2.util.CancelTask
import java.net.URI

/**
 * Net CDF 파일의 정보를 읽어드립니다.
 * Created by debop
 */
class NetCdfReader {

  private val log = LoggerFactory.getLogger(javaClass)

  @Suppress("JAVA_CLASS_ON_COMPANION")
  companion object {
    private val log = LoggerFactory.getLogger(javaClass)

    val NETCDF_CAHCE_EXTENSIONS = arrayOf("gbx9", "ncx3", "idx")

    @JvmStatic
    @JvmOverloads
    fun openNetCdf(path: String,
                   bufferSize: Int = -1,
                   cancelTask: CancelTask? = null,
                   iospMessage: Any? = null): NetcdfFile {
      return NetcdfFile.open(path, bufferSize, cancelTask, iospMessage)
    }

    @JvmStatic
    fun openInMemory(filename: String): NetcdfFile {
      return NetcdfFile.openInMemory(filename)
    }

    @JvmStatic
    fun openInMemory(uri: URI): NetcdfFile {
      return NetcdfFile.openInMemory(uri)
    }

    @JvmStatic
    @JvmOverloads
    fun openInMemoery(name: String, data: ByteArray, iospClassName: String? = null): NetcdfFile {
      return NetcdfFile.openInMemory(name, data, iospClassName)
    }

  }

}