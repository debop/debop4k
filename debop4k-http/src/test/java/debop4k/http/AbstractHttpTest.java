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

package debop4k.http;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public abstract class AbstractHttpTest {

  protected static final String URI_GOOGLE = "https://www.google.co.kr";
  protected static final String URI_DUCKDUCKGO = "https://api.duckduckgo.com/";

  @SneakyThrows({URISyntaxException.class})
  protected URI makeURIWithSearchString(String searchStr) {
    return new URIBuilder()
        .setPath(URI_DUCKDUCKGO)
        .setParameter("q", searchStr)
        .setParameter("format", "json")
        .setParameter("pretty", "1")
        .build();
  }

  @SneakyThrows({URISyntaxException.class})
  protected URI makeSSLURI() {
    return new URIBuilder().setScheme("https")
                           .setHost("issues.apache.org")
                           .setPort(443)
                           .build();
  }
}
