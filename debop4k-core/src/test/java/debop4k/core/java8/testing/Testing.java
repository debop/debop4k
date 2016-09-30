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

package debop4k.core.java8.testing;

import debop4k.core.java8.model.Album;
import debop4k.core.java8.model.Artist;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class Testing {

  public static List<String> allToUpperCase(List<String> words) {
    return words.stream()
                .map(String::toUpperCase)
                .collect(toCollection(FastList::new));
  }

  public static List<String> elementFirstToUpperCaseLambdas(List<String> words) {
    return words.stream()
                .map(word -> {
                  if (!word.isEmpty()) {
                    char firstChar = Character.toUpperCase(word.charAt(0));
                    return firstChar + word.substring(1);
                  } else {
                    return word;
                  }
                })
                .collect(toCollection(FastList::new));
  }

  public static String firstToUpper(String word) {
    if (!word.isEmpty()) {
      char firstChar = Character.toUpperCase(word.charAt(0));
      return firstChar + word.substring(1);
    } else {
      return word;
    }
  }

  public static List<String> elementFirstToUpper(List<String> words) {
    return words.stream()
                .map(Testing::firstToUpper)
                .collect(toCollection(FastList::new));
  }

  public static Set<String> imerativeNationalityReport(Album album) {
    Set<String> nationalities = new HashSet<>();
    album.getMusicianList()
         .stream()
         .filter(artist -> artist.getName().startsWith("The"))
         .forEach(artist -> {
           String nationality = artist.getNationality();
           log.debug("Found nationality={}", nationality);
           nationalities.add(nationality);
         });
    return nationalities;
  }

  public static Set<String> forEachLoggingFailure(Album album) {
    album.getMusicians()
         .filter(artist -> artist.getName().startsWith("The"))
         .map(Artist::getNationality)
         .forEach(nationality -> log.debug("Found: {}", nationality));

    Set<String> nationalities =
        album.getMusicians()
             .filter(artist -> artist.getName().startsWith("The"))
             .map(Artist::getNationality)
             .collect(toSet());
    return nationalities;
  }

  public static Set<String> nationalityReportUsingPeek(Album album) {
    Set<String> nationalities =
        album.getMusicians()
             .filter(artist -> artist.getName().startsWith("The"))
             .map(Artist::getNationality)
             .peek(nation -> log.debug("Found nationality={}", nation))
             .collect(toSet());
    return nationalities;
  }
}
