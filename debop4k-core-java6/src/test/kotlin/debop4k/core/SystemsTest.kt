package debop4k.core

import org.junit.Test

/**
 * @author sunghyouk.bae@gmail.com
 */
class SystemsTest : AbstractCoreTest() {

  @Test fun retrieveJavaSpecificationVersion() {
    with(SystemEx) {
      log.debug("java version={}, implementation={}", version, implementationVersion)
      log.debug("java vendor={}, implementation={}", vendor, implementationVendor)

      log.debug("Java6={}", isJava6)
      log.debug("Java7={}", isJava7)
      log.debug("Java8={}", isJava8)

    }
  }
}