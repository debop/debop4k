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

package debop4k.science.gis;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;

/**
 * AbstractGisTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 9.
 */
@Slf4j
public abstract class AbstractGisTest {

  public static Offset<Double> TOLERANCE = Offset.offset(1.0e-8);

}
