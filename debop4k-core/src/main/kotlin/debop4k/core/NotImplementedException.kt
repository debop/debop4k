package debop4k.core

/**
 * NotImplementedException
 * @author sunghyouk.bae@gmail.com
 */
class NotImplementedException : RuntimeException {

  constructor() : super()
  constructor(msg: String) : super(msg)
  constructor(msg: String, cause: Throwable) : super(msg, cause)
  constructor(cause: Throwable) : super(cause)
}