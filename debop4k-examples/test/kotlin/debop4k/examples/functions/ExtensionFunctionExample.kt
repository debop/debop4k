/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.functions

import org.slf4j.LoggerFactory

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class ExtensionFunctionExample {
  val log = LoggerFactory.getLogger(javaClass)

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
}