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

import com.fasterxml.jackson.databind.ObjectMapper;
import debop4k.core.json.JacksonSerializer;
import debop4k.core.json.Jsonx;
import debop4k.core.utils.Convertx;
import debop4k.core.utils.Stringx;
import kotlin.text.Charsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Spring MVC 를 이용하는 Web Application 을 위한 Helper class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class WebEx {

  private WebEx() {}

  /**
   * Restful API 의 응답 형식은 UTF8 방식의 "application/json" 을 나타냅니다.
   */
  public static final MediaType APPLICATION_JSON_UTF8 =
      new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    Charsets.UTF_8);

  /**
   * Json Serializer
   */
  static final JacksonSerializer serializer = new JacksonSerializer();

  /**
   * 객체를 Json 직렬화를 수행하고, 바이트 배열로 반환합니다.
   *
   * @param obj Json 직렬화할 객체
   * @return Json 직렬화된 정보를 담은 바이트 배열
   */
  public static byte[] objectToJsonByteArray(Object obj) {
    return serializer.toByteArray(obj);
  }

  /**
   * 객체가 Map 인 경우, Map의 정보를 HTTP GET 방식의 URL 을 만듭니다.
   *
   * @param obj 전송할 객체 정보
   * @return 객체 정보를 Form UrlEncoding 방식으로 인코딩한 정보
   */
  @SneakyThrows(Exception.class)
  @SuppressWarnings("unchecked")
  public static byte[] objectToFormUrlEncodedBytes(Object obj) {
    StringBuilder builder = new StringBuilder();
    ObjectMapper mapper = Jsonx.getDefaultObjectMapper();

    Map<String, Object> props = (Map<String, Object>) mapper.convertValue(obj, Map.class);

    for (Map.Entry<String, Object> entry : props.entrySet()) {
      builder.append(entry.getKey())
             .append("=")
             .append(entry.getValue())
             .append("&");
    }

    return Stringx.toUtf8Bytes(builder.toString());
  }

  public static int getPageNo(Integer pageNo) {
    return Convertx.asInt(pageNo);
  }

  public static int getPageSize(Integer pageSize, int defaultSize) {
    return (pageSize != null && pageSize >= 0) ? pageSize : defaultSize;
  }

  /**
   * Request 에서 parameter 정보를 가져옵니다. 만약 없다면 defaultValue 를 반환합니다.
   *
   * @param request      HttpServletRequest 인스턴스
   * @param paramName    파라미터 이름
   * @param defaultValue 파라미터의 값이 없을 때 제공되는 값
   * @return 파라미터 값
   */
  public static String getParamValue(HttpServletRequest request,
                                     String paramName,
                                     String defaultValue) {
    String value = request.getParameter(paramName);
    return Stringx.isNotEmpty(value) ? value : defaultValue;
  }
}
