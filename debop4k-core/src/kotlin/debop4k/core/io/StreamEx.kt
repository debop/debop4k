/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.core.io

import java.io.InputStream

/**
 * @author debop sunghyouk.bae@gmail.com
 */
object StreamEx {

}

fun InputStream?.readText(): String? {
    if (this == null)
        return null

    return this.bufferedReader().readText()
}