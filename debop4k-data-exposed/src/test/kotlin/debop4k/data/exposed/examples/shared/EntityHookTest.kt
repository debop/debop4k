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
 */

package debop4k.data.exposed.examples.shared

import debop4k.data.exposed.examples.DatabaseTestBase
import debop4k.data.exposed.examples.shared.EntityHookData.Cities
import debop4k.data.exposed.examples.shared.EntityHookData.City
import debop4k.data.exposed.examples.shared.EntityHookData.Country
import debop4k.data.exposed.examples.shared.EntityHookData.User
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.EntityChangeType.Removed
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test


object EntityHookData {

  object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val age = integer("age")
  }

  object Cities : IntIdTable() {
    val name = varchar("name", 50)
    val country = reference("country", Countries)
  }

  object Countries : IntIdTable() {
    val name = varchar("name", 50)
  }

  object UsersToCities : Table() {
    val user = reference("user", Users, CASCADE)
    val city = reference("city", Cities, CASCADE)
  }

  class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var age by Users.age
    var cities by City via UsersToCities
  }

  class City(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<City>(Cities)

    var name by Cities.name
    var users by User via UsersToCities
    var country by Country referencedOn Cities.country
  }

  class Country(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Country>(Countries)

    var name by Countries.name
  }

  val allTables = arrayOf(Users, Cities, UsersToCities, Countries)
}

class EntityHookTest : DatabaseTestBase() {

  @Test
  fun testCreated01() {
    withTables(*EntityHookData.allTables) {
      val entities: Pair<Unit, Collection<EntityChange<*>>> = transactionWithEntityHook {
        val ru = Country.new {
          name = "RU"
        }
        val x = City.new {
          name = "St. Petersburg"
          country = ru
        }
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg").hasSize(1)

      val countryName = entities.second.map { it.toEntity(Country)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("RU").hasSize(1)
    }
  }

  @Test
  fun testDelete01() {
    withTables(*EntityHookData.allTables) {
      val spbId = transaction {
        val ru = Country.new {
          name = "RU"
        }
        val x = City.new {
          name = "St. Petersburg"
          country = ru
        }
        flushCache()
        x.id
      }

      val entities = transactionWithEntityHook {
        val spb = City.findById(spbId)!!
        spb.delete()
      }

      assertThat(entities.second.count()).isEqualTo(1)
      assertThat(entities.second.single().changeType).isEqualTo(Removed)
      assertThat(entities.second.single().id).isEqualTo(spbId)
    }
  }

  @Test
  fun testModifiedSimple01() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = Country.new {
          name = "RU"
        }
        val x = City.new {
          name = "St. Petersburg"
          country = ru
        }
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val de = Country.new {
          name = "DE"
        }
        val x = City.all().single()
        x.name = "Munich"
        x.country = de
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("Munich").hasSize(1)

      val countryName = entities.second.map { it.toEntity(Country)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("DE").hasSize(1)
    }
  }

  @Test fun testModifiedInnerTable01() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = Country.new {
          name = "RU"
        }
        val de = Country.new {
          name = "DE"
        }
        City.new {
          name = "St. Petersburg"
          country = ru
        }
        City.new {
          name = "Munich"
          country = de
        }
        User.new {
          name = "John"
          age = 30
        }
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val spb = City.find({ Cities.name eq "St. Petersburg" }).single()
        val john = User.all().single()
        john.cities = SizedCollection(listOf(spb))
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg").hasSize(1)

      val countryName = entities.second.map { it.toEntity(User)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("John").hasSize(1)
    }
  }

  @Test fun testModifiedInnerTable02() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = Country.new {
          name = "RU"
        }
        val de = Country.new {
          name = "DE"
        }
        val spb = City.new {
          name = "St. Petersburg"
          country = ru
        }
        val muc = City.new {
          name = "Munich"
          country = de
        }
        val john = User.new {
          name = "John"
          age = 30
        }
        john.cities = SizedCollection(listOf(muc))
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val spb = City.find({ Cities.name eq "St. Petersburg" }).single()
        val john = User.all().single()
        john.cities = SizedCollection(listOf(spb))
      }

      assertThat(entities.second.count()).isEqualTo(3)

      val cityName: List<String> = entities.second.map { it.toEntity(City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg", "Munich").hasSize(2)

      val countryName = entities.second.map { it.toEntity(User)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("John").hasSize(1)
    }
  }

  @Test fun testModifiedInnerTable03() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = Country.new {
          name = "RU"
        }
        val de = Country.new {
          name = "DE"
        }
        val spb = City.new {
          name = "St. Petersburg"
          country = ru
        }
        val muc = City.new {
          name = "Munich"
          country = de
        }
        val john = User.new {
          name = "John"
          age = 30
        }
        john.cities = SizedCollection(listOf(spb))
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val john = User.all().single()
        john.cities = SizedCollection(emptyList())
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg").hasSize(1)

      val countryName = entities.second.map { it.toEntity(User)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("John").hasSize(1)
    }
  }
}