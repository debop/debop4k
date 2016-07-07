package debop4k.core.cryptography

import java.security.SecureRandom
import java.util.*


fun randomBytes(num: Int): ByteArray {
  require(num >= 0)

  val bytes = ByteArray(num)
  random.nextBytes(bytes)
  return bytes
}

val RANDOM_NUMBER_GENERATION = "SHA1PRNG"

internal val random: Random = SecureRandom.getInstance(RANDOM_NUMBER_GENERATION)