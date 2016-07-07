package debop4k.core.cryptography.digesters

import org.jasypt.salt.SaltGenerator

/**
 * @author sunghyouk.bae@gmail.com
 */
interface ByteDigester {

  val algorithm: String

  val saltGenerator: SaltGenerator

  fun digest(message: ByteArray): ByteArray

  fun matches(message: ByteArray, digested: ByteArray): Boolean
}