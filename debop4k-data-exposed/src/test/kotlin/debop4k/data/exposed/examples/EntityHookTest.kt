package debop4k.data.exposed.examples

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
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
    val user = reference("user", Users, ReferenceOption.CASCADE)
    val city = reference("city", Cities, ReferenceOption.CASCADE)
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
        val ru = EntityHookData.Country.new {
          name = "RU"
        }
        val x = EntityHookData.City.new {
          name = "St. Petersburg"
          country = ru
        }
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(EntityHookData.City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg").hasSize(1)

      val countryName = entities.second.map { it.toEntity(EntityHookData.Country)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("RU").hasSize(1)
    }
  }

  @Test
  fun testDelete01() {
    withTables(*EntityHookData.allTables) {
      val spbId = transaction {
        val ru = EntityHookData.Country.new {
          name = "RU"
        }
        val x = EntityHookData.City.new {
          name = "St. Petersburg"
          country = ru
        }
        flushCache()
        x.id
      }

      val entities = transactionWithEntityHook {
        val spb = EntityHookData.City.findById(spbId)!!
        spb.delete()
      }

      assertThat(entities.second.count()).isEqualTo(1)
      assertThat(entities.second.single().changeType).isEqualTo(EntityChangeType.Removed)
      assertThat(entities.second.single().id).isEqualTo(spbId)
    }
  }

  @Test
  fun testModifiedSimple01() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = EntityHookData.Country.new {
          name = "RU"
        }
        val x = EntityHookData.City.new {
          name = "St. Petersburg"
          country = ru
        }
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val de = EntityHookData.Country.new {
          name = "DE"
        }
        val x = EntityHookData.City.all().single()
        x.name = "Munich"
        x.country = de
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(EntityHookData.City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("Munich").hasSize(1)

      val countryName = entities.second.map { it.toEntity(EntityHookData.Country)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("DE").hasSize(1)
    }
  }

  @Test fun testModifiedInnerTable01() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = EntityHookData.Country.new {
          name = "RU"
        }
        val de = EntityHookData.Country.new {
          name = "DE"
        }
        EntityHookData.City.new {
          name = "St. Petersburg"
          country = ru
        }
        EntityHookData.City.new {
          name = "Munich"
          country = de
        }
        EntityHookData.User.new {
          name = "John"
          age = 30
        }
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val spb = EntityHookData.City.find({ EntityHookData.Cities.name eq "St. Petersburg" }).single()
        val john = EntityHookData.User.all().single()
        john.cities = SizedCollection(listOf(spb))
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(EntityHookData.City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg").hasSize(1)

      val countryName = entities.second.map { it.toEntity(EntityHookData.User)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("John").hasSize(1)
    }
  }

  @Test fun testModifiedInnerTable02() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = EntityHookData.Country.new {
          name = "RU"
        }
        val de = EntityHookData.Country.new {
          name = "DE"
        }
        val spb = EntityHookData.City.new {
          name = "St. Petersburg"
          country = ru
        }
        val muc = EntityHookData.City.new {
          name = "Munich"
          country = de
        }
        val john = EntityHookData.User.new {
          name = "John"
          age = 30
        }
        john.cities = SizedCollection(listOf(muc))
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val spb = EntityHookData.City.find({ EntityHookData.Cities.name eq "St. Petersburg" }).single()
        val john = EntityHookData.User.all().single()
        john.cities = SizedCollection(listOf(spb))
      }

      assertThat(entities.second.count()).isEqualTo(3)

      val cityName: List<String> = entities.second.map { it.toEntity(EntityHookData.City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg", "Munich").hasSize(2)

      val countryName = entities.second.map { it.toEntity(EntityHookData.User)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("John").hasSize(1)
    }
  }

  @Test fun testModifiedInnerTable03() {
    withTables(*EntityHookData.allTables) {
      transaction {
        val ru = EntityHookData.Country.new {
          name = "RU"
        }
        val de = EntityHookData.Country.new {
          name = "DE"
        }
        val spb = EntityHookData.City.new {
          name = "St. Petersburg"
          country = ru
        }
        val muc = EntityHookData.City.new {
          name = "Munich"
          country = de
        }
        val john = EntityHookData.User.new {
          name = "John"
          age = 30
        }
        john.cities = SizedCollection(listOf(spb))
        flushCache()
      }

      val entities = transactionWithEntityHook {
        val john = EntityHookData.User.all().single()
        john.cities = SizedCollection(emptyList())
      }

      assertThat(entities.second.count()).isEqualTo(2)

      val cityName: List<String> = entities.second.map { it.toEntity(EntityHookData.City)?.name }.filterNotNull()
      assertThat(cityName).containsOnly("St. Petersburg").hasSize(1)

      val countryName = entities.second.map { it.toEntity(EntityHookData.User)?.name }.filterNotNull()
      assertThat(countryName).containsOnly("John").hasSize(1)
    }
  }
}