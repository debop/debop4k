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
    withCitiesAndUsers(exclude = listOf(TestDB.POSTGRESQL)) { cities, users, userData ->
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
    withCitiesAndUsers(exclude = listOf(TestDB.POSTGRESQL)) { cities, users, userData ->
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

    }
  }

  @Test fun testSubstring01() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testInsertSelect01() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testInsertSelect02() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testSelectCase01() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  private fun DMLData.Misc.checkRow(row: ResultRow,
                                    n: Int, nn: Int?,
                                    d: DateTime, dn: DateTime?,
                                    t: DateTime, tn: DateTime?,
                                    e: DMLData.E, en: DMLData.E?,
                                    s: String, sn: String?,
                                    dc: BigDecimal, dcn: BigDecimal?) {
    assertThat(row[this.n]).isEqualTo(n)
    assertThat(row[this.nn]).isEqualTo(nn)
    assertThat(row[this.d]).isEqualTo(d)
    assertThat(row[this.dn]).isEqualTo(dn)
    assertThat(row[this.t]).isEqualTo(t)
    assertThat(row[this.tn]).isEqualTo(tn)
    assertThat(row[this.e]).isEqualTo(e)
    assertThat(row[this.en]).isEqualTo(en)
    assertThat(row[this.s]).isEqualTo(s)
    assertThat(row[this.sn]).isEqualTo(sn)
    assertThat(row[this.dc]).isEqualTo(dc)
    assertThat(row[this.dcn]).isEqualTo(dcn)
  }

  @Test fun testInsert01() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testInsert02() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testInsert03() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testInsert04() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testGeneratedKey01() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testGeneratedKey02() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testGeneratedKey03() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testSelectDistinct() {
    withCitiesAndUsers { cities, users, userData ->

    }
  }

  @Test fun testSelect01() {

  }

  @Test fun testSelect02() {

  }

  @Test fun testUpdate02() {

  }

  @Test fun testUpdate03() {

  }

  @Test fun testJoinWithAlias01() {

  }

  @Test fun testStringFunctions() {

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