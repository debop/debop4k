package debop4k.core.utils


fun <T> (() -> T).exec(): T = this.invoke()