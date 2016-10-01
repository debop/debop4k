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

package debop4k.mongodb.bulk

import debop4k.mongodb.AbstractMongoDocument
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

/**
 * BulkEntity
 * @author sunghyouk.bae@gmail.com
 */
@Document
@CompoundIndex(name = "ix_bulk_entity", def = "{ 'longitude': 1, 'latitude': 1}", background = true)
open class BulkEntity : AbstractMongoDocument() {

  var name: String? = null
  var longitude: Double? = null
  var latitude: Double? = null
}