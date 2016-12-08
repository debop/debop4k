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

import debop4k.core.kodatimes.asUtc
import debop4k.core.kodatimes.trimToMillis
import debop4k.data.exposed.dao.LongIdTable
import debop4k.data.exposed.examples.DatabaseTestBase
import debop4k.data.exposed.examples.TestDB
import debop4k.data.exposed.examples.TestDB.POSTGRESQL
import debop4k.data.exposed.examples.shared.DMLData.Cities
import debop4k.data.exposed.examples.shared.DMLData.E
import debop4k.data.exposed.examples.shared.DMLData.Misc
import debop4k.data.exposed.examples.shared.DMLData.UserData
import debop4k.data.exposed.examples.shared.DMLData.Users
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.joda.time.DateTime
import org.junit.Test
import java.math.BigDecimal

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

//    val es = enumerationByName("es", 5, E::class.java)
//    val esn = enumerationByName("esn", 5, E::class.java).nullable()

    val s = varchar("s", 100)
    val sn = varchar("sn", 100).nullable()

    val dc = decimal("dc", 12, 2)
    val dcn = decimal("dcn", 12, 2).nullable()
  }
}

class DMLTests : DatabaseTestBase() {

  fun withCitiesAndUsers(exclude: List<TestDB> = emptyList(),
                         statement: Transaction.(cities: Cities, users: Users, userData: UserData) -> Unit) {

//    val Users = Users
//    val Cities = Cities
//    val UserData = UserData

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

      UserData.insert {
        it[user_id] = "smth"
        it[comment] = "Something is here"
        it[value] = 10
      }

      UserData.insert {
        it[user_id] = "eugene"
        it[comment] = "Comment for Eugene"
        it[value] = 20
      }

      UserData.insert {
        it[user_id] = "sergey"
        it[comment] = "Comment for Sergey"
        it[value] = 30
      }
      statement(Cities, Users, UserData)
    }
  }

  @Test fun testUpdate01() {
    withCitiesAndUsers { cities, users, userData ->
      val alexId = "alex"
      val alexName = users.slice(users.name).select { users.id.eq(alexId) }.first()[users.name]
      assertThat(alexName).isEqualTo("Alex")

      val newName = "Alexey"
      users.update({ users.id.eq(alexId) }) {
        it[users.name] = newName
      }
      val alexNewName = users.slice(users.name).select { users.id.eq(alexId) }.first()[users.name]
      assertThat(alexNewName).isEqualTo("Alexey")
    }
  }

  @Test fun testPreparedStatement() {
    withCitiesAndUsers { cities, users, userData ->
      val name = users.select { users.id eq "eugene" }.first()[users.name]
      assertThat(name).isEqualTo("Eugene")
    }
  }

  @Test fun testDelete01() {
    withCitiesAndUsers { cities, users, userData ->
      userData.deleteAll()
      val userDataExists = userData.selectAll().any()
      assertThat(userDataExists).isFalse()

      val smthId = users.slice(users.id).select { users.name like "%thing" }.single()[users.id]
      assertThat(smthId).isEqualTo("smth")

      users.deleteWhere { users.name like "%thing" }
      val hasSmth = users.slice(users.id).select { users.name like "%thing" }.any()
      assertThat(hasSmth).isFalse()
    }
  }

  // manual join
  @Test fun testJoin01() {
    withCitiesAndUsers { cities, users, userData ->
      (users innerJoin cities).slice(users.name, cities.name)
          .select { (users.id.eq("andrey") or users.name.eq("Sergey")) and users.cityId.eq(cities.id) }
          .forEach {
            val userName = it[users.name]
            val cityName = it[cities.name]
            when (userName) {
              "Andrey" -> assertThat(cityName).isEqualTo("St. Petersburg")
              "Sergey" -> assertThat(cityName).isEqualTo("Munich")
              else     -> error("Unexpected user $userName")
            }
          }
    }
  }

  // join with foreign key
  @Test fun testJoin02() {
    withCitiesAndUsers { cities, users, userData ->
      val stPetersburgUser = (users innerJoin cities).slice(users.name, users.cityId, cities.name)
          .select { cities.name.eq("St. Petersburg") or users.cityId.isNull() }
          .single()

      assertThat(stPetersburgUser[users.name]).isEqualTo("Andrey")
      assertThat(stPetersburgUser[cities.name]).isEqualTo("St. Petersburg")
    }
  }

  // triple join
  @Test fun testJoin03() {
    withCitiesAndUsers { cities, users, userData ->
      val r = (cities innerJoin users innerJoin userData).selectAll().orderBy(users.id).toList()

      assertThat(r).hasSize(2)

      assertThat(r[0][users.name]).isEqualTo("Eugene")
      assertThat(r[0][userData.comment]).isEqualTo("Comment for Eugene")
      assertThat(r[0][cities.name]).isEqualTo("Munich")

      assertThat(r[1][users.name]).isEqualTo("Sergey")
      assertThat(r[1][userData.comment]).isEqualTo("Comment for Sergey")
      assertThat(r[1][cities.name]).isEqualTo("Munich")
    }
  }

  @Test fun testJoin04() {
    val Numbers = object : Table() {
      val id = integer("id").primaryKey()
    }
    val Names = object : Table() {
      val name = varchar("name", 10).primaryKey()
    }
    val Map = object : Table() {
      val id_ref = integer("id_ref") references Numbers.id
      val name_ref = varchar("name_ref", 10) references Names.name
    }

    withTables(Numbers, Names, Map) {
      Numbers.insert { it[id] = 1 }
      Numbers.insert { it[id] = 2 }
      Names.insert { it[name] = "Foo" }
      Names.insert { it[name] = "Bar" }
      Map.insert {
        it[id_ref] = 2
        it[name_ref] = "Foo"
      }

      val r = (Numbers innerJoin Map innerJoin Names).selectAll().toList()
      assertThat(r).hasSize(1)
      assertThat(r[0][Numbers.id]).isEqualTo(2)
      assertThat(r[0][Names.name]).isEqualTo("Foo")
    }
  }

  @Test fun testGroupBy01() {
    withCitiesAndUsers { cities, users, userData ->
      val query: Query = (cities innerJoin users)
          .slice(cities.name, users.id.count())
          .selectAll()
          .groupBy(cities.name)

      query.forEach {
        val cityName = it[cities.name]
        val userCount = it[users.id.count()]

        when (cityName) {
          "Munich"         -> assertThat(userCount).isEqualTo(2)
          "Prague"         -> assertThat(userCount).isEqualTo(0)
          "St. Petersburg" -> assertThat(userCount).isEqualTo(1)
          else             -> error("Unknown city $cityName")
        }
      }

    }
  }

  @Test fun testGroupBy02() {
    withCitiesAndUsers { cities, users, userData ->
      val c = users.id.count()
      val r = (cities innerJoin users)
          .slice(cities.name, c)
          .selectAll()
          .groupBy(cities.name)
          .having { c eq 1 }
          .toList()

      assertThat(r).hasSize(1)
      assertThat(r[0][cities.name]).isEqualTo("St. Petersburg")
      assertThat(r[0][c]).isEqualTo(1)
    }
  }

  @Test fun testGroupBy03() {
    withCitiesAndUsers { cities, users, userData ->
      val c = users.id.count()
      val r = (cities innerJoin users)
          .slice(cities.name, c, cities.id.max())
          .selectAll()
          .groupBy(cities.name)
          .having { c.eq(cities.id.max()) }
          .orderBy(cities.name)
          .toList()

      assertThat(r).hasSize(2)

      0.let {
        assertThat(r[it][cities.name]).isEqualTo("Munich")
        assertThat(r[it][c]).isEqualTo(2)
        assertThat(r[it][cities.id.max()]).isEqualTo(2)
      }
      1.let {
        assertThat(r[it][cities.name]).isEqualTo("St. Petersburg")
        assertThat(r[it][c]).isEqualTo(1)
        assertThat(r[it][cities.id.max()]).isEqualTo(1)
      }
    }
  }

  @Test fun testGroupBy04() {
    withCitiesAndUsers { cities, users, userData ->
      val c = users.id.count()
      val r = (cities innerJoin users)
          .slice(cities.name, c, cities.id.max()).selectAll()
          .groupBy(cities.name)
          .having { c.lessEq(42) }
          .orderBy(cities.name)
          .toList()

      assertThat(r).hasSize(2)

      0.let {
        assertThat(r[it][cities.name]).isEqualTo("Munich")
        assertThat(r[it][c]).isEqualTo(2)
      }
      1.let {
        assertThat(r[it][cities.name]).isEqualTo("St. Petersburg")
        assertThat(r[it][c]).isEqualTo(1)
      }
    }
  }

  @Test fun testGroupBy05() {
    withCitiesAndUsers { cities, users, userData ->

      val maxNullableCityId: Max<Int?> = users.cityId.max()

      users.slice(maxNullableCityId).selectAll()
          .map { it[maxNullableCityId] }
          .let { result: List<Int?> ->
            assertThat(result).hasSize(1)
            assertThat(result.single()).isNotNull()
          }

      users.slice(maxNullableCityId)
          .select { users.cityId.isNull() }
          .map { it[maxNullableCityId] }
          .let { result: List<Int?> ->
            assertThat(result).hasSize(1)
            assertThat(result.single()).isNull()
          }
    }
  }

  @Test fun testGroupBy06() {
    withCitiesAndUsers { cities, users, userData ->
      val maxNullableId: Max<Int> = cities.id.max()

      cities.slice(maxNullableId).selectAll()
          .map { it[maxNullableId] }
          .let { result: List<Int?> ->
            assertThat(result).hasSize(1)
            assertThat(result.single()).isNotNull()
          }

      cities.slice(maxNullableId)
          .select { cities.id.isNull() }
          .map { it[maxNullableId] }
          .let { result: List<Int?> ->
            assertThat(result).hasSize(1)
            assertThat(result.single()).isNull()
          }
    }
  }

  @Test fun testGroupBy07() {
    withCitiesAndUsers { cities, users, userData ->
      val avgIdExpr = cities.id.avg()
      val avgId = BigDecimal.valueOf(cities.selectAll().map { it[cities.id] }.average())

      cities.slice(avgIdExpr).selectAll()
          .map { it[avgIdExpr] }
          .let { result ->
            assertThat(result).hasSize(1)
            assertThat(result.single()!!.compareTo(avgId) == 0).isTrue()
          }

      cities.slice(avgIdExpr).select { cities.id.isNull() }
          .map { it[avgIdExpr] }
          .let { result ->
            assertThat(result).hasSize(1)
            assertThat(result.single()).isNull()
          }
    }
  }

  @Test fun testOrderBy01() {
    val expected = listOf("alex", "andrey", "eugene", "sergey", "smth")

    withCitiesAndUsers { cities, users, userData ->
      val r = users.slice(users.id).selectAll()
          .orderBy(users.id)
          .map { it[users.id] }
          .toList()

      assertThat(r).hasSize(5)
      assertThat(r).isEqualTo(expected)
    }
  }

  @Test fun testOrderBy02() {
    val expected = listOf("eugene", "sergey", "andrey", "alex", "smth")
    withCitiesAndUsers(exclude = listOf(POSTGRESQL)) { cities, users, userData ->
      val r = users.slice(users.id).selectAll()
          .orderBy(users.cityId, false)
          .orderBy(users.id)
          .map { it[users.id] }
          .toList()

      assertThat(r).hasSize(5)
      assertThat(r).isEqualTo(expected)
    }
  }

  @Test fun testOrderBy03() {
    val expected = listOf("eugene", "sergey", "andrey", "alex", "smth")
    withCitiesAndUsers(exclude = listOf(POSTGRESQL)) { cities, users, userData ->
      val r = users.slice(users.id).selectAll()
          .orderBy(users.cityId to false)
          .orderBy(users.id to true)
          .map { it[users.id] }
          .toList()
      assertThat(r).hasSize(5)
      assertThat(r).isEqualTo(expected)
    }
  }

  @Test fun testOrderBy04() {
    withCitiesAndUsers { cities, users, userData ->
      val r = (cities innerJoin users).slice(cities.name, users.id.count()).selectAll()
          .groupBy(cities.name)
          .orderBy(cities.name)
          .toList()

      assertThat(r).hasSize(2)
      assertThat(r[0][cities.name]).isEqualTo("Munich")
      assertThat(r[0][users.id.count()]).isEqualTo(2)
      assertThat(r[1][cities.name]).isEqualTo("St. Petersburg")
      assertThat(r[1][users.id.count()]).isEqualTo(1)
    }
  }

  @Test fun testSizeIterable() {
    withCitiesAndUsers { cities, users, userData ->
      assertThat(cities.selectAll().empty()).isFalse()
      assertThat(cities.select { cities.name eq "Qwertt" }.empty()).isTrue()

      assertThat(cities.selectAll().count()).isEqualTo(3)
      assertThat(cities.select { cities.name eq "Qwertt" }.count()).isEqualTo(0)
    }
  }

  @Test fun testExists01() {
    withCitiesAndUsers { cities, users, userData ->
      val r = users
          .select {
            exists(userData.select((userData.user_id eq users.id) and (userData.comment like "%here%")))
          }
          .toList()

      assertThat(r).hasSize(1)
      assertThat(r[0][users.name]).isEqualTo("Something")
    }
  }

  @Test fun testExists02() {
    withCitiesAndUsers { cities, users, userData ->

      var commentsWhere = userData.comment like "%here%"
      commentsWhere = commentsWhere.or(userData.comment like "%Sergey")

      val r = users
          .select {
            exists(userData.select((userData.user_id eq users.id) and commentsWhere))
          }
          .orderBy(users.id)
          .toList()

      assertThat(r).hasSize(2)
      assertThat(r.map { it[users.name] }).isEqualTo(listOf("Sergey", "Something"))
    }
  }

  @Test fun testExists03() {
    withCitiesAndUsers { cities, users, userData ->

      val r = users
          .select {
            exists(userData.select((userData.user_id eq users.id) and (userData.comment like "%here%"))) or
                exists(userData.select((userData.user_id eq users.id) and (userData.comment like "%Sergey")))
          }
          .orderBy(users.id)
          .toList()
    }
  }

  @Test fun testInList01() {
    withCitiesAndUsers { cities, users, userData ->
      val searchIds = listOf("andrey", "alex")
      val r = users.select { users.id inList searchIds }.orderBy(users.name).toList()
      assertThat(r).hasSize(2)
      assertThat(r.map { it[users.name] }).isEqualTo(listOf("Alex", "Andrey"))
    }
  }

  @Test fun testInList02() {
    withCitiesAndUsers { cities, users, userData ->
      val cityIds: List<Int> = cities.selectAll().map { it[cities.id] }.take(2)
      val r = cities.select { cities.id inList cityIds }

      assertThat(r.count()).isEqualTo(2)
    }
  }

  @Test fun testCalc01() {
    withCitiesAndUsers { cities, users, userData ->
      val r = cities.slice(cities.id.sum()).selectAll().toList()
      assertThat(r).hasSize(1)
      assertThat(r[0][cities.id.sum()]).isEqualTo(6)
    }
  }

  @Test fun testCalc02() {
    withCitiesAndUsers { cities, users, userData ->
      val sum: Expression<Int?> = Expression.build {
        Sum(cities.id + userData.value, IntegerColumnType())
      }
      val r = (users innerJoin userData innerJoin cities).slice(users.id, sum)
          .selectAll()
          .groupBy(users.id)
          .orderBy(users.id)
          .toList()

      assertThat(r).hasSize(2)
      assertThat(r[0][users.id]).isEqualTo("eugene")
      assertThat(r[0][sum]).isEqualTo(22)
      assertThat(r[1][users.id]).isEqualTo("sergey")
      assertThat(r[1][sum]).isEqualTo(32)
    }
  }

  @Test fun testCalc03() {
    withCitiesAndUsers { cities, users, userData ->
      val sum = Expression.build { Sum(cities.id * 100 + userData.value / 10, IntegerColumnType()) }
      val r = (users innerJoin userData innerJoin cities).slice(users.id, sum)
          .selectAll()
          .groupBy(users.id)
          .orderBy(users.id)
          .toList()

      assertThat(r).hasSize(2)
      assertThat(r[0][users.id]).isEqualTo("eugene")
      assertThat(r[0][sum]).isEqualTo(202)
      assertThat(r[1][users.id]).isEqualTo("sergey")
      assertThat(r[1][sum]).isEqualTo(203)
    }
  }

  @Test fun testSubstring01() {
    withCitiesAndUsers { cities, users, userData ->
      val substring = users.name.substring(1, 2)
      val r = users.slice(users.id, substring)
          .selectAll()
          .orderBy(users.id)
          .toList()

      assertThat(r).hasSize(5)
      assertThat(r[0][substring]).isEqualTo("Al")
      assertThat(r[1][substring]).isEqualTo("An")
      assertThat(r[2][substring]).isEqualTo("Eu")
      assertThat(r[3][substring]).isEqualTo("Se")
      assertThat(r[4][substring]).isEqualTo("So")
    }
  }

  @Test fun testInsertSelect01() {
    withCitiesAndUsers { cities, users, userData ->
      val substring = users.name.substring(1, 2).upperCase()
      cities.insert(users.slice(substring).selectAll().orderBy(users.id).limit(2))

      val r = cities.slice(cities.name).selectAll().orderBy(cities.id, false).limit(2).toList()
      assertThat(r).hasSize(2)
      assertThat(r[0][cities.name]).isEqualTo("AN")
      assertThat(r[1][cities.name]).isEqualTo("AL")
    }
  }

  @Test fun testInsertSelect02() {
    withCitiesAndUsers { cities, users, userData ->
      userData.insert(userData.slice(userData.user_id, userData.comment, intParam(42)).selectAll())

      val r = userData.select { userData.value eq 42 }.orderBy(userData.user_id).toList()
      assertThat(r).hasSize(3)
    }
  }

  @Test fun testSelectCase01() {
    withCitiesAndUsers { cities, users, userData ->
      val field = Expression.build { case().When(users.id eq "alex", stringLiteral("11")).Else(stringLiteral("22")) }
      val r = users.slice(users.id, field).selectAll().orderBy(users.id).limit(2).toList()

      assertThat(r).hasSize(2)
      assertThat(r[0][users.id]).isEqualTo("alex")
      assertThat(r[0][field]).isEqualTo("11")
      assertThat(r[1][users.id]).isEqualTo("andrey")
      assertThat(r[1][field]).isEqualTo("22")
    }
  }

  private fun Misc.checkRow(row: ResultRow,
                            n: Int, nn: Int?,
                            d: DateTime, dn: DateTime?,
                            t: DateTime, tn: DateTime?,
                            e: E, en: E?,
      //                            es:E, esn: E?,
                            s: String, sn: String?,
                            dc: BigDecimal, dcn: BigDecimal?) {
    assertThat(row[this.n]).isEqualTo(n)
    assertThat(row[this.nn]).isEqualTo(nn)
    assertThat(row[this.d]).isEqualTo(d.asUtc().withTimeAtStartOfDay())
    assertThat(row[this.dn]).isEqualTo(dn?.asUtc()?.withTimeAtStartOfDay())
    assertThat(row[this.t].trimToMillis()).isEqualTo(t.asUtc().trimToMillis())
    assertThat(row[this.tn]?.trimToMillis()).isEqualTo(tn?.asUtc()?.trimToMillis())
    assertThat(row[this.e]).isEqualTo(e)
    assertThat(row[this.en]).isEqualTo(en)
//    assertThat(row[this.es]).isEqualTo(es)
//    assertThat(row[this.esn]).isEqualTo(esn)
    assertThat(row[this.s]).isEqualTo(s)
    assertThat(row[this.sn]).isEqualTo(sn)
    assertThat(row[this.dc]).isEqualTo(dc)
    assertThat(row[this.dcn]).isEqualTo(dcn)
  }

  @Test fun testInsert01() {
    val tbl = DMLData.Misc
    val date = today
    val time = DateTime.now()

    withTables(tbl) {
      tbl.insert {
        it[n] = 42
        it[d] = date
        it[t] = time
        it[e] = DMLData.E.ONE
        it[s] = "test"
        it[dc] = BigDecimal("239.42")
      }

      val row = tbl.selectAll().single()
      tbl.checkRow(row, 42, null, date, null, time, null, DMLData.E.ONE, null, "test", null, BigDecimal("239.42"), null)
    }
  }

  @Test fun testInsert02() {
    val tbl = DMLData.Misc
    val date = today
    val time = DateTime.now()

    withTables(tbl) {
      tbl.insert {
        it[n] = 42
        it[nn] = null
        it[d] = date
        it[dn] = null
        it[t] = time
        it[tn] = null
        it[e] = DMLData.E.ONE
        it[en] = null
        it[s] = "test"
        it[sn] = null
        it[dc] = BigDecimal("239.42")
        it[dcn] = null
      }

      val row = tbl.selectAll().single()
      tbl.checkRow(row, 42, null, date, null, time, null, DMLData.E.ONE, null, "test", null, BigDecimal("239.42"), null)
    }
  }

  @Test fun testInsert03() {
    val tbl = DMLData.Misc
    val date = today
    val time = DateTime.now()

    withTables(tbl) {
      tbl.insert {
        it[n] = 42
        it[nn] = 42
        it[d] = date
        it[dn] = date
        it[t] = time
        it[tn] = time
        it[e] = DMLData.E.ONE
        it[en] = DMLData.E.ONE
        it[s] = "test"
        it[sn] = "test"
        it[dc] = BigDecimal("239.42")
        it[dcn] = BigDecimal("239.42")
      }

      val row = tbl.selectAll().single()
      tbl.checkRow(row, 42, 42,
                   date, date,
                   time, time,
                   DMLData.E.ONE, DMLData.E.ONE,
                   "test", "test",
                   BigDecimal("239.42"), BigDecimal("239.42"))
    }
  }

  @Test fun testInsert04() {
    val stringThatNeedsEscaping = "A'braham Barakhyaru"
    val tbl = DMLData.Misc
    val date = today
    val time = DateTime.now()

    withTables(tbl) {
      tbl.insert {
        it[n] = 42
        it[d] = date
        it[t] = time
        it[e] = DMLData.E.ONE
        it[s] = stringThatNeedsEscaping
        it[dc] = BigDecimal("239.42")
      }
      val row = tbl.selectAll().single()
      tbl.checkRow(row, 42, null, date, null, time, null, DMLData.E.ONE, null, stringThatNeedsEscaping, null, BigDecimal("239.42"), null)
    }
  }

  @Test fun testGeneratedKey01() {
    withTables(DMLData.Cities) {
      val id = DMLData.Cities.insert {
        it[DMLData.Cities.name] = "FooCity"
      } get DMLData.Cities.id

      val row = DMLData.Cities.selectAll().last()
      assertThat(row[DMLData.Cities.id]).isEqualTo(id)
    }
  }

  @Test fun testGeneratedKey02() {
    val LongIdTable = object : Table() {
      val id = long("id").autoIncrement().primaryKey()
      val name = text("name")
    }
    withTables(LongIdTable) {
      val id = LongIdTable.insert {
        it[LongIdTable.name] = "Foo"
      } get LongIdTable.id
      val row = LongIdTable.selectAll().last()
      assertThat(row[LongIdTable.id]).isEqualTo(id)
    }
  }

  @Test fun testGeneratedKey03() {
    val IntIdTable = object : IntIdTable() {
      val name = text("name")
    }
    withTables(IntIdTable) {
      val id = IntIdTable.insertAndGetId {
        it[IntIdTable.name] = "Foo"
      }
      assertThat(IntIdTable.selectAll().last()[IntIdTable.id]).isEqualTo(id)
    }
  }

  @Test fun testGeneratedKey04() {
    val LongIdTable = object : LongIdTable() {
      val name = text("name")
    }
    withTables(LongIdTable) {
      val id = LongIdTable.insertAndGetId {
        it[LongIdTable.name] = "Foo"
      }
      assertThat(LongIdTable.selectAll().last()[LongIdTable.id]).isEqualTo(id)
    }
  }

  @Test fun testSelectDistinct() {
    val cities = DMLData.Cities
    withTables(cities) {
      cities.insert { it[cities.name] = "test" }
      cities.insert { it[cities.name] = "test" }

      assertThat(cities.selectAll().count()).isEqualTo(2)
      assertThat(cities.selectAll().withDistinct().count()).isEqualTo(2)
      assertThat(cities.slice(cities.name).selectAll().withDistinct().count()).isEqualTo(1)
      assertThat(cities.slice(cities.name).selectAll().withDistinct().single()[cities.name]).isEqualTo("test")
    }
  }

  @Test fun testSelect01() {
    val tbl = DMLData.Misc

    withTables(tbl) {
      val date = today
      val time = DateTime.now()
      val sTest = "test"
      val dec = BigDecimal("239.42")
      tbl.insert {
        it[n] = 42
        it[d] = date
        it[t] = time
        it[e] = DMLData.E.ONE
//        it[es] = DMLData.E.ONE
        it[s] = sTest
        it[dc] = dec
      }

      tbl.checkRow(tbl.select { tbl.n.eq(42) }.single(), 42, null, date, null, time, null, DMLData.E.ONE, null, sTest, null, dec, null)
    }
  }

  @Test fun testSelect02() {

  }

  @Test fun testUpdate02() {

  }

  @Test fun testUpdate03() {

  }

  @Test fun testJoinWithAlias01() {
    /*
      SELECT Users.id, Users.name, Users.city_id, u2.id, u2.name, u2.city_id
      FROM Users LEFT JOIN Users AS u2 ON u2.id = 'smth'
      WHERE Users.id = 'alex'
     */
    withCitiesAndUsers { cities, users, userData ->
      val usersAlias = users.alias("u2")
      val resultRow = Join(users).join(usersAlias, JoinType.LEFT, usersAlias[users.id], stringLiteral("smth"))
          .select { users.id eq "alex" }
          .single()

      assertThat(resultRow[users.name]).isEqualTo("Alex")
      assertThat(resultRow[usersAlias[users.name]]).isEqualTo("Something")
    }
  }

  @Test fun testStringFunctions() {
    withCitiesAndUsers { cities, users, userData ->
      val lcase = DMLData.Cities.name.lowerCase()
      assertThat(cities.slice(lcase).selectAll().any { it[lcase] == "prague" }).isTrue()

      val ucase = DMLData.Cities.name.upperCase()
      assertThat(cities.slice(ucase).selectAll().any { it[ucase] == "PRAGUE" }).isTrue()
    }
  }

  @Test fun testJoinSubQuery01() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testJoinSubQuery02() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testDefaultExpressions01() {

  }
}

//interface Foo<out T> {}
//
//open class F<out T> : Foo<T> {}
//
//interface Foo2<out T> : Foo<T> {}
//
//fun <T> Foo2<T>.test(): String {
//  return "test"
//}
//
//class FooImpl<T> : F<T>(), Foo2<T> {}
//
//class FooTests {
//  @Test fun test01() {
//    val foo = FooImpl<Int>()
//    assertThat(foo.test()).isEqualTo("test")
//  }
//}

val today: DateTime = DateTime.now().withTimeAtStartOfDay()