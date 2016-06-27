/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("streams")

package debop4k.core.io

import java.io.InputStream
import java.nio.charset.Charset

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