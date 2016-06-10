/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.core.utils


object ObjectEx {

    fun equal(x: Any?, y: Any?): Boolean {
        return (x != null) && x.equals(y)
    }
}

fun Any?.equals(y: Any?): Boolean = this != null && this!!.equals(y)
