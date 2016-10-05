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

package debop4k.core.cryptography;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StringDigesterTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class StringDigesterTest extends AbstractCoreTest {

  List<AbstractStringDigester> digesters =
      FastList.newListWith(new MD5StringDigester(),
                           new SHA1StringDigester(),
                           new SHA256StringDigester(),
                           new SHA384StringDigester(),
                           new SHA512StringDigester());

  @Test
  public void passwordDigest() {
    for (StringDigester digester : digesters) {
      String digestedStr = digester.digest(sampleText);
      assertThat(digester.matches(sampleText, digestedStr)).isTrue();
    }
  }
}
