/*
 * Copyright 2015-2020 KESTI s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.cryptography;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JasyptTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class JasyptTest extends AbstractCoreTest {

  String password = "@21abc!12ks";

  @Test
  public void strongPasswordEncryptor() {
    PasswordEncryptor encryptor = new StrongPasswordEncryptor();

    String encrypted = encryptor.encryptPassword(password);
    assertThat(encryptor.checkPassword(password, encrypted)).isTrue();
    log.debug("encrypted={}", encrypted);
  }

  @Test
  @Ignore("$JAVA_HOME/jre/lib/security 에 JCE 를 설치해야 작동합니다.")
  public void strongTextEncryptor() {
    StrongTextEncryptor encryptor = new StrongTextEncryptor();
    encryptor.setPassword(password);
    String encrypted = encryptor.encrypt(sampleText);
    String decrypted = encryptor.decrypt(encrypted);

    assertThat(decrypted).isEqualTo(sampleText);
    log.debug("encrypted={}, decrypted={}", encrypted, decrypted);
  }
}
