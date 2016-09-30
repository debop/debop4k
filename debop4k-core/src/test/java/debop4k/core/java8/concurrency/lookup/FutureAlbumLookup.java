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

package debop4k.core.java8.concurrency.lookup;

import debop4k.core.java8.model.Album;
import debop4k.core.java8.model.Artist;
import debop4k.core.java8.model.Track;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class FutureAlbumLookup implements AlbumLookup {

  private static final ExecutorService service = Executors.newFixedThreadPool(2);

  private final List<Track> tracks;
  private final List<Artist> artists;

  public FutureAlbumLookup(List<Track> tracks, List<Artist> artists) {
    this.tracks = tracks;
    this.artists = artists;
  }

  @Override
  public Album lookupByName(String albumName) {
    return null;
  }

  private Future<List<Artist>> lookupArtists(String albumName, Credentials credentials) {
    return service.submit(() -> {
      fakeWatingForExternalWebService();
      return artists;
    });
  }

  private Future<List<Track>> lookupTracks(String albumName, Credentials credentials) {
    return service.submit(() -> {
      fakeWatingForExternalWebService();
      return tracks;
    });
  }

  private Future<Credentials> loginTo(String serviceName) {
    return service.submit(() -> {
      if ("track".equals(serviceName))
        fakeWatingForExternalWebService();
      return new Credentials();
    });
  }

  private void fakeWatingForExternalWebService() throws InterruptedException {
    Thread.sleep(1000);
  }
}
