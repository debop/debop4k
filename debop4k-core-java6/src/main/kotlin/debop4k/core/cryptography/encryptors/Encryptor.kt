package debop4k.core.cryptography.encryptors

import debop4k.core.fromHexString
import debop4k.core.toHexString
import debop4k.core.toUtf8String
import org.jasypt.salt.SaltGenerator

/**
 * @author sunghyouk.bae@gmail.com
 */
interface Encryptor {

  val algorithm: String
  val saltGenerator: SaltGenerator
  var password: String

  fun encrypt(message: ByteArray): ByteArray
  fun decrypt(encrypted: ByteArray): ByteArray

  fun encryptAsString(message: String): String {
    return encrypt(message.toByteArray()).toHexString()
  }

  fun decryptAsString(encrypted: String): String {
    return decrypt(encrypted.fromHexString()).toUtf8String()
  }
}