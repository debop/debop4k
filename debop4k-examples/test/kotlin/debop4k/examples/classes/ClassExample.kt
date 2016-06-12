/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.classes

import io.kotlintest.specs.FunSpec

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class ClassExample : FunSpec() {

  init {
    test("Secondary constructor") {
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

    test("private constructor") {

      class Secretive {
        // private constructor
        private constructor()
      }
      // not compiled
      // val x = Secretive()
    }
  }
}