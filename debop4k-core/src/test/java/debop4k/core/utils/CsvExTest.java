/*
 * Copyright 2015-2020 KESTI s.r.o.
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

package debop4k.core.utils;

import debop4k.core.AbstractCoreTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static debop4k.core.utils.Convertx.asDateTime;
import static debop4k.core.utils.Convertx.asInt;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * CSV 파일 읽기
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 11. 29.
 */
@Slf4j
public class CsvExTest extends AbstractCoreTest {

  @Test
  @SneakyThrows({IOException.class})
  public void readPM10() {
    InputStream input = Resourcex.getClassPathResourceStream("csv/pm10_station_all.csv");
    try {
      assertThat(input).isNotNull();
      List<CSVRecord> records = Csvx.toCSVRecordList(input);
      for (CSVRecord record : records) {
        Integer stationId = asInt(record.get(1));
        DateTime endTime = asDateTime(record.get(2), "yyyy.MM.dd.HH:mm");
        DateTime startTime = asDateTime(record.get(3), "yyyy.MM.dd.HH:mm");
        String stationName = record.get(4);

        log.debug("stationId={}, endTime={}, startTime={}, stationName={}",
                  stationId, endTime, startTime, stationName);
      }
    } finally {
      input.close();
    }
  }
}
