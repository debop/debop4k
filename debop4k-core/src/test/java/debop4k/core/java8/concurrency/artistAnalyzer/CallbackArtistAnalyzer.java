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

package debop4k.core.java8.concurrency.artistAnalyzer;

import debop4k.core.java8.model.Artist;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class CallbackArtistAnalyzer implements ArtistAnalyzer {

  private final Function<String, Artist> artistLookupService;

  public CallbackArtistAnalyzer(Function<String, Artist> artistLookupService) {
    this.artistLookupService = artistLookupService;
  }

  @Override
  public void isLargerGroup(String artistName, String otherArtistName, Consumer<Boolean> handler) {
    boolean isLarger = getNumberOfMembers(artistName) > getNumberOfMembers(otherArtistName);
    handler.accept(isLarger);
  }

  private long getNumberOfMembers(String artistName) {
    return artistLookupService.apply(artistName)
                              .getMembers()
                              .count();
  }
}
