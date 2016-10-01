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

package debop4k.mongodb.music;

import debop4k.mongodb.music.models.Album;
import debop4k.mongodb.music.models.Stars;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class AlbumRepositoryTest extends AbstractMusicTest {

  @Inject AlbumRepository repository;
  @Inject MongoTemplate template;

  @Before
  public void setup() {
    log.debug("delete all albums");
    repository.deleteAll();
    repository.save(albums);
  }

  @Test
  public void createAlbums() {
    assertSingleGruxAlbum(repository.findOne(bigWhiskey.getId()));
  }

  @Test
  public void findByConcreteTrackName() {
    assertSingleGruxAlbums(repository.findByTracksName("Grux"));
  }

  @Test
  public void findAllAlbumsByTrackNameLike() {
    assertSingleGruxAlbums(repository.findByTracksNameLike("*way*"));
  }

  @Test
  public void findAlbumsByTrackRating() {
    repository.deleteAll();
    bigWhiskey.getTracks().get(4).setRating(Stars.FOUR);
    repository.save(albums);

    assertSingleGruxAlbums(repository.findByTracksRatingGreaterThan(Stars.THREE));

    List<Album> loaded = repository.findByTracksRatingGreaterThan(Stars.FOUR);
    assertThat(loaded).isEmpty();
  }

  private void assertSingleGruxAlbums(List<Album> albums) {
    assertThat(albums).isNotNull().hasSize(1);
    assertThat(albums.get(0)).isExactlyInstanceOf(Album.class);
    assertSingleGruxAlbum(albums.get(0));
  }
}
