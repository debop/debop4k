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

package debop4k.core.lazyseq.samples

import debop4k.core.lazyseq.AbstractLazySeqTest
import debop4k.core.lazyseq.LazySeq
import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify

class LazyPagingTest : AbstractLazySeqTest() {

  @Mock
  val pageConsumer: (String) -> Unit = uninitialized()

  val PAGE_SIZE: Long = 5L

  @Test
  fun testLoadOnlyFirstPageWhenInitialized() {
    records(0)
    verify(pageConsumer).invoke("0,5")
  }

  @Test
  fun testReturnFirstFewRecords() {
    val records = records(0)
    records.startsWith(listOf(Record(0), Record(1), Record(2), Record(3)))
    verify(pageConsumer).invoke("0,5")
  }

  @Test
  fun testNotLoadSubsequentPagesIfOneRecordLeftInCurrentBatch() {
    val records = records(0)
    records.drop(PAGE_SIZE - 1)
    verify(pageConsumer).invoke("0,5")
  }

  @Test
  fun testLoadAllPreviousPageWhenAccessingArbitraryRecord() {
    val records = records(0)

    val record = records[17]

    val order = Mockito.inOrder(pageConsumer)
    order.verify(pageConsumer).invoke("0,5")
    order.verify(pageConsumer).invoke("5,5")
    order.verify(pageConsumer).invoke("10,5")
    order.verify(pageConsumer).invoke("15,5")
    assertThat(record).isEqualTo(Record(17))
  }

  private fun records(from: Long): LazySeq<Record> {
    return LazySeq.concat(loadPage(from, PAGE_SIZE)) { records(from + PAGE_SIZE) }
  }

  fun loadPage(offset: Long, max: Long): List<Record> {
    pageConsumer.invoke("$offset,$max")
    return LazySeq.numbers(offset)
        .take(max)
        .map(::Record)
        .toList()
  }
}