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

package debop4k.core.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * JDK 8 의 Stream 에 대한 테스트 코드
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 9.
 */
public class StreamTest {

  @Test
  public void collectByCollectors() {
    List<String> collected = Stream.of("a", "b", "c")
                                   .collect(toList());

    assertThat(asList("a", "b", "c")).isEqualTo(collected);
  }

  @Test
  public void collectByMap() {
    List<String> collected = Stream.of("a", "b", "hello")
                                   .map(String::toUpperCase)
                                   .collect(toList());
    assertThat(asList("A", "B", "HELLO")).isEqualTo(collected);
  }

  @Test
  public void filter() {
    List<String> beginningWithNumbers =
        Stream.of("a", "1abc", "abc1")
              .filter(s -> Character.isDigit(s.charAt(0)))
              .collect(toList());

    assertThat(Collections.singletonList("1abc")).isEqualTo(beginningWithNumbers);
  }

  @Test
  public void flatMap() {
    List<Integer> together = Stream.of(asList(1, 2), asList(3, 4))
                                   .flatMap(Collection::stream)
                                   .collect(Collectors.toList());
    assertThat(together).isEqualTo(asList(1, 2, 3, 4));
  }

  @Test
  public void comparator() {
    List<Track> tracks = asList(new Track("Bakai", 524),
                                new Track("Violets for Your Furs", 378),
                                new Track("Times was", 451));
    Track shortestTrack = tracks.stream()
                                .min(Comparator.comparing(Track::getLength))
                                .get();
    assertThat(shortestTrack).isEqualTo(tracks.get(1));
  }

  @Data
  @AllArgsConstructor
  public static class Track {
    private String name;
    private int length;
  }

  @Test
  public void reduce() {
    int count = Stream.of(1, 2, 3).reduce(0, (acc, x) -> acc + x);
    assertThat(count).isEqualTo(6);

    BinaryOperator<Integer> accumulator = (acc, x) -> acc + x;

    count = accumulator.apply(
        accumulator.apply(
            accumulator.apply(0, 1),
            2),
        3);
    assertThat(count).isEqualTo(6);
  }
}
