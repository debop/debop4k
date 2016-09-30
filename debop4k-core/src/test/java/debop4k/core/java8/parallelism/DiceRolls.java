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

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Slf4j
public class DiceRolls {

  @Rule
  public TestRule benchmarkRun = new BenchmarkRule();

  private static final int N = 100000000;

  @Test
  public void serialDiceRolls() {
    double fraction = 1.0 / N;
    Map<Integer, Double> numbers = IntStream.range(0, N)
                                            .mapToObj(twoDiceThrows())
                                            .collect(groupingBy(side -> side,
                                                                summingDouble(n -> fraction)));

    log.debug("serial={}", numbers);
  }

  @Test
  public void parallelDiceRolls() {
    double fraction = 1.0 / N;
    Map<Integer, Double> numbers = IntStream.range(0, N)
                                            .parallel()
                                            .mapToObj(twoDiceThrows())
                                            .collect(groupingBy(side -> side,
                                                                summingDouble(n -> fraction)));
    log.debug("serial={}", numbers);
  }

  private static IntFunction<Integer> twoDiceThrows() {
    return i -> {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      int firstThrows = random.nextInt(1, 7);
      int secondThrows = random.nextInt(1, 7);
      return firstThrows + secondThrows;
    };
  }
}
