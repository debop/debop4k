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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class CompletableAlbumLookup implements AlbumLookup {

  private static final ExecutorService service = ForkJoinPool.commonPool(); //Executors.newFixedThreadPool(4);

  private final List<Track> tracks;
  private final List<Artist> artists;

  public CompletableAlbumLookup(List<Track> tracks, List<Artist> artists) {
    this.tracks = tracks;
    this.artists = artists;
  }

  @Override
  public Album lookupByName(String albumName) {
    CompletableFuture<List<Artist>> artistLookup =
        loginTo("artist")
            .thenCompose(credentials -> lookupArtists(albumName, credentials));

    return loginTo("track")
        .thenCompose(credentials -> lookupTracks(albumName, credentials))
        .thenCombine(artistLookup, (tracks, artists) -> new Album(albumName, tracks, artists))
        .join();
  }

  private Track track;
  private Artist artist;

  CompletableFuture<Track> lookupTrack(String id) {
    return CompletableFuture.supplyAsync(() -> track, service);
  }

  CompletableFuture<Artist> lookupArtist(String id) {
    CompletableFuture<Artist> future = new CompletableFuture<>();
    startJob(future);
    return future;
  }

  private void startJob(CompletableFuture<Artist> future) {
    // scala Promuse.success(v) 와 같다.
    future.complete(artist);
  }

  private void processExceptionally(CompletableFuture<Album> future, String name) {
    // scala Promise.fail(t) 와 같다.
    future.completeExceptionally(new AlbumLookupException("Unable to find " + name));
  }


  // FAKE LOOKUP METHODS -------------------------

  private CompletableFuture<List<Artist>> lookupArtists(String albumname, Credentials credentials) {
    return CompletableFuture.completedFuture(artists);
  }

  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private CompletableFuture<List<Track>> lookupTracks(String albumName, Credentials credentials) {
    return CompletableFuture.supplyAsync(() -> {
      sleep(1000);
      return tracks;
    }, service);
  }

  private CompletableFuture<Credentials> loginTo(String serviceName) {
    return CompletableFuture.supplyAsync(() -> {
      if ("artist".equals(serviceName))
        sleep(1000);
      return new Credentials();
    }, service);
  }
}
