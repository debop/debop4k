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

package debop4k.examples.functions

import debop4k.examples.AbstractExampleTest
import debop4k.examples.asyncs.runAsync
import org.junit.Test
import java.util.concurrent.*

class ExtensionFunctionExample : AbstractExampleTest() {

  data class User(val id: Int, val name: String, val address: String)

  fun saveUser1(user: User) {
    // Local function
    fun validate(value: String, fieldName: String) {
      if (value.isEmpty()) {
        // local function 에서는 상위 함수의 인자에 접근할 수 있다
        throw IllegalArgumentException("Cannot save user ${user.id}: $fieldName is empty")
      }
    }

    validate(user.name, "Name")
    validate(user.address, "Address")

    // Save
  }

  // Extension Method 를 이용하면 더 편하게 사용할 수 있습니다.

  fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
      if (value.isEmpty()) {
        // local function 에서는 상위 함수의 인자에 접근할 수 있다
        throw IllegalArgumentException("Cannot save user ${id}: $fieldName is empty")
      }
    }
    validate(name, "Name")
    validate(address, "Address")
  }

  fun saveUser2(user: User) {
    user.validateBeforeSave()
    // Save
  }

  @Test
  fun executorExecute() {
    val executor = Executors.newFixedThreadPool(4)
    log.debug("run methods...")
    executor.runAsync {
      log.debug("I was execute asynchronous.")
    }
    log.debug("call runAsync")
  }
}