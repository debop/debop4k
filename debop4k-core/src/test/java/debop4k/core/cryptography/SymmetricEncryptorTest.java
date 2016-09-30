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
import debop4k.core.cryptography.encryptors.AbstractSymmetricEncryptor;
import debop4k.core.cryptography.encryptors.DESEncryptor;
import debop4k.core.cryptography.encryptors.RC2Encryptor;
import debop4k.core.cryptography.encryptors.TripleDESEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SymmetricEncryptorTest extends AbstractCoreTest {

  List<AbstractSymmetricEncryptor> encryptors =
      FastList.newListWith(new RC2Encryptor(), new DESEncryptor(), new TripleDESEncryptor());

  @Test
  public void encryptString() {
    for (SymmetricEncryptor encryptor : encryptors) {
      String encrypted = encryptor.encryptString(sampleText);
      log.debug("encryptor={}, encrypted={}", encryptor.getClass().getSimpleName(), encrypted);

      String decrypted = encryptor.decryptString(encrypted);
      log.debug("decrypted={}", decrypted);

      assertThat(decrypted).isEqualToIgnoringCase(sampleText);
    }
  }
}
