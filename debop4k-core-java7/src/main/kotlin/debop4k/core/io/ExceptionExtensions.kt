@file:JvmName("exceptionExtensions")
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "NOTHING_TO_INLINE")

package debop4k.core.io

/**
 * Appends the specified exception to the exceptions that were
 * suppressed in order to deliver this exception.
 */
inline fun Throwable.addSuppressed(exception: Throwable) =
    (this as java.lang.Throwable).addSuppressed(exception)


/**
 * Returns an array containing all of the exceptions that were suppressed.
 */
inline fun Throwable.getSuppressed(): Array<Throwable>
    = (this as java.lang.Throwable).suppressed

/**
 * Returns an array containing all of the exceptions that were suppressed.
 */
val Throwable.suppressed: Array<Throwable>
  @JvmName("suppressed")
  get() = (this as java.lang.Throwable).suppressed