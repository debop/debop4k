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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * ListTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 10.
 */
@Slf4j
public class ListTest {

  @Test
  public void ordering() {
    List<Integer> numbers = asList(1, 2, 3, 4);
    List<Integer> sameOrder = numbers.stream().collect(toList());
    assertEquals(numbers, sameOrder);
  }

  @Test
  public void setOrdering() {
    Set<Integer> numbers = Sets.mutable.ofAll(asList(4, 3, 2, 1));
    List<Integer> sameOrder = numbers.stream().collect(toList());

    assertNotEquals(asList(4, 3, 2, 1), sameOrder);

    List<Integer> ascending = numbers.stream().sorted().collect(toList());
    assertEquals(asList(1, 2, 3, 4), ascending);
  }

  @Test
  public void mapOrdering() {
    List<Integer> numbers = asList(1, 2, 3, 4, 10);
    List<Integer> stillOrdered = numbers.stream()
                                        .map(x -> x + 1)
                                        .collect(toList());

    assertThat(stillOrdered).containsExactly(2, 3, 4, 5, 11);

    Set<Integer> unordered = Sets.mutable.ofAll(numbers);
    List<Integer> stillUnordered = unordered.stream()
                                            .map(x -> x + 1)
                                            .collect(toList());

    log.debug("unordered={}, stillUnordered={}", unordered, stillUnordered);

    assertThat(stillUnordered).containsExactly(2, 3, 4, 5, 11);
  }

  @Test
  public void collectors() {
    List<Integer> numbers = asList(1, 2, 3, 4);
    FastList<Integer> fastList = numbers.stream().collect(toCollection(FastList::new));
    assertEquals(asList(1, 2, 3, 4), fastList);
  }

  @Test
  public void toValues() {
    List<Integer> numbers = asList(1, 2, 3, 4);
    // Function<Integer, Integer> getCount = n -> n;
    Optional<Integer> max = numbers.stream().collect(maxBy(comparing(n -> n)));
    assertSame(4, max.orElse(-1));
  }

  @Test
  public void average() {
    List<Integer> numbers = asList(1, 2, 3, 4);

    double avg = numbers.stream().collect(averagingDouble(x -> x));
    assertEquals(2.5, avg, 1e-8);
  }

  @Test
  public void partitioning() {
    List<Integer> numbers = asList(1, 2, 3, 4);

    Map<Boolean, List<Integer>> partitions = numbers.stream().collect(partitioningBy(n -> n % 2 == 0));

    assertEquals(asList(1, 3), partitions.get(false));
    assertEquals(asList(2, 4), partitions.get(true));
  }

  @Test
  public void makeString() {
    List<String> alphabet = asList("a", "b", "c", "d");

    String joined = alphabet.stream().collect(Collectors.joining(", ", "[", "]"));
    log.debug("joined={}", joined);
    assertEquals("[a, b, c, d]", joined);
  }

  @Test
  public void groupBy() {
    List<Integer> numbers = asList(1, 2, 3, 4);

    Function<Integer, String> classifier = x -> (x % 2 == 0) ? "even" : "odd";
    Map<String, List<Integer>> boolGroups = numbers.stream()
                                                   .collect(groupingBy(classifier));

    assertEquals(asList(1, 3), boolGroups.get("odd"));
    assertEquals(asList(2, 4), boolGroups.get("even"));
  }


}
