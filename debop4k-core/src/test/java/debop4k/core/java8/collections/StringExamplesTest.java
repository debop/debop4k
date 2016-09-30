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

import debop4k.core.java8.model.Artist;
import debop4k.core.java8.model.SampleData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class StringExamplesTest {

  @Test
  public void beatlesExample() {
    StringJoiner joiner = new StringJoiner(", ", "[", "]");
    joiner.add("John");
    joiner.add("Paul");
    joiner.add("Ringo");
    assertEquals("[John, Paul, Ringo]", joiner.toString());
  }

  @Test
  public void allStringJoins() {
    List<Function<List<Artist>, String>> formatters = asList(
        StringExamples::formatArtists,
        StringExamples::formatArtistsByStringCollector,
        StringExamples::formatArtistsByStringCombiner,
        StringExamples::formatArtistsReducing
                                                            );

    formatters.forEach(formatter -> {
      log.debug("Testing: {}", formatter.toString());
      String result = formatter.apply(SampleData.getThreeArtists());
      assertEquals("[John Coltrane, John Lennon, The Beatles]", result);

      result = formatter.apply(Collections.emptyList());
      assertEquals("[]", result);
    });
  }

  @Test
  public void explicitForLoop() {
    String result = StringExamples.formatArtists(SampleData.getThreeArtists());
    assertEquals("[John Coltrane, John Lennon, The Beatles]", result);
  }
}
