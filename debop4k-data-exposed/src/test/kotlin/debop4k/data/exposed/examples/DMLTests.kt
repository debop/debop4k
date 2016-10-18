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

import org.jetbrains.exposed.sql.*

object DMLData {

  object Cities : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
  }

  object Users : Table() {
    val id = varchar("id", 10).primaryKey()
    val name = varchar("name", length = 50)
    val cityId = (integer("city_id") references Cities.id).nullable() // optReference
  }

  object UserData : Table() {
    val user_id = varchar("user_id", 10) references Users.id
    val comment = varchar("comment", 30)
    val value = integer("value")
  }

  enum class E {
    ONE,
    TWO,
    THREE
  }

  object Misc : Table() {
    val n = integer("n")
    val nn = integer("nn").nullable()

    val d = date("d")
    val dn = date("dn").nullable()

    val t = datetime("t")
    val tn = datetime("tn").nullable()

    val e = enumeration("e", E::class.java)
    val en = enumeration("en", E::class.java).nullable()

    val s = varchar("s", 100)
    val sn = varchar("sn", 100).nullable()

    val dc = decimal("dc", 12, 2)
    val dcn = decimal("dcn", 12, 2).nullable()
  }
}

class DMLTests : DatabaseTestBase() {

  fun withCitiesAndUsers(exclude: List<TestDB> = emptyList(),
                         statement: Transaction.(cities: DMLData.Cities, users: DMLData.Users, userData: DMLData.UserData) -> Unit) {

    val Users = DMLData.Users
    val Cities = DMLData.Cities
    val UserData = DMLData.UserData

    withTables(exclude, Cities, Users, UserData) {
      val saintPetersburgId = Cities.insert {
        it[name] = "St. Petersburg"
      }[Cities.id]

      val munichId = Cities.insert {
        it[name] = "Munich"
      }[Cities.id]

      Cities.insert {
        it[name] = "Prague"
      }

      Users.insert {
        it[id] = "andrey"
      }

    }

  }
}