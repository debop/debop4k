package debop4k.reactive.utils

/**
 * 테스트 시 Assertion 에외를 나타내는 클래스
 * @author sunghyouk.bae@gmail.com
 */
class AssertionFailedError(override val message: String? = null,
                           override val cause: Exception? = null) : RuntimeException(message, cause)