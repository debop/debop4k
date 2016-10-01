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
import debop4k.mongodb.music.models.Track;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MusicMongoConfiguration.class})
public abstract class AbstractMusicTest {

  public static String COLLECTION_NAME = "album";

  @Inject MongoTemplate operations;

  protected Album bigWhiskey;
  protected Album thePursuit;

  protected List<Album> albums;

  @Before
  public void setupClass() {
    init();
  }

  @Before
  public void setup() {
    operations.dropCollection(COLLECTION_NAME);
  }

  public void init() {
    albums = FastList.newList();

    bigWhiskey = new Album("Big Whiskey and the Groo Grux King", "Dave Matthews Band");
    bigWhiskey.add(new Track(0, "Grux"));
    bigWhiskey.add(new Track(1, "Shake me lika a monkey"));
    bigWhiskey.add(new Track(2, "Funny the way it is"));
    bigWhiskey.add(new Track(3, "Lying in the hands newPeriod God"));
    bigWhiskey.add(new Track(4, "Why I am"));
    bigWhiskey.add(new Track(5, "Dive in"));
    bigWhiskey.add(new Track(6, "Spaceman"));
    bigWhiskey.add(new Track(7, "Squirm"));
    bigWhiskey.add(new Track(8, "Alligator pie"));
    bigWhiskey.add(new Track(9, "Seven"));
    bigWhiskey.add(new Track(10, "Time bomb"));
    bigWhiskey.add(new Track(11, "My baby blue"));
    bigWhiskey.add(new Track(12, "You and me"));

    bigWhiskey.setPublishDate(DateTime.now().minusYears(1));

    albums.add(bigWhiskey);

    thePursuit = new Album("The Pursuit", "Jamie Cullum");
    thePursuit.add(new Track(0, "Just one newPeriod those things"));
    thePursuit.add(new Track(1, "I'm all over it"));
    thePursuit.add(new Track(2, "Wheels"));
    thePursuit.add(new Track(3, "If I ruled the world"));
    thePursuit.add(new Track(4, "You and me are gone"));
    thePursuit.add(new Track(5, "Don't stop the music"));
    thePursuit.add(new Track(6, "Love ain't gonna let you down"));
    thePursuit.add(new Track(7, "Mixtape"));
    thePursuit.add(new Track(8, "I think, I love"));
    thePursuit.add(new Track(9, "We run things"));
    thePursuit.add(new Track(10, "Not while I am around"));
    thePursuit.add(new Track(11, "Music is through"));
    thePursuit.add(new Track(12, "Grand Torino"));
    thePursuit.add(new Track(13, "Grace is gone"));

    thePursuit.setPublishDate(DateTime.now().minusYears(2));

    albums.add(thePursuit);
  }

  protected void assertSingleGruxAlbum(Query query) {
    List<Album> results = operations.find(query, Album.class, COLLECTION_NAME);
    assertThat(results).isNotNull().hasSize(1);

    assertSingleGruxAlbum(results.get(0));
  }

  protected void assertSingleGruxAlbum(Album album) {
    assertThat(album).isNotNull();
    assertThat(album.getId()).isEqualTo(bigWhiskey.getId());
    assertThat(album.getTitle()).isEqualTo(bigWhiskey.getTitle());
    assertThat(album).isEqualTo(bigWhiskey);
    assertThat(album.getTracks().size()).isEqualTo(bigWhiskey.getTracks().size());
  }

  protected void assertSinglePursuitAlbum(Query query) {
    List<Album> results = operations.find(query, Album.class, COLLECTION_NAME);
    assertThat(results).isNotNull().hasSize(1);

    assertSinglePursuitAlbum(results.get(0));
  }

  protected void assertSinglePursuitAlbum(Album album) {
    assertThat(album).isNotNull();
    assertThat(album.getId()).isEqualTo(thePursuit.getId());
    assertThat(album.getTitle()).isEqualTo(thePursuit.getTitle());
    assertThat(album).isEqualTo(thePursuit);
    assertThat(album.getTracks().size()).isEqualTo(thePursuit.getTracks().size());
  }

  protected void assertBothAlbums(List<Album> albums) {
    assertThat(albums).isNotNull().hasSize(2);

    for (Album album : albums) {
      if (album.getId().equals(bigWhiskey.getId())) {
        assertSingleGruxAlbum(album);
      } else if (album.getId().equals(thePursuit.getId())) {
        assertSinglePursuitAlbum(album);
      } else {
        fail("Album is neither Grux or Pursuit!");
      }
    }
  }
}
