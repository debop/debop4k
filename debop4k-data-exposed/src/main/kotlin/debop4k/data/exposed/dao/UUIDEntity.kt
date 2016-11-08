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

import com.fasterxml.uuid.Generators
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

/**
 * Primary Key의 수형이 UUID 인 테이블을 표현합니다.
 */
open class UUIDIdTable(name: String = "", columnName: String = "id") : IdTable<UUID>(name) {
  override val id: Column<EntityID<UUID>>
      = uuid(columnName)
      .clientDefault { Generators.timeBasedGenerator().generate() }
      .primaryKey()
      .entityId()
}

abstract class UUIDEntity(id: EntityID<UUID>) : Entity<UUID>(id)

abstract class UUIDEntityClass<out E : UUIDEntity>(table: IdTable<UUID>) : EntityClass<UUID, E>(table)
