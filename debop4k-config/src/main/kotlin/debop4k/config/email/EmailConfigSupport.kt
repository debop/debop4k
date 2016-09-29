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

package debop4k.config.email

import debop4k.config.ConfigSupport

/**
 * Email 서버 접속을 위한 환경 설정 정보를 {@link Config} 로부터 읽어드리는 Adapter.
 * <p>
 * <blockquote><pre><code>
 *     # Email server settings
 *     email {
 *        host = "mail.kesti.co.kr"
 *        port = 25
 *        username = "service@kesti.co.kr"
 *        password = "rhrlwntpdy"
 *        encoding = "UTF-8"
 *        sender = "kesti &lt;service@kesti.co.kr&gt;"
 * <p>
 *        mail.transport.protocol = "smtp"
 *        mail.smtp.auth = "true"
 *        mail.smtp.starttls.enable = "true"
 *        mail.smtp.ssl.trust = "mail.kesti.co.kr"
 *      }
 * </code></pre></blockquote>
 *
 * @author sunghyouk.bae @gmail.com
 */
interface EmailConfigSupport : ConfigSupport {

  val email: EmailConfigElement
    get() = EmailConfigElement(config.getConfig("email"))
}