package debop4k.core.cryptography.digesters

import org.jasypt.digest.PooledByteDigester
import org.jasypt.salt.SaltGenerator
import org.jasypt.salt.ZeroSaltGenerator

abstract class AbstractByteDigester(override val algorithm: String,
                                    override val saltGenerator: SaltGenerator = ZeroSaltGenerator()) : ByteDigester {

  private val digester: PooledByteDigester by lazy {
    PooledByteDigester().apply {
      setPoolSize(4)
      setAlgorithm(algorithm)
      setSaltGenerator(saltGenerator)
    }
  }

  override fun digest(message: ByteArray): ByteArray {
    return digester.digest(message)
  }

  override fun matches(message: ByteArray, digested: ByteArray): Boolean {
    return digester.matches(message, digested)
  }
}

class MD5ByteDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractByteDigester("MD5", saltGenerator)

class SHA1ByteDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractByteDigester("SHA1", saltGenerator)

class SHA256ByteDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractByteDigester("SHA256", saltGenerator)

class SHA384ByteDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractByteDigester("SHA384", saltGenerator)

class SHA512ByteDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractByteDigester("SHA512s", saltGenerator)


object ByteDigesters {

  val md5: MD5ByteDigester by lazy { MD5ByteDigester() }
  val sha1: SHA1ByteDigester by lazy { SHA1ByteDigester() }
  val sha256: SHA256ByteDigester by lazy { SHA256ByteDigester() }
  val sha384: SHA384ByteDigester by lazy { SHA384ByteDigester() }
  val sha512: SHA512ByteDigester by lazy { SHA512ByteDigester() }

}