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
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.reducing;

@Slf4j
public final class StringExamples {

  private StringExamples() {}

  public static String formatArtists(List<Artist> artists) {
    return artists.stream()
                  .map(Artist::getName)
                  .collect(joining(", ", "[", "]"));
  }

  public static String formatArtistsByStringCombiner(List<Artist> artists) {
    return artists.stream()
                  .map(Artist::getName)
                  .reduce(new StringCombiner(", ", "[", "]"),
                          StringCombiner::add,
                          StringCombiner::merge)
                  .toString();
  }

  public static String formatArtistsByStringCollector(List<Artist> artists) {
    return artists.stream()
                  .map(Artist::getName)
                  .collect(new StringCollector(", ", "[", "]"));
  }

  public static String formatArtistsReducing(List<Artist> artists) {
    return artists.stream()
                  .map(Artist::getName)
                  .collect(reducing(new StringCombiner(", ", "[", "]"),
                                    name -> new StringCombiner(", ", "[", "]").add(name),
                                    StringCombiner::merge))
                  .toString();
  }
}
