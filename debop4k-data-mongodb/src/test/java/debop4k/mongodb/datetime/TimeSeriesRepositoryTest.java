/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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
 *
 */

package debop4k.mongodb.datetime;

import debop4k.mongodb.AbstractMongoTest;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TimeSeriesConfiguration.class})
public class TimeSeriesRepositoryTest extends AbstractMongoTest {

  @Inject TimeSeriesRepository timeSeriesRepo;

  @Before
  public void setup() {
    timeSeriesRepo.deleteAll();
  }

  @Test
  public void saveEntity() {
    TimeSeries ts = new TimeSeries(DateTime.now(), 100);
    TimeSeries saved = timeSeriesRepo.save(ts);

    assertThat(saved).isNotNull();
    assertThat(saved).isEqualTo(ts);
  }
}
