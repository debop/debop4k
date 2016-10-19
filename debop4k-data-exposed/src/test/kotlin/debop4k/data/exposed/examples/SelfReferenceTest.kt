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

package debop4k.data.exposed.examples

import debop4k.data.exposed.examples.DMLData.Cities
import debop4k.data.exposed.examples.DMLData.UserData
import debop4k.data.exposed.examples.DMLData.Users
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.EntityCache
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.junit.Test

/**
 * SelfReferenceTest
 * @author sunghyouk.bae@gmail.com
 */
@Suppress("unsued")
class SelfReferenceTest {

  @Test
  fun simpleTest() {

    assertThat(EntityCache.sortTablesByReferences(listOf(Cities))).isEqualTo(listOf(Cities))
    assertThat(EntityCache.sortTablesByReferences(listOf(Users))).isEqualTo(listOf(Users))

    val rightOrder = listOf(Cities, Users, UserData)
    val r1 = EntityCache.sortTablesByReferences(listOf(Cities, Users, UserData))
    val r2 = EntityCache.sortTablesByReferences(listOf(UserData, Cities, Users))
    val r3 = EntityCache.sortTablesByReferences(listOf(Users, Cities, UserData))

    assertThat(r1).isEqualTo(rightOrder)
    assertThat(r2).isEqualTo(rightOrder)
    assertThat(r3).isEqualTo(rightOrder)
  }

  @Test
  fun cycleReferencesCheckTest() {
    val cities = object : Table() {
      val id = integer("id").autoIncrement().primaryKey()
      val name = varchar("name", 50)
      val strange_id = varchar("strange_id", 10)
    }
    val users = object : Table() {
      val id = varchar("id", 10).primaryKey()
      val name = varchar("name", length = 50)
      val cityId = (integer("city_id") references cities.id).nullable()
    }

    val noReferenceTable = object : Table() {
      val id = varchar("id", 10).primaryKey()
      val col1 = varchar("col1", 10)
    }
    val refereeTable = object : Table() {
      val id = varchar("id", 10).primaryKey()
      val ref = reference("ref", noReferenceTable.id)
    }
    val referencedTable = object : IntIdTable() {
      val col3 = varchar("col3", 10)
    }

    val strangeTable = object : Table() {
      val id = varchar("id", 10).primaryKey()
      val user_id = varchar("user_id", 10) references users.id
      val comment = varchar("comment", 30)
      val value = integer("value")
    }

    with(cities) {
      strange_id.references(strangeTable.id)
    }

    val sortedTable = EntityCache.sortTablesByReferences(listOf(cities, users, strangeTable, noReferenceTable, refereeTable, referencedTable))

    assertThat(sortedTable.indexOf(referencedTable)).isIn(0, 1)
    assertThat(sortedTable.indexOf(noReferenceTable)).isIn(0, 1)
    assertThat(sortedTable.indexOf(refereeTable)).isEqualTo(2)

    assertThat(sortedTable.indexOf(cities)).isIn(3, 4, 5)
    assertThat(sortedTable.indexOf(users)).isIn(3, 4, 5)
    assertThat(sortedTable.indexOf(strangeTable)).isIn(3, 4, 5)
  }

}