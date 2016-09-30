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

import java.util.stream.IntStream;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class SingleResponsibilityPrinciple {

  public static interface PrimeCounter {
    long countPrimes(int upTo);
  }

  public static class ImperativeSingleMethodPrimeCounter implements PrimeCounter {
    @Override
    public long countPrimes(int upTo) {
      long count = 0;
      for (int i = 1; i < upTo; i++) {
        boolean isPrime = true;
        for (int j = 2; j < i; j++) {
          if (i % j == 0) {
            isPrime = false;
            break;
          }
        }
        if (isPrime) {
          count++;
        }
      }
      return count;
    }
  }

  public static class ImperativeRefactoredPrimeCounter implements PrimeCounter {
    @Override
    public long countPrimes(int upTo) {
      long count = 0;
      for (int i = 1; i < upTo; i++) {
        if (isPrime(i)) {
          count++;
        }
      }
      return count;
    }

    private boolean isPrime(int n) {
      for (int i = 2; i < n; i++) {
        if (n % i == 0) {
          return false;
        }
      }
      return true;
    }
  }

  public static class FunctionalPrimeCount implements PrimeCounter {
    @Override
    public long countPrimes(int upTo) {
      return IntStream.range(1, upTo)
                      .filter(this::isPrime)
                      .count();
    }

    private boolean isPrime(int n) {
      return IntStream.range(2, n).allMatch(x -> (n % x) != 0);
    }
  }

  public static class ParallelFunctionalPrimeCount implements PrimeCounter {
    @Override
    public long countPrimes(int upTo) {
      return IntStream.range(1, upTo)
                      .parallel()
                      .filter(this::isPrime)
                      .count();
    }

    private boolean isPrime(int n) {
      return IntStream.range(2, n).allMatch(x -> (n % x) != 0);
    }
  }
}
