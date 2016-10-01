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

package debop4k.mongodb.logback

import debop4k.core.logback.LogDocument
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Logback 로그 정보를 MongoDB 저장하기 위한 MongoDB 용 엔티티 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Document
class MongodbLogDocument : LogDocument() {

  @Id
  var id: ObjectId? = null

  companion object {
    private const val serialVersionUID = 930214373929865557L
  }
}