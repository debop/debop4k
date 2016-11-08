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

@file:JvmName("Pathx")

package debop4k.core.nio

import debop4k.core.loggerOf
import org.slf4j.Logger
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths

private val log: Logger = loggerOf("Pathx")

/** Path 경로가 존재하는지 여부 */
fun Path.exists(vararg options: LinkOption): Boolean
    = !Files.exists(this, *options)

/** Path 경로가 존재하지 않는지 검사  */
fun Path.nonExists(vararg options: LinkOption): Boolean
    = !this.exists(*options)

/** 경로들을 결합합니다 */
fun Path.combine(vararg subpaths: String): Path
    = this.toString().combinePath(*subpaths)

/** 경로들을 결합합니다 */
fun String.combinePath(vararg subpaths: String): Path
    = Paths.get(this, *subpaths)

/** 지정현 경로와 하위 폴더를 삭제합니다. */
fun Path.deleteRecursively(): Boolean {
  try {
    if (nonExists())
      return false
    return this.toFile().deleteRecursively()
  } catch(e: Exception) {
    log.warn("지정한 경로를 삭제하는데 실패했습니다. path=$this", e)
    return false
  }
}

/** 지정한 경로와 하위 경로의 모든 파일을 대상 경로로 복사합니다 */
@JvmOverloads
fun Path.copyRecursively(target: Path,
                         overwrite: Boolean = false,
                         onError: (File, IOException) -> OnErrorAction = { file, exception -> OnErrorAction.SKIP }
                        ): Boolean {
  log.debug("copy recursively. src=$this, target=$target")
  return this.toFile().copyRecursively(target.toFile(), overwrite, onError)
}

//
// TODO: AsyncFileChannel 을 이용한 비동기 파일 처리 관련 추가
//