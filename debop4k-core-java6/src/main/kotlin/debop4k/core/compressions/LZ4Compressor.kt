package debop4k.core.compressions

import debop4k.core.emptyByteArray
import net.jpountz.lz4.LZ4Factory

/**
 * LZ4 알고리즘으로 압축/복원
 * @author sunghyouk.bae@gmail.com
 */
class LZ4Compressor : Compressor {

  override fun compress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return emptyByteArray
    return compressor.compress(input)
  }

  override fun decompress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return emptyByteArray

    return decompressor.decompress(input, input.size * 1000)
  }

  companion object {
    val factory by lazy { LZ4Factory.fastestInstance() }
    val compressor by lazy { factory.fastCompressor() }
    val decompressor by lazy { factory.safeDecompressor() }
  }
}