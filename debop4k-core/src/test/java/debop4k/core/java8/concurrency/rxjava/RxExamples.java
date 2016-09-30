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

package debop4k.core.java8.concurrency.rxjava;

import debop4k.core.java8.model.Artist;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class RxExamples {

  private final List<Artist> savedArtists;
  private final List<String> savedArtistNames;

  public RxExamples(List<Artist> savedArtists) {
    this.savedArtists = savedArtists;
    this.savedArtistNames = savedArtists.stream()
                                        .map(Artist::getName)
                                        .collect(toList());
  }

  public Observable<Artist> search(String searchedName,
                                   String searchedNationality,
                                   int maxResults) {
    return
        getSavedArtistNames()
            .filter(name -> name.contains(searchedName))
            .flatMap(this::lookupArtist)
            .filter(artist -> artist.getNationality().contains(searchedNationality))
            .take(maxResults);
  }

  private Observable<String> getSavedArtistNames() {
    return Observable.from(savedArtistNames);
  }

  private Observable<Artist> lookupArtist(String name) {
    log.debug("search artist... name={}", name);
    Artist required = savedArtists.stream()
                                  .filter(a -> a.getName().contains(name))
                                  .peek(artist -> log.debug("find artist={}", artist))
                                  .findFirst()
                                  .get();
    log.debug("required={}", required);
    return Observable.just(required);
    //return Observable.from(new Artist[] { required });
  }

//  public void creationCodeSample() {
//    Observer<String> observer = new TestObserver<>();
//
//    observer.onNext("a");
//    observer.onNext("b");
//    observer.onNext("c");
//    observer.onCompleted();
//
//    observer.onError(new RuntimeException());
//  }
}
