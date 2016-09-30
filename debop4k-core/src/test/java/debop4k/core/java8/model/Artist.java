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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


@Getter
@Setter
public class Artist {

  private String name;
  private String nationality;
  private List<Artist> members = new ArrayList<>();

  public Artist(String name, String nationality) {
    this.name = name;
    this.nationality = nationality;
  }

  public Artist(String name, String nationality, List<Artist> members) {
    this(name, nationality);
    this.members = members;
  }

  public Stream<Artist> getMembers() {
    return members.stream();
  }

  public boolean isSolo() {
    return members.isEmpty();
  }

  @Override
  public String toString() {
    return getName();
  }

  public Artist copy() {
    List<Artist> members = getMembers().map(Artist::copy).collect(toList());
    return new Artist(name, nationality, members);
  }
}
