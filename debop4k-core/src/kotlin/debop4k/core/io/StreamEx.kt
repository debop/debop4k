/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.core.io

import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author debop sunghyouk.bae@gmail.com
 */
object StreamEx {

}

/**
 * Input Stream 전체를 읽어 문자열로 반환합니다.
 */
fun InputStream.readText(): String? {
    return this.bufferedReader().readText()
}

/**
 * Input Stream 전체를 읽어 문자열 리스트로 반환합니다.
 */
fun InputStream.readLines(charset: Charset = Charsets.UTF_8): List<String> {
  return this.bufferedReader(charset).readLines()
}