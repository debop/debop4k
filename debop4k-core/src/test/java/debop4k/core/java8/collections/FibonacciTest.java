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

package debop4k.core.java8.collections;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * FibonacciTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 10.
 */
@Slf4j
public class FibonacciTest {

  @Rule
  public TestRule benchmarkRun = new BenchmarkRule();

  private static final int N = 35;

  @Test
  public void cachedFibonacci() {
    List<Long> xs = IntStream.range(0, N)
                             .parallel()
                             .mapToObj(Fibonacci::get)
                             .collect(toList());
    //log.debug("fibonacci={}", xs);
  }

  @Test
  public void fibonacci() {
    List<Long> xs = IntStream.range(0, N)
                             .mapToObj(this::calcFibonacci)
                             .collect(toList());
  }

  private long calcFibonacci(int x) {
    if (x <= 0) return 0L;
    else if (x == 1) return 1L;
    return calcFibonacci(x - 1) + calcFibonacci(x - 2);
  }
}
