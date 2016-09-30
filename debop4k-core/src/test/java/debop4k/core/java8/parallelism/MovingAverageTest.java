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

package debop4k.core.java8.parallelism;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class MovingAverageTest {

  @Test
  public void smallArray() {
    double[] input = {0, 1, 2, 3, 4, 3.5};
    double[] result = ArrayExamples.simpleMovingAverage(input, 3);
    log.debug("이동평균=" + Arrays.toString(result));

    double[] expected = {1.0, 2.0, 3.0, 3.5};
    assertThat(result).isEqualTo(expected);

  }
}
