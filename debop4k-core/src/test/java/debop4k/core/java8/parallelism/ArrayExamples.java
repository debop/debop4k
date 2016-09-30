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

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class ArrayExamples {

  /**
   * 이동 평균
   */
  public static double[] simpleMovingAverage(double[] values, int n) {
    double[] sums = Arrays.copyOf(values, values.length);
    Arrays.parallelPrefix(sums, Double::sum);
    int start = n - 1;
    return IntStream.range(start, sums.length)
                    .mapToDouble(i -> {
                      double prefix = (i == start) ? 0 : sums[i - n];
                      return (sums[i] - prefix) / n;
                    })
                    .toArray();
  }

  public static double[] parallelInitialize(int size) {
    double[] values = new double[size];
    Arrays.parallelSetAll(values, i -> i);
    return values;
  }

  public static double[] imperativeInitialize(int size) {
    double[] values = new double[size];
    for (int i = 0; i < values.length; i++) {
      values[i] = i;
    }
    return values;
  }
}
