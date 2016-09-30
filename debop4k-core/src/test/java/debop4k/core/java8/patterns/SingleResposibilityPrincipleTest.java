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

package debop4k.core.java8.patterns;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static debop4k.core.java8.patterns.SingleResponsibilityPrinciple.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(Parameterized.class)
public class SingleResposibilityPrincipleTest {

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][]{
        {new ImperativeRefactoredPrimeCounter()},
        {new ImperativeSingleMethodPrimeCounter()},
        {new FunctionalPrimeCount()},
        {new ParallelFunctionalPrimeCount()}
    };
    return Arrays.asList(data);
  }

  private final PrimeCounter primeCounter;

  public SingleResposibilityPrincipleTest(PrimeCounter primeCounter) {
    this.primeCounter = primeCounter;
  }

  @Test
  public void countsPrimesTo10() {
    assertThat(primeCounter.countPrimes(10)).isEqualTo(5);
    assertThat(primeCounter.countPrimes(20)).isEqualTo(9);
  }

  @Test
  public void countsPrimesTo20() {
    assertThat(primeCounter.countPrimes(20)).isEqualTo(9);
  }

  @Test
  public void countsPrimesTo30() {
    assertThat(primeCounter.countPrimes(30)).isEqualTo(11);
  }


}
