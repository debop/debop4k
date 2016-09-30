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

package debop4k.core.utils

import debop4k.core.AbstractCoreKotlinTest
import lombok.SneakyThrows
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException

/**
 * CsvExKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class CsvExKotlinTest : AbstractCoreKotlinTest() {

  @Test
  @SneakyThrows(IOException::class)
  fun readPM10() {
    val input = Resourcex.getClassPathResourceStream("csv/pm10_station_all.csv")
    assertThat(input).isNotNull()
    input!!.use {
      val records = input.toCSVRecordList()
      for (record in records) {
        val stationId = record.get(1).asInt()
        val endTime = record.get(2).asDateTime("yyyy.MM.dd.HH:mm")
        val startTime = record.get(3).asDateTime("yyyy.MM.dd.HH:mm")
        val stationName = record.get(4)

        log.debug("stationId={}, endTime={}, startTime={}, stationName={}",
                  stationId, endTime, startTime, stationName)

        assertThat(stationId).isGreaterThan(0)
        assertThat(stationName).isNotNull()
      }
    }
  }
}