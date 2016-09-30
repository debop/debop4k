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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class WordCounting {

  private static final Pattern username = Pattern.compile("\\s+<username>(.*?)</username>");

  @SneakyThrows(IOException.class)
  public Map<String, Long> countUsers(InputStream stream) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      return reader.lines()
                   .flatMap(username::splitAsStream)
                   .map(String::trim)
                   .filter(word -> !word.isEmpty())
                   .collect(groupingBy(word -> word, counting()));

    }
  }

  private static final Pattern space = Pattern.compile("\\s+");

  @SneakyThrows(IOException.class)
  public Map<String, Long> countWords(InputStream stream) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      return reader.lines()
                   .flatMap(space::splitAsStream)
                   .map(String::trim)
                   .filter(word -> !word.isEmpty())
                   .sorted(Comparator.comparing(word -> word, Comparator.<String>reverseOrder()))
                   .collect(groupingBy(word -> word, counting()));

    }
  }

  @Test
  public void testCountWords() throws IOException {
    InputStream stream = null;
    try {
      stream = WordCounting.class.getResourceAsStream("/huckleberry_finn");
      Map<String, Long> wordCounts = countWords(stream);
      wordCounts.forEach((word, count) -> log.debug("{} -> {}", word, count));

      TreeSortedMap.newMap(wordCounts).forEach((word, count) -> log.debug("{} -> {}", word, count));
    } finally {
      if (stream != null)
        stream.close();
    }
  }
}
