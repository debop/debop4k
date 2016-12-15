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
 */
@file:JvmName("Controllerx")

package debop4k.web.utils

import debop4k.core.asyncs.future
import debop4k.core.asyncs.result
import debop4k.core.io.exists
import debop4k.core.io.readAllBytesAsync
import debop4k.core.loggerOf
import debop4k.web.rest.ApiResult
import nl.komponents.kovenant.Promise
import org.springframework.http.*
import org.springframework.web.context.request.async.WebAsyncTask
import java.io.*
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.*
import javax.servlet.http.HttpServletResponse

private val log = loggerOf("Controllerx")

const val DEFAULT_TIMEOUT: Long = 90000L
const val DEFAULT_CONTENT_TYPE: String = "application/octet-stream"

@JvmOverloads
inline fun <T> asyncTask(timeoutInMillis: Long = DEFAULT_TIMEOUT,
                         crossinline func: () -> ResponseEntity<T>): WebAsyncTask<ResponseEntity<T>> {
  return WebAsyncTask(timeoutInMillis, Callable { func.invoke() })
}

inline fun <T> execAsync(crossinline func: () -> T): Promise<ResponseEntity<T>, Exception> {
  return future(Callable {
    success(func)
  })
}

inline fun <T> success(func: () -> T): ResponseEntity<T> {
  return try {
    ResponseEntity.ok(func.invoke())
  } catch(e: Exception) {
    throw RuntimeException("메소드 실행에 예외가 발생했습니다", e)
  }
}

fun <T> serviceUnavailable(): ResponseEntity<T> {
  return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null as T)
}

/**
 * Controller 에서 예외가 발생했을 시에 예외 정보를 {@link ResponseEntity} 로 빌드합니다.
 *
 * @param e 예외 정보
 * @return ResponesEntity 인스턴스
 */
fun handleException(e: Exception?): ResponseEntity<ApiResult> {
  val body = if (e != null) {
    ApiResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.stackTrace)
  } else {
    ApiResult(999, "알 수 없는 예외가 발생했습니다")
  }

  return ResponseEntity(body, HttpStatus.SERVICE_UNAVAILABLE)
}

/**
 * 파일을 클라이언트에 전송합니다.
 * @param response    HttpServletRepsonse 인스턴스
 * @param path        전송할 파일 경로
 * @param contentType 파일 Content Type
 */
fun sendFile(response: HttpServletResponse,
             path: String,
             contentType: MediaType) {
  val content = handleFileSend(response, path, contentType)
  response.outputStream.write(content)
}

/**
 * 파일을 클라이언트에 비동기 방식으로 전송합니다.
 * @param response    HttpServletRepsonse 인스턴스
 * @param path        전송할 파일 경로
 * @param contentType 파일 Content Type
 * @return [Promise] 인스턴스
 */
fun sendFileAsync(response: HttpServletResponse,
                  path: String,
                  contentType: MediaType): Promise<Unit, Exception> {
  val content = handleFileSend(response, path, contentType)

  return future(Callable {
    try {
      response.outputStream.write(content)
    } catch (e: IOException) {
      log.error("파일 전송에 예외가 발생했습니다.", e)
    }
  })
}

/**
 * Http Response 에 파일을 다운로드 하기 위해 파일 내용을 읽어 오고, HttpResponse 의 Header 에 관련 정보를 설정합니다.
 * 파일 크기가 작은 경우에 쓰고, 대용량 파일일 경우는 chunk 를 할 수 있도록 해야 합니다.
 *
 * @param response    Http Response 객체
 * @param path        다운로드할 파일 경로
 * @param contentType 파일의 Content type
 * @return 파일 내용
 */
@JvmOverloads
fun handleFileSend(response: HttpServletResponse,
                   path: String,
                   contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM): ByteArray {
  if (!exists(path))
    throw FileNotFoundException("파일을 찾을 수 없습니다. path=$path")

  val result = readAllBytesAsync(path)

  response.setHeader("Content-Disposition", "attachment: filename=\"${File(path).name}\"")
  response.contentType = contentType.toString()

  val bytes = result.result()
  response.setContentLength(bytes.size)

  return bytes
}

/**
 * [HttpServletResponse] 의 OutputStream 에 지정한 파일 내용을 씁니다.
 *
 * @param file        다운로드할 파일
 * @param contentType 파일의 Content type
 */
@JvmOverloads
fun HttpServletResponse.sendFile(file: File, contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM) {
  if (!file.exists())
    throw FileNotFoundException("파일을 찾을 수 없습니다. file=$file")

  this.setHeader("Content-Disposition", "attachment: filename=${file.name}")
  this.contentType = contentType.toString()

  val length = file.length()
  val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

  file.inputStream().buffered().use {
    do {
      val read = it.read(buffer)
      if (read > 0)
        this.outputStream.write(buffer, 0, read)
    } while (read > 0)
  }
}
