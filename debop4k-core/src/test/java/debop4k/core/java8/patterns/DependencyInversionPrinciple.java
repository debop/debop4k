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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class DependencyInversionPrinciple {

  public static interface HeadingFinder {
    List<String> findHeadings(Reader reader);
  }

  public static class NoDIP implements HeadingFinder {
    @Override
    public List<String> findHeadings(Reader input) {
      try (BufferedReader reader = new BufferedReader(input)) {
        return reader.lines()
                     .filter(line -> line.endsWith(":"))
                     .map(line -> line.substring(0, line.length() - 1))
                     .collect(toList());
      } catch (IOException e) {
        throw new HandlingLookupException(e);
      }
    }
  }

  public static class ExtractedDIP implements HeadingFinder {
    @Override
    public List<String> findHeadings(Reader input) {
      return withLinesOf(input,
                         lines -> lines.filter(line -> line.endsWith(":"))
                                       .map(line -> line.substring(0, line.length() - 1))
                                       .collect(toList()),
                         HandlingLookupException::new);
    }

    private <T> T withLinesOf(Reader input,
                              Function<Stream<String>, T> handler,
                              Function<IOException, RuntimeException> error) {

      try (BufferedReader reader = new BufferedReader(input)) {
        return handler.apply(reader.lines());
      } catch (IOException e) {
        throw error.apply(e);
      }
    }
  }
}
