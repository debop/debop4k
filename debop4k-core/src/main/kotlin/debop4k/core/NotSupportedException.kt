package debop4k.core

/**
 * NotSupportedException
 * @author sunghyouk.bae@gmail.com
 */
class NotSupportedException : RuntimeException {

  constructor() : super()
  constructor(msg: String) : super(msg)
  constructor(msg: String, cause: Throwable) : super(msg, cause)
  constructor(cause: Throwable) : super(cause)
}