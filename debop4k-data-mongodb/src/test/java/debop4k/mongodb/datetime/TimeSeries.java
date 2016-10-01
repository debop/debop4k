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

import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.mongodb.AbstractMongoDocument;
import debop4k.timeperiod.models.TimestampZoneText;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 시계열 정보를 표현하는 MongoDB 용 엔티티.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Document
@Getter
@Setter
public class TimeSeries extends AbstractMongoDocument {

  @Indexed
  private TimestampZoneText time;

  private long score;

  public TimeSeries() {}

  public TimeSeries(DateTime time, long score) {
    this.time = TimestampZoneText.of(time);
    this.score = score;
  }

  @Override
  public int hashCode() {
    return Hashx.compute(time);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("time", time)
                .add("score", score);
  }

  private static final long serialVersionUID = 2629457149132159294L;
}
