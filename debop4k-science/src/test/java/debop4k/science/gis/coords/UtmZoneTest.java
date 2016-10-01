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

package debop4k.science.gis.coords;

import debop4k.science.gis.AbstractGisTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.science.gis.coords.UtmZonex.utmZoneOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * UtmZoneTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 12.
 */
@Slf4j
public class UtmZoneTest extends AbstractGisTest {

  @Test
  public void instancing() {
    UtmZone utm51S = utmZoneOf(51, 'S');
    UtmZone utm51S2 = utmZoneOf("51S");

    assertThat(utm51S2).isEqualTo(utm51S);

    assertThat(utm51S2).isEqualTo(utmZoneOf("51s"));
    assertThat(utm51S2).isEqualTo(utmZoneOf(51, 's'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNull() {
    utmZoneOf((String) null);
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorWithEmptyString() {
    utmZoneOf("");
  }

  @Test(expected = NumberFormatException.class)
  public void testConstructorWithMalFormat() {
    UtmZone utmZone = utmZoneOf("S51");
    assertThat(utmZone.getLongitudeZone()).isEqualTo(51);
    assertThat(utmZone.getLatitudeZone()).isEqualTo('S');
  }

}
