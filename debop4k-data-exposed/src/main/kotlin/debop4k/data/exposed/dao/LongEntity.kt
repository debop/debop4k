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

package debop4k.data.exposed.dao

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column

/**
 * Primary Key의 수형이 Long인 테이블을 표현합니다.
 */
open class LongIdTable(name: String = "", columnName: String = "id") : IdTable<Long>(name) {
  override val id: Column<EntityID<Long>>
      = long(columnName).autoIncrement().primaryKey().entityId()
}

abstract class LongEntity(id: EntityID<Long>) : Entity<Long>(id)

abstract class LongEntityClass<E : LongEntity>(table: IdTable<Long>) : EntityClass<Long, E>(table)