@file:JvmName("collections")
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST", "NOTHING_TO_INLINE")

package debop4k.core.java8.utils

import java.util.*
import java.util.function.*


inline fun <T> Iterable<T>.forEach(action: Consumer<T>): Unit {
  (this as java.lang.Iterable<T>).forEach(action)
}

inline fun <T> Iterable<T>.spliterator(): Spliterator<T>
    = (this as java.lang.Iterable<T>).spliterator()