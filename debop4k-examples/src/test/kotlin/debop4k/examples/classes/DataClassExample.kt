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

package debop4k.examples.classes

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class DataClassExample : AbstractExampleTest() {

  data class Client(val name: String, val postalCode: Int)

  @Test fun `data class equals`() {

    val client1 = Client("Alice", 342562)
    val client2 = Client("Alice", 342562)

    assertThat(client1).isEqualTo(client2)
    assertThat(client1 == client2).isTrue()
    assertThat(client1 === client2).isFalse()
  }

  @Test fun `data class copy`() {
    val bob = Client("Bob", 123456)
    assertThat(bob.copy()).isEqualTo(bob)
  }
}
