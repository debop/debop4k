@file:JvmName("charsequenceExtensions")
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST", "NOTHING_TO_INLINE")

package debop4k.core.java8.utils

import java.util.stream.*

/**
 * Returns an [IntStream] of UTF-16 character code values from this sequence.
 * Surrogate pairs are represented as pair of consecutive chars.
 */
inline fun CharSequence.chars(): IntStream = (this as java.lang.CharSequence).chars()

/**
 * Returns an [IntStream] of UTF code point values from this sequence.
 */
inline fun CharSequence.codePoints(): IntStream = (this as java.lang.CharSequence).codePoints()