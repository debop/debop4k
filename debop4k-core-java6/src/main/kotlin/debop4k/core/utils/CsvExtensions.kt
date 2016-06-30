/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("csv")

package debop4k.core.utils

import debop4k.core.collections.fastListOf
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.eclipse.collections.impl.list.mutable.FastList
import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author sunghyouk.bae@gmail.com
 */

fun InputStream.toCSVRecords(cs: Charset = Charsets.UTF_8): List<CSVRecord> {
  val records = fastListOf<CSVRecord>()

  this.bufferedReader(cs).use { reader ->
    val parser = CSVFormat.EXCEL.parse(reader)
    for (item in parser) {
      records.add(item)
    }
  }
  return records
}

fun InputStream.toStringListList(cs: Charset = Charsets.UTF_8): List<List<String>> {
  val records = this.toCSVRecords(cs)
  val itemsList = FastList.newList<MutableList<String>>(records.size)

  records.forEach { record ->
    val items = FastList.newList<String>(record.size())
    record.forEach { item -> items.add(item) }
    itemsList.add(items)
  }
  return itemsList
}
