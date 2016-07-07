package debop4k.core.cryptography.digesters

import org.jasypt.digest.PooledStringDigester
import org.jasypt.salt.SaltGenerator
import org.jasypt.salt.ZeroSaltGenerator

abstract class AbstractStringDigester(override val algorithm: String,
                                      override val saltGenerator: SaltGenerator = ZeroSaltGenerator()) : StringDigester {

  private val digester: PooledStringDigester by lazy {
    PooledStringDigester().apply {
      setPoolSize(4)
      setAlgorithm(algorithm)
      setSaltGenerator(saltGenerator)
    }
  }

  override fun digest(message: String): String {
    return digester.digest(message)
  }

  override fun matches(message: String, digested: String): Boolean {
    return digester.matches(message, digested)
  }
}

class MD5StringDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractStringDigester("MD5", saltGenerator)

class SHA1StringDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractStringDigester("SHA1", saltGenerator)

class SHA256StringDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractStringDigester("SHA256", saltGenerator)

class SHA384StringDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractStringDigester("SHA384", saltGenerator)

class SHA512StringDigester(saltGenerator: SaltGenerator = ZeroSaltGenerator()) : AbstractStringDigester("SHA512s", saltGenerator)


object StringDigesters {

  val md5: MD5ByteDigester by lazy { MD5ByteDigester() }
  val sha1: SHA1ByteDigester by lazy { SHA1ByteDigester() }
  val sha256: SHA256ByteDigester by lazy { SHA256ByteDigester() }
  val sha384: SHA384ByteDigester by lazy { SHA384ByteDigester() }
  val sha512: SHA512ByteDigester by lazy { SHA512ByteDigester() }

}