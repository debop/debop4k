/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.csv

import java.io.Serializable

interface Quoting : Serializable

object QUOTE_ALL : Quoting
object QUOTE_MINIMAL : Quoting
object QUOTE_NONE : Quoting
object QUOTE_NONNUMERIC : Quoting


interface CSVFormat {

  val delimiter: Char
  val quoteChar: Char
  val escapeChar: Char
  val lineTerminator: String
  val quoting: Quoting
  val treatEmptyLineAsNull: Boolean
}

open class DefaultCSVFormat : CSVFormat {
  override val delimiter: Char = ','
  override val quoteChar: Char = '"'
  override val escapeChar: Char = '"'
  override val lineTerminator: String = "\r\n"
  override val quoting: Quoting = QUOTE_MINIMAL
  override val treatEmptyLineAsNull: Boolean = false
}

open class TSVFormat : CSVFormat {
  override val delimiter: Char = '\t'
  override val quoteChar: Char = '"'
  override val escapeChar: Char = '\\'
  override val lineTerminator: String = "\r\n"
  override val quoting: Quoting = QUOTE_NONE
  override val treatEmptyLineAsNull: Boolean = false
}