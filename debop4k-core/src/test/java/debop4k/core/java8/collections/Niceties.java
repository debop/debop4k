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

import debop4k.core.java8.model.Album;
import debop4k.core.java8.model.Artist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class Niceties {

  abstract class ArtistService {
    protected Map<String, Artist> artistCache = new HashMap<>();

    public abstract Artist getArtist(String name);

    protected Artist readArtistFromDB(String name) {
      return new Artist(name, "UK");
    }
  }

  class OldArtistService extends ArtistService {
    @Override
    public Artist getArtist(String name) {
      Artist artist = artistCache.get(name);
      if (artist == null) {
        artist = readArtistFromDB(name);
        artistCache.put(name, artist);
      }
      return artist;
    }
  }

  class Java8ArtistService extends ArtistService {
    public Artist getArtist(String name) {
      return artistCache.computeIfAbsent(name, this::readArtistFromDB);
    }
  }

  class ImperativeCount {
    public Map<Artist, Integer> countAlbums(Map<Artist, List<Album>> albumsByArtist) {
      Map<Artist, Integer> countOfAlbums = new HashMap<>();
      for (Map.Entry<Artist, List<Album>> entry : albumsByArtist.entrySet()) {
        Artist artist = entry.getKey();
        List<Album> albums = entry.getValue();
        countOfAlbums.put(artist, albums.size());
      }
      return countOfAlbums;
    }
  }

  class Java8Count {
    public Map<Artist, Integer> countAlbums(Map<Artist, List<Album>> albumsByArtist) {
      Map<Artist, Integer> countOfAlbums = new HashMap<>();
      albumsByArtist.forEach((artist, albums) -> {
        countOfAlbums.put(artist, albums.size());
      });
      return countOfAlbums;
    }
  }
}
