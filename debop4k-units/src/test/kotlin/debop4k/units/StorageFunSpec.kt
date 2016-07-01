package debop4k.units

import io.kotlintest.matchers.be
import io.kotlintest.specs.FunSpec

class StorageFunSpec : FunSpec() {
  init {

    test("convert Storage Unit") {

      100.toBytes().inBytes() shouldEqual 100.0
      100.toKBytes().inBytes() shouldEqual 100.0 * 1024
      100.toMBytes().inBytes() shouldEqual 100.0 * 1024 * 1024

      100.toGBytes().inKBytes() shouldEqual 100.0 * 1024 * 1024
      100.toTBytes().inMBytes() shouldEqual 100.0 * 1024 * 1024
      100.toPBytes().inGBytes() shouldEqual 100.0 * 1024 * 1024
      100.toXBytes().inTBytes() shouldEqual 100.0 * 1024 * 1024
      100.toZBytes().inPBytes() shouldEqual 100.0 * 1024 * 1024
      100.toYBytes().inXBytes() shouldEqual 100.0 * 1024 * 1024
    }

    test("toHuman") {
      100.toBytes().toHuman() shouldEqual "100 B"
      123.toKBytes().toHuman() shouldEqual "123.0 KB"
      123.43.toMBytes().toHuman() shouldEqual "123.4 MB"
      12.59.toGBytes().toHuman() shouldEqual "12.6 GB"
    }

    test("parsing Storage String") {
      Storage.parse("100 B") shouldEqual 100.toBytes()
      Storage.parse("17.5 KB") shouldEqual 17.5.toKBytes()
      Storage.parse("8.1 MB") shouldEqual 8.1.toMBytes()
    }

    test("negative") {
      -Storage(100.0) shouldEqual -100.toBytes()
    }

    test("arithmetic - plus/minus") {
      val a = Storage(100.0)
      val b = Storage(200.0)
      a + b shouldEqual 300.toBytes()
      b - a shouldEqual 100.toBytes()
    }
    test("arithmetic - times/div") {
      val a = Storage(100.0)
      val b = Storage(200.0)

      a * 2 shouldEqual b
      b / 2 shouldEqual a
    }

    test("compare") {
      1.7.toKBytes().compareTo(1.7.toBytes()) should be gt 0
      1.2.toMBytes().compareTo(1.2.toKBytes()) should be gt 0
      1.1.toMBytes().compareTo(0.9.toGBytes()) should be lt 0
    }
  }
}