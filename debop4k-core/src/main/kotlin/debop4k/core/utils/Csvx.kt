/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("Csvx")

package debop4k.core.utils

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.eclipse.collections.impl.list.mutable.FastList
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * CSV 파일을 읽어 [CSVRecord] 컬렉션으로 변환합니다.
 */
@JvmOverloads
fun File.toCSVRecordList(cs: Charset = Charsets.UTF_8,
                         skipHeader: Boolean = true): FastList<CSVRecord> {
  return FileInputStream(this).buffered().toCSVRecordList(cs, skipHeader)
}

/**
 * CSV 파일을 읽어 [List<String>]의 컬렉션으로 변환합니다.
 */
@JvmOverloads
fun File.toListStringList(cs: Charset = Charsets.UTF_8): FastList<List<String>> {
  return FileInputStream(this).buffered().toListStringList(cs)
}

/**
 * CSV 형식의 [InputStream]을 읽어 [CSVRecord] 컬렉션으로 변환합니다.
 */
@JvmOverloads
fun InputStream.toCSVRecordList(cs: Charset = Charsets.UTF_8,
                                skipHeader: Boolean = true): FastList<CSVRecord> {
  val reader = InputStreamReader(this, cs)
  val parser = CSVFormat.EXCEL.parse(reader)

  val records = FastList.newList<CSVRecord>(16)
  val iter = parser.iterator()

  // Header 를 제외
  if (skipHeader) {
    iter.next()
  }

  while (iter.hasNext()) {
    records.add(iter.next())
  }
  return records
}

/**
 * CSV 형식의 [InputStream]을 읽어 [List<String>]의 컬렉션으로 변환합니다.
 */
@JvmOverloads
fun InputStream.toListStringList(cs: Charset = Charsets.UTF_8): FastList<List<String>> {
  val records = this.toCSVRecordList(cs)
  val itemsList = FastList.newList<List<String>>(records.size)

  for (record in records) {
    val items = FastList.newList<String>(record.size())
    for (item in record) {
      items.add(item)
    }
    itemsList.add(items)
  }

  return itemsList
}

