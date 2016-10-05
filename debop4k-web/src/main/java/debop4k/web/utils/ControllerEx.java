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

package debop4k.web.utils;

import debop4k.core.asyncs.Asyncx;
import debop4k.core.io.Filex;
import debop4k.web.ApiResult;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Spring MVC {@link Controller} 에서 사용할 수 있는 Helper class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class ControllerEx {

  private ControllerEx() {}

  /**
   * HTTP Request Timeout (90 sec)
   */
  public static long TimeoutInMillis = 90000L;
  /**
   * HTTP 통신 기본 Content Type 입니다.
   */
  public static String DEFAULT_CONTENT_TYPE = "application/octet-stream";

  // TODO: DeferredResult 를 반환하는 작업을 추가하자.
  // HINT: http://xpadro.blogspot.kr/2015/07/understanding-callable-and-spring.html
  // HINT: https://github.com/spring-projects/spring-mvc-showcase/blob/master/src/main/java/org/springframework/samples/mvc/async/DeferredResultController.java

  public static <T> WebAsyncTask<ResponseEntity<T>> asyncTask(Function0<ResponseEntity<T>> supplier) {
    return asyncTask(TimeoutInMillis, supplier);
  }

  public static <T> WebAsyncTask<ResponseEntity<T>> asyncTask(long timeoutInMillis,
                                                              @NonNull final Function0<ResponseEntity<T>> supplier) {
    return new WebAsyncTask<ResponseEntity<T>>(timeoutInMillis,
                                               new Callable<ResponseEntity<T>>() {
                                                 @Override
                                                 public ResponseEntity<T> call() throws Exception {
                                                   return supplier.invoke();
                                                 }
                                               });
  }

  public static <T> Promise<ResponseEntity<T>, Exception> execAsync(final Function0<T> supplier) {
    return Asyncx.future(new Callable<ResponseEntity<T>>() {

      @Override
      public ResponseEntity<T> call() throws Exception {
        return success(supplier);
      }
    });

  }

  public static <T> ResponseEntity<T> success(final Function0<T> supplier) {
    try {
      return new ResponseEntity<T>(supplier.invoke(), HttpStatus.OK);
    } catch (Exception e) {
      log.error("메소드 실행에 예외가 발생헸습니다.", e);
      return serviceUnavailable();
    }
  }

  public static <T> ResponseEntity<T> serviceUnavailable() {
    return new ResponseEntity<T>((T) null, HttpStatus.SERVICE_UNAVAILABLE);
  }

  /**
   * Controller 에서 예외가 발생했을 시에 예외 정보를 {@link ResponseEntity} 로 빌드합니다.
   *
   * @param e 예외 정보
   * @return ResponesEntity 인스턴스
   */
  public static ResponseEntity<ApiResult> handleException(Exception e) {
    ApiResult result;
    if (e != null) {
      result = ApiResult.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
      log.error("예외가 발생했습니다.", e);
    } else {
      result = ApiResult.of(999, "알 수 없는 예외가 발생했습니다.");
    }
    return new ResponseEntity<ApiResult>(result, HttpStatus.SERVICE_UNAVAILABLE);
  }

  /**
   * 파일을 클라이언트에 전송합니다.
   *
   * @param response    HttpServletRepsonse 인스턴스
   * @param path        전송할 파일 경로
   * @param contentType 파일 Content Type
   */
  @SneakyThrows(IOException.class)
  public static void sendFile(@NonNull final HttpServletResponse response,
                              @NonNull final String path,
                              @NonNull final MediaType contentType) {
    byte[] content = handleFileSend(response, path, contentType);
    response.getOutputStream().write(content);
  }

  /**
   * 파일을 클라이언트에 비동기 방식으로 전송합니다.
   *
   * @param response    HttpServletRepsonse 인스턴스
   * @param path        전송할 파일 경로
   * @param contentType 파일 Content Type
   * @return {@link Promise} 인스턴스
   */
  public static Promise<Unit, Exception> sendFileAsync(@NonNull final HttpServletResponse response,
                                                       @NonNull final String path,
                                                       @NonNull final MediaType contentType) {
    final byte[] content = handleFileSend(response, path, contentType);

    return Asyncx.future(new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        try {
          response.getOutputStream().write(content);
        } catch (IOException e) {
          log.error("파일 전송에 예외가 발생했습니다.", e);
        }
        return Unit.INSTANCE;
      }
    });
  }

  /**
   * Http Response 에 파일을 다운로드 하기 위해 파일 내용을 읽어 오고, HttpResponse 의 Header 에 관련 정보를 설정합니다.
   *
   * @param response Http Response 객체
   * @param path     다운로드할 파일 경로
   * @return 파일 내용
   */
  public static byte[] handleFileSend(@NonNull HttpServletResponse response,
                                      @NonNull String path) {

    return handleFileSend(response, path, MediaType.APPLICATION_OCTET_STREAM);
  }

  /**
   * Http Response 에 파일을 다운로드 하기 위해 파일 내용을 읽어 오고, HttpResponse 의 Header 에 관련 정보를 설정합니다.
   *
   * @param response    Http Response 객체
   * @param path        다운로드할 파일 경로
   * @param contentType 파일의 Content type
   * @return 파일 내용
   */
  @SneakyThrows(Exception.class)
  public static byte[] handleFileSend(@NonNull final HttpServletResponse response,
                                      @NonNull String path,
                                      MediaType contentType) {
    if (!Filex.exists(path))
      throw new FileNotFoundException("파일을 찾을 수 없습니다. path=" + path);

    Promise<byte[], Exception> result = Filex.readAllBytesAsync(path);

    response.setHeader("Content-Disposition", String.format("attachment: filename=\"%s\"", new File(path).getName()));
    response.setContentType(contentType.toString());

    byte[] bytes = Asyncx.result(result);
    response.setContentLength(bytes.length);

    return bytes;
  }

}
