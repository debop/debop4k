package debop4k.examples.classes

import io.kotlintest.specs.FunSpec

/**
 * @author sunghyouk.bae@gmail.com
 */

enum class BitCount(val value: Int) {
  x16(16),
  x32(32),
  x64(64);

  companion object {
    fun from(findValue: Int): BitCount = BitCount.values().first { it.value == findValue }
  }
}


class EnumClassExample : FunSpec() {


  init {

    test("enum class 생성하기") {
      val bit = BitCount.x32
      println(bit)
    }

    test("MassUnit enum class 생성하기") {
      val mg = MassUnit.MILLIGRAM
      println(mg)
    }
  }
}