package debop4k.core.io

import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.CharBuffer
import java.nio.charset.Charset

/**
 * [InputStream]에 Encoding 정보를 제외한 실제 정보를 읽어드리는 Reader 입니다.
 */
class UnicodeReader(input: InputStream, defaultCharset: Charset = Charsets.UTF_8) : Reader() {

  val reader: InputStreamReader by lazy {
    val (withoutBom, charset) = input.withoutBomStream(defaultCharset)
    InputStreamReader(withoutBom, charset)
  }

  val encoding: String get() = reader.encoding

  override fun read(): Int = reader.read()

  override fun read(cbuf: CharArray?): Int = reader.read(cbuf)

  override fun read(cbuf: CharArray?, off: Int, len: Int): Int {
    return reader.read(cbuf, off, len)
  }

  override fun read(target: CharBuffer?): Int = reader.read(target)

  override fun ready(): Boolean = reader.ready()

  override fun reset() = reader.reset()

  override fun markSupported(): Boolean = reader.markSupported()

  override fun mark(readAheadLimit: Int) = reader.mark(readAheadLimit)

  override fun skip(n: Long): Long = reader.skip(n)

  override fun close() = reader.close()

}