package debop4k.core.compressions

import debop4k.core.emptyByteArray
import debop4k.core.io.stream.toByteArray
import org.springframework.util.FastByteArrayOutputStream
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

/**
 * Deflater 알고리즘을 이용하는 압축기
 * @author sunghyouk.bae@gmail.com
 */
class DeflateCompressor : Compressor {

  override fun compress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return emptyByteArray

    FastByteArrayOutputStream().use { bos ->
      DeflaterOutputStream(bos).use { deflater ->
        deflater.write(input)
      }
      return bos.toByteArrayUnsafe()
    }
  }

  override fun decompress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return emptyByteArray

    BufferedInputStream(ByteArrayInputStream(input)).use { bis ->
      InflaterInputStream(bis).use { inflater ->
        return inflater.toByteArray()
      }
    }
  }
}