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
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class AlbumIntegrationTest extends AbstractMusicTest {

  @Before
  public void setup() {
    super.setup();
    operations.insertAll(albums);
  }

  @Test
  public void lookupAlbumByIdWithQueryBuilder() {
    Query query = Query.query(Criteria.where("_id").is(bigWhiskey.getId()));
    assertSingleGruxAlbum(query);
  }

  @Test
  public void lookupAlbumByIdUsingJson() {
    Query query = parseQuery("{ _id : '%s'}", bigWhiskey.getId());
    assertSingleGruxAlbum(query);
  }

  @Test
  public void lookupAlbumByTrackNameUsingJson() {
    Query query = parseQuery("{ 'tracks.name' : 'Wheels' }");
    assertSinglePursuitAlbum(query);
  }

  @Test
  public void lookupAlbumByTrackNameWithQueryBuilder() {
    Query query = Query.query(Criteria.where("tracks.name").is("Grux"));
    assertSingleGruxAlbum(query);
  }

  @Test
  public void lookupAlbumByTrackNamePattern() {
    Query query = parseQuery("{ 'tracks.name' : {'$regex' : '.*it.*', '$options' : '' } }");
    assertBothAlbums(operations.find(query, Album.class, COLLECTION_NAME));
  }

  @Test
  public void lookupAlbumByTrackNamePatternWithQueryBuilder() {
    Query query = Query.query(Criteria.where("tracks.name").regex(".*it.*"));
    assertBothAlbums(operations.find(query, Album.class, COLLECTION_NAME));
  }

  private Query parseQuery(String query, Object... arguments) {
    return new BasicQuery(String.format(query, arguments));
  }
}
