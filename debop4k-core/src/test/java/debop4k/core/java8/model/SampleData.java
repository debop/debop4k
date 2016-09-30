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

package debop4k.core.java8.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author sunghyouk.bae@gmail.com
 */
public final class SampleData {

  private SampleData() {}

  public static final Artist johnColtrane = new Artist("John Coltrane", "US");

  public static final Artist johnLennon = new Artist("John Lennon", "UK");
  public static final Artist paulMcCartney = new Artist("Paul McCartney", "UK");
  public static final Artist georgeHarrison = new Artist("George Harrison", "UK");
  public static final Artist ringoStarr = new Artist("Ringo Starr", "UK");

  public static final List<Artist> membersOfTheBeatles = Arrays.asList(johnLennon,
                                                                       paulMcCartney,
                                                                       georgeHarrison,
                                                                       ringoStarr);

  public static final Artist theBeatles = new Artist("The Beatles", "UK", membersOfTheBeatles);

  public static final Album aLoveSupreme = new Album("A Love Supreme",
                                                     asList(new Track("Acknowledgement", 467), new Track("Resolution", 442)),
                                                     singletonList(johnColtrane));

  public static final Album sampleShortAlbum = new Album("sample Short Album",
                                                         singletonList(new Track("short track", 30)),
                                                         singletonList(johnColtrane));

  public static final Album manyTrackAlbum = new Album("sample Short Album",
                                                       asList(new Track("short track", 30),
                                                              new Track("short track 2", 30),
                                                              new Track("short track 3", 30),
                                                              new Track("short track 4", 30),
                                                              new Track("short track 5", 30)),
                                                       asList(johnColtrane));

  public static Stream<Album> albums = Stream.of(aLoveSupreme);

  public static Stream<Artist> threeArtists() {
    return Stream.of(johnColtrane, johnLennon, theBeatles);
  }

  public static List<Artist> getThreeArtists() {
    return Arrays.asList(johnColtrane, johnLennon, theBeatles);
  }
}
