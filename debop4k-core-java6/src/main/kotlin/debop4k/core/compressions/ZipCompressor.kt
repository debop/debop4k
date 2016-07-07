package debop4k.core.compressions

import debop4k.core.emptyByteArray
import debop4k.core.io.stream.toByteArray
import debop4k.core.isNullOrEmpty
import org.springframework.util.FastByteArrayOutputStream
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


class ZipCompressor : Compressor {

  override fun compress(input: ByteArray?): ByteArray {
    if (input.isNullOrEmpty())
      return emptyByteArray

    FastByteArrayOutputStream().use { bos ->
      ZipOutputStream(bos).use { zip ->
        zip.write(input)
      }
      return bos.toByteArrayUnsafe()
    }
  }

  override fun decompress(input: ByteArray?): ByteArray {
    if (input.isNullOrEmpty())
      return emptyByteArray

    BufferedInputStream(ByteArrayInputStream(input)).use { bis ->
      ZipInputStream(bis).use { zip ->
        return zip.toByteArray()
      }
    }
  }
}