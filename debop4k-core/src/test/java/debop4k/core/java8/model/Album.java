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

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class Album implements Performance {

  private String name;
  private List<Track> tracks;
  private List<Artist> musicians;

  public Album(String name, List<Track> tracks, List<Artist> musicians) {
    this.name = name;
    this.tracks = tracks;
    this.musicians = musicians;
  }

  public Stream<Track> getTracks() {
    return tracks.stream();
  }

  @Override
  public Stream<Artist> getMusicians() {
    return musicians.stream();
  }

  public List<Track> getTrackList() {
    return Collections.unmodifiableList(tracks);
  }

  public List<Artist> getMusicianList() {
    return Collections.unmodifiableList(musicians);
  }

  public Artist getMainMusician() {
    return musicians.get(0);
  }

  public Album copy() {
    List<Track> tracks = getTracks().map(Track::copy).collect(toList());
    List<Artist> musicians = getMusicians().map(Artist::copy).collect(toList());

    return new Album(name, tracks, musicians);
  }
}
