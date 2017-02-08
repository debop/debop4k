@file:JvmName("Bomx")

package debop4k.core.io

import debop4k.core.Tuple2
import debop4k.core.tupleOf
import java.io.InputStream
import java.io.PushbackInputStream
import java.nio.charset.Charset
import java.util.*


private val BOM_SIZE = 4
private val ZZ = 0x00.toByte()
private val EF = 0xEF.toByte()
private val BB = 0xBB.toByte()
private val BF = 0xBF.toByte()
private val FE = 0xFE.toByte()
private val FF = 0xFF.toByte()

fun ByteArray.deleteBOM(defaultCharset: Charset): Tuple2<Int, Charset> {
  val header = this.copyOf(4)
  when (header) {
    byteArrayOf(ZZ, ZZ, FE, FF) -> return tupleOf(4, Charsets.UTF_32BE)
    byteArrayOf(FE, ZZ, ZZ, ZZ) -> return tupleOf(4, Charsets.UTF_32LE)
  }
  when (header.copyOf(3)) {
    byteArrayOf(EF, BB, BF) -> return tupleOf(3, Charsets.UTF_8)
  }
  when (header.copyOf(2)) {
    byteArrayOf(FE, FF) -> return tupleOf(2, Charsets.UTF_16BE)
    byteArrayOf(FF, FE) -> return tupleOf(2, Charsets.UTF_16LE)
  }
  return tupleOf(0, defaultCharset)
}

fun ByteArray.withoutBomArray(defaultCharset: Charset = Charsets.UTF_8): Tuple2<ByteArray, Charset> {
  val bom = this.copyOf(4)
  val (skipSize, charset) = deleteBOM(defaultCharset)
  val array = if (skipSize > 0) Arrays.copyOfRange(this, skipSize, size) else this
  return tupleOf(array, charset)
}

fun InputStream.withoutBomStream(defaultCharset: Charset = Charsets.UTF_8): Tuple2<InputStream, Charset> {
  val bom = ByteArray(BOM_SIZE)
  val pushbackStream = PushbackInputStream(this, BOM_SIZE)
  val readSize = pushbackStream.read(bom, 0, bom.size)
  val (skipSize, charset) = bom.deleteBOM(defaultCharset)

  pushbackStream.unread(bom, skipSize, readSize - skipSize)
  return tupleOf(pushbackStream, charset)
}
