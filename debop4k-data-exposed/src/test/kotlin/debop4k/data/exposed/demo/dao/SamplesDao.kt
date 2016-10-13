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

package debop4k.data.exposed.demo.dao

import debop4k.data.DataSources
import debop4k.data.exposed.AbstractExposedTest
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

object Cities : IntIdTable() {
  val name = varchar("name", 50).uniqueIndex()
}

object Users : IntIdTable() {
  val name = varchar("name", 50).index()
  val city = reference("city", Cities)
  val age = integer("age").nullable()
}

class City(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<City>(Cities)

  var name by Cities.name
  val users by User referrersOn Users.city
}

class User(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<User>(Users)

  var name by Users.name
  var city by City referencedOn Users.city
  var age by Users.age
}

class SamplesDaoTest : AbstractExposedTest() {

  @Test
  fun testSimpleDao() {

    val dataSource = DataSources.ofEmbeddedH2()
    Database.connect(dataSource)

    transaction {
      logger.addLogger(StdOutSqlLogger())

      SchemaUtils.create(Cities, Users)

      val stPete = City.new { name = "St. Petersburg" }
      val munich = City.new { name = "Munich" }

      User.new {
        name = "a"
        city = stPete
        age = 5
      }
      User.new {
        name = "b"
        city = munich
        age = 27
      }
      User.new {
        name = "c"
        city = munich
        age = 42
      }

      println("Cities: ${City.all().joinToString { it.name }}")
      println("Users in ${stPete.name}: ${stPete.users.joinToString { it.name }}")
      println("Adults: ${User.find { Users.age greaterEq 18 }.joinToString { it.name }}")

      SchemaUtils.drop(Users, Cities)
    }
  }
}