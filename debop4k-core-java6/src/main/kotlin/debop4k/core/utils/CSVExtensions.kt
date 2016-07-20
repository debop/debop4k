/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

@file:JvmName("CSVExtensions")

package debop4k.core.utils

import debop4k.core.collections.eclipseCollections.fastListOf
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.eclipse.collections.impl.list.mutable.FastList
import java.io.InputStream
import java.nio.charset.Charset

/**
 * CSV 형식 파일의 [InputStream] 을 읽어 [CSVRecord] 컬렉션으로 변환합니다.
 * @author sunghyouk.bae@gmail.com
 */
@JvmOverloads
fun InputStream.toCSVRecords(cs: Charset = Charsets.UTF_8): FastList<CSVRecord> {
  val records = fastListOf<CSVRecord>()

  this.bufferedReader(cs).use { reader ->
    val parser = CSVFormat.EXCEL.parse(reader)
    for (item in parser) {
      records.add(item)
    }
  }
  return records
}

/**
 * CSV 형식 파일의 [InputStream] 을 읽어 문자열 List 의 List 로 변환합니다.
 */
@JvmOverloads
fun InputStream.toStringListList(cs: Charset = Charsets.UTF_8): FastList<FastList<String>> {
  val records = this.toCSVRecords(cs)
  val itemsList = FastList.newList<FastList<String>>(records.size)

  records.forEach { record ->
    val items = FastList.newList<String>(record.size())
    record.forEach { item -> items.add(item) }
    itemsList.add(items)
  }
  return itemsList
}
