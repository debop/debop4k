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

package debop4k.data.exposed.demo.sql

import debop4k.data.DataSources
import debop4k.data.exposed.AbstractExposedTest
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

object Cities : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val name = varchar("name", 50).uniqueIndex()
}

object Users : Table() {
  val id = varchar("id", 10).primaryKey()
  val name = varchar("name", length = 50)
  val cityId = (integer("city_id") references Cities.id).nullable()
}

class SimpleSqlTest : AbstractExposedTest() {

  @Test
  fun testSqlDDL() {

    val database = Database.connect(DataSources.ofEmbeddedH2())


    transaction {
      logger.addLogger(StdOutSqlLogger())

      SchemaUtils.create(Cities, Users)

      val saintPetersburgId = Cities.insert {
        it[name] = "St. Petersburg"
      } get Cities.id

      val munichId = Cities.insert {
        it[name] = "Munich"
      } get Cities.id

      val pragueId = Cities.insert {
        it.update(name, stringLiteral("   Prague   ").trim().substring(1, 2))
      }[Cities.id]

      val pragueName = Cities.select { Cities.id eq pragueId }.single()[Cities.name]

      assertThat(pragueName).isEqualTo("Pr")

      Users.insert {
        it[id] = "andrey"
        it[name] = "Andrey"
        it[cityId] = saintPetersburgId
      }
      Users.insert {
        it[id] = "sergey"
        it[name] = "Sergey"
        it[cityId] = munichId
      }
      Users.insert {
        it[id] = "eugene"
        it[name] = "Eugene"
        it[cityId] = munichId
      }
      Users.insert {
        it[id] = "alex"
        it[name] = "Alex"
        it[cityId] = null
      }
      Users.insert {
        it[id] = "smth"
        it[name] = "Something"
        it[cityId] = null
      }

      Users.update({ Users.id eq "alex" }) {
        it[name] = "Alexey"
      }

      Users.deleteWhere { Users.name like "%thing" }

      println("All cities:")

      Cities.selectAll().forEach { city ->
        println("${city[Cities.id]}: ${city[Cities.name]}")
      }

      println("Manual join:")
      (Users innerJoin Cities).slice(Users.name, Cities.name)
          .select {
            (Users.id.eq("andrey") or Users.name.eq("Sergey")) and
                Users.id.eq("sergey") and
                Users.cityId.eq(Cities.id)
          }.forEach {
        println("${it[Users.name]} lives in ${it[Cities.name]}")
      }

      println("Join with foreign key:")

      (Users leftJoin Cities).slice(Users.name, Users.cityId, Cities.name)
          .select { Cities.name.eq("St. Petersburg") or Users.cityId.isNull() }
          .forEach {
            if (it[Users.cityId] != null) {
              println("${it[Users.name]} lives in ${it[Cities.name]}")
            } else {
              println("${it[Users.name]} lives nowhere")
            }
          }

      println("Functions and group by:")

      val groupBy = (Cities leftJoin Users)
          .slice(Cities.name, Users.id.count())
          .selectAll()
          .groupBy(Cities.name)
          .orderBy(Users.id.count())

      groupBy.forEach {
        val cityName = it[Cities.name]
        val userCount = it[Users.id.count()]

        if (userCount > 0) {
          println("$userCount user(s) live(s) in $cityName")
        } else {
          println("Nobody lives in $cityName")
        }
      }

      SchemaUtils.drop(Users, Cities)

    }
  }
}