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
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Repository
public interface AlbumRepository extends MongoRepository<Album, ObjectId> {

  List<Album> findByTracksName(String name);

  List<Album> findByTracksNameLike(String nameToMatch);

  List<Album> findByTracksRatingGreaterThan(Stars rating);
}
