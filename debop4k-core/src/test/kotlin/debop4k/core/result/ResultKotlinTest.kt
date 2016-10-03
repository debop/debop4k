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

package debop4k.core.result

import debop4k.core.loggerOf
import debop4k.core.utils.Resourcex
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class ResultKotlinTest {

  private val log = loggerOf(javaClass)

  @Test
  fun testCreateSuccess() {
    val v = Result.of(1)

    assertThat(v)
        .isNotNull()
        .isInstanceOf(Result.Success::class.java)
        .extracting("value").isEqualTo(arrayOf(1))
  }

  @Test
  fun testCreateFailure() {
    val e = Result.error(RuntimeException())

    assertThat(e).isNotNull()
        .isInstanceOf(Result.Failure::class.java)
  }

  @Test
  fun testCreateOptionalValue() {
    val value1: String? = null
    val value2: String? = "1"

    val result1 = Result.of(value1) { UnsupportedOperationException("value is null") }
    val result2 = Result.of(value2) { IllegalStateException("value is null") }

    assertThat(result1).isInstanceOf(Result.Failure::class.java)
    assertThat(result2).isInstanceOf(Result.Success::class.java)
  }

  @Test
  fun testCreateFromLambda() {
    val f1: () -> String = { "foo" }
    val f2: () -> Int = {
      val v = arrayListOf<Int>()
      v[1]
    }
    val f3: () -> String? = {
      val s: String?
      s = null
      s
    }

    val result1: Result<String, Exception> = Result.of(f1)
    val result2: Result<Int, Exception> = Result.of(f2)
    val result3 = Result.of(f3())

    assertThat(result1).isInstanceOf(Result.Success::class.java)
    assertThat(result2).isInstanceOf(Result.Failure::class.java)
    assertThat(result3).isInstanceOf(Result.Failure::class.java)
  }

  @Test
  fun testOr() {
    val one = Result.of<Int>(null) or 1

    assertThat(one).isInstanceOf(Result.Success::class.java)
  }

  @Test
  fun testOrElse() {
    val one = Result.of<Int>(null) getOrElse 1

    assertThat(one).isEqualTo(1)
  }

  @Test
  fun testSuccess() {
    val result = Result.of { true }

    var beingCalled = false
    result.success {
      beingCalled = true
    }

    var notBeingCalled = true
    result.failure {
      notBeingCalled = false
    }

    assertThat(beingCalled).isTrue()
    assertThat(notBeingCalled).isTrue()
  }

  @Test
  fun testFailure() {
    val result = Result.of { File("not_found_file").readText() }

    var beingCalled = false
    result.failure {
      beingCalled = true
    }

    var notBeingCalled = true
    result.success {
      notBeingCalled = false
    }

    assertThat(beingCalled).isTrue()
    assertThat(notBeingCalled).isTrue()
  }

  @Test
  fun testGet() {
    val f1 = { true }
    val f2 = { File("not_found_file").readText() }

    val result1 = Result.of(f1)
    val result2 = Result.of(f2)

    assertThat(result1.get()).isTrue()

    var raiseError = false
    try {
      result2.get()
    } catch(e: FileNotFoundException) {
      raiseError = true
    }
    assertThat(raiseError).isTrue()
  }

  @Suppress("UNUSED_VARIABLE")
  @Test
  fun testGetAsValue() {
    val result1 = Result.of(22)
    val result2 = Result.error(KotlinNullPointerException())

    val v1: Int = result1.getAs()!!
    val (v2: Nothing?, err) = result2

    assertThat(v1).isEqualTo(22)
    assertThat(err).isInstanceOf(KotlinNullPointerException::class.java)
  }

  @Test
  fun testFold() {
    val success = Result.of("success")
    val failure = Result.error(RuntimeException("failure"))

    val v1 = success.fold({ 1 }, { 0 })
    val v2 = failure.fold({ 1 }, { 0 })

    assertThat(v1).isEqualTo(1)
    assertThat(v2).isEqualTo(0)
  }

  fun Nothing.count(): Int = 0

  fun Nothing.getMessage() = ""

  @Suppress("UNREACHABLE_CODE")
  @Test
  fun testMap() {
    val success = Result.of("success")
    val failure = Result.error(RuntimeException("failure"))

    val v1: Result<Int, Exception> = success.map { it.count() }
    val v2: Result<Int, Exception> = failure.map { it.count() }

    assertThat(v1.getAs<Int>()).isEqualTo(7)
    assertThat(v2.getAs<Int>()).isNull()
  }

  @Suppress("UNREACHABLE_CODE")
  @Test
  fun testFlatMap() {
    val success = Result.of("success")
    val failure = Result.error(RuntimeException("failure"))

    val v1 = success.flatMap { Result.of(it.last()) }
    val v2 = failure.flatMap { Result.of(it.count()) }

    assertThat(v1.getAs<Char>()).isEqualTo('s')
    assertThat(v2.getAs<Char>()).isNull()
  }

  //@Suppress("UNREACHABLE_CODE")
  @Test
  fun testMapError() {
    val success = Result.of("success")
    val failure = Result.error(Exception("failure"))

    val v1 = success.mapError { InstantiationException(it.message) }
    val v2 = failure.mapError { InstantiationException(it.message) }

    assertThat(v1).isInstanceOf(Result.Success::class.java)
    assertThat(v1.component1()!!).isEqualTo("success")

    assertThat(v2).isInstanceOf(Result.Failure::class.java)
    assertThat(v2.component2()!!.message).isEqualTo("failure")
  }

  @Test
  fun testFlatMapError() {
    val success = Result.of("success")
    val failure = Result.error(Exception("failure"))

    val v1 = success.flatMapError { Result.error(InstantiationException()) }
    val v2 = failure.flatMapError { Result.error(InstantiationException()) }

    assertThat(v1).isInstanceOf(Result.Success::class.java)
    assertThat(v1.getAs<String>()).isEqualTo("success")

    assertThat(v2).isInstanceOf(Result.Failure::class.java)
    assertThat(v2.component2()!!).isInstanceOf(InstantiationException::class.java)
    assertThat(v2.component2()!!.message).isNull()
  }

  @Test
  fun testAny() {
    val foo = Result.of { readFile("foo.txt") }
    val nofile = Result.of { readFile("not_exists_file.txt") }

    val v1 = foo.any { "Lorem" in it }
    val v2 = nofile.any { "Lorem" in it }
    val v3 = foo.any { "LOREM" in it }

    assertThat(v1).isTrue()
    assertThat(v2).isFalse()
    assertThat(v3).isFalse()
  }

  @Test
  fun testComposableFunctions1() {
    val foo = { readFile("foo.txt") }
    val bar = { readFile("bar.txt") }

    val notFound = { readFile("not_exists_file.txt") }

    assertThat(Result.of(notFound)).isInstanceOf(Result.Failure::class.java)

    val (value1, error1) = Result.of(foo).map { it.count() }.mapError { IllegalStateException() }
    val (value2, error2) = Result.of(notFound).map { bar }.mapError { IllegalStateException() }

    assertThat(value1).isEqualTo(574)
    assertThat(error1).isNull()
    assertThat(value2).isNull()
    assertThat(error2).isInstanceOf(IllegalStateException::class.java)
  }

  @Test
  fun testComposableFunction2() {
    val r1 = Result.of(functionThatCanReturnNull(false))
        .flatMap { resultReadFile("bar.txt") }
        .mapError { Exception("this should not happen") }

    val r2 = Result.of(functionThatCanReturnNull(true))
        .map { it.rangeTo(Int.MAX_VALUE) }
        .mapError { KotlinNullPointerException() }

    assertThat(r1).isInstanceOf(Result.Success::class.java)
    assertThat(r2).isInstanceOf(Result.Failure::class.java)
  }

  @Test
  fun testNoException() {
    val r = concat("1", "2")

    assertThat(r)
        .isInstanceOf(Result.Success::class.java)
        .isEqualTo(Result.of("12"))
  }


  private fun readFile(name: String): String {
    val text = Resourcex.getString("./result/$name")
    if (text.isEmpty())
      throw FileNotFoundException(name)
    return text
  }

  private fun resultReadFile(name: String): Result<String, Exception> {
    return Result.of { readFile(name) }
  }

  private fun functionThatCanReturnNull(nullEnalbed: Boolean): Int? {
    return if (nullEnalbed) null else Int.MIN_VALUE
  }

  private fun concat(a: String, b: String): Result<String, NoException> {
    return Result.Success(a + b)
  }

}