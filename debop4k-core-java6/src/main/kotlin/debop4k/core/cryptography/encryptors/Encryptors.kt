package debop4k.core.cryptography.encryptors

import org.jasypt.encryption.pbe.PooledPBEByteEncryptor
import org.jasypt.salt.SaltGenerator
import org.jasypt.salt.ZeroSaltGenerator

internal const val DEFAULT_PASSWORD = "@kesti21"

abstract class AbstractEncryptor(override val algorithm: String,
                                 override val saltGenerator: SaltGenerator = ZeroSaltGenerator(),
                                 override var password: String = DEFAULT_PASSWORD) : Encryptor {

  val encryptor: PooledPBEByteEncryptor by lazy {
    PooledPBEByteEncryptor().apply {
      setPoolSize(4)
      setAlgorithm(algorithm)
      setSaltGenerator(saltGenerator)
      setPassword(password)
    }
  }

  override fun encrypt(message: ByteArray): ByteArray {
    return encryptor.encrypt(message)
  }

  override fun decrypt(encrypted: ByteArray): ByteArray {
    return encryptor.decrypt(encrypted)
  }
}

class DESEncryptor(saltGenerator: SaltGenerator = ZeroSaltGenerator(),
                   password: String = DEFAULT_PASSWORD) : AbstractEncryptor("PBEwithMD5andDES", saltGenerator, password)

class RC2Encryptor(saltGenerator: SaltGenerator = ZeroSaltGenerator(),
                   password: String = DEFAULT_PASSWORD) : AbstractEncryptor("PBEwithSHA1andRC2_40", saltGenerator, password)

class TripleDESEncryptor(saltGenerator: SaltGenerator = ZeroSaltGenerator(),
                         password: String = DEFAULT_PASSWORD) : AbstractEncryptor("PBEwithSHA1andDESEDE", saltGenerator, password)

// NOTE: "PBEWithMD5AndTripleDES" 는 JCE 를 설치해야만 가능합니다. (Oracle 사이트에서 따로 다운받아 JRE에 추가해야 합니다.)


object Encryptors {

  val RC2 by lazy { RC2Encryptor() }

  val DES by lazy { DESEncryptor() }

  val TripleDES by lazy { TripleDESEncryptor() }

}