package debop4k.core

val emptyCharArray = CharArray(0)
val emptyByteArray = ByteArray(0)
val emptyIntArray = IntArray(0)
val emptyLongArray = LongArray(0)


fun ByteArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()
fun IntArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()
fun LongArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

