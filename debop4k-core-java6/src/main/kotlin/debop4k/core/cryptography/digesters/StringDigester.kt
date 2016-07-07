package debop4k.core.cryptography.digesters

import org.jasypt.salt.SaltGenerator

/**
 * @author sunghyouk.bae@gmail.com
 */
interface StringDigester {

  val algorithm: String

  val saltGenerator: SaltGenerator

  fun digest(message: String): String

  fun matches(message: String, digested: String): Boolean
}