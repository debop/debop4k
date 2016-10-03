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
import org.junit.Test


/**
 * @author debop sunghyouk.bae@gmail.com
 */
class ClassExample : AbstractExampleTest() {

  @Test fun secondary_constructor() {
    open class View {
      constructor(name: String) {
        // some code
      }

      constructor(name: String, age: Int) {
        // some code
      }
    }

    class MyButton : View {
      constructor(name: String) : this(name, 12) {
        // some code
      }

      constructor(name: String, age: Int) : super(name, age) {
        // some code
      }
    }

    val view = View("a", 12)
    val button = MyButton("default", 34)
  }

  @Test fun private_constructor() {
    class Secretive {
      // private constructor
      private constructor()
    }
    // not compiled
    // val x = Secretive()
  }
}