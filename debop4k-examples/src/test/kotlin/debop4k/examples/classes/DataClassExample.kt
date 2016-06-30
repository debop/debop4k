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
class DataClassExample : FunSpec() {

  data class Client(val name: String, val postalCode: Int)

  init {

    test("data class equals") {

      val client1 = Client("Alice", 342562)
      val client2 = Client("Alice", 342562)

      client1 shouldBe client2
      (client1 == client2) shouldBe true

    }

    test("data class copy") {
      val bob = Client("Bob", 123456)
      bob.copy() shouldEqual bob
    }

  }
}