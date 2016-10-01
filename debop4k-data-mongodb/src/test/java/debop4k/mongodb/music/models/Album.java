/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.mongodb.music.models;

import debop4k.core.utils.Hashx;
import debop4k.mongodb.AbstractMongoDocument;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class Album extends AbstractMongoDocument {

  @Indexed
  private String title;
  private String artist;
  private DateTime publishDate;
  private List<Track> tracks = FastList.newList();

  public Album(String title, String artist) {
    this.title = title;
    this.artist = artist;
  }

  public void add(Track track) {
    this.tracks.add(track);
  }

  @Override
  public int hashCode() {
    return Hashx.compute(title, artist, publishDate);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  private static final long serialVersionUID = -3696965225367370870L;
}
