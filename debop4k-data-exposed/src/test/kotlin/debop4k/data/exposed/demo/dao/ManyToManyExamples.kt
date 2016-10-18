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

class ManyToManyExamples : AbstractExposedTest() {

  // Schema

  object AppUsers : IntIdTable("app_user") {
    val userName = varchar("user_name", 50).index()
  }

  object Teams : IntIdTable("team") {
    val teamName = varchar("team_name", 100)
  }

  object TeamUsers : Table("team_users") {
    val team = reference("team_id", Teams).primaryKey(0)
    val user = reference("user_id", AppUsers).primaryKey(1)
  }

  // Entities

  class AppUser(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, AppUser>(AppUsers)

    var userName by AppUsers.userName
    val teams by Team via TeamUsers
  }

  class Team(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Team>(Teams)

    var teamName by Teams.teamName
    var members by AppUser via TeamUsers
  }

  @Test
  fun testManyToMany() {

    val dataSource = DataSources.ofEmbeddedH2()
    val database = Database.connect(dataSource)

    transaction {
      logger.addLogger(StdOutSqlLogger())

      SchemaUtils.create(Teams, AppUsers, TeamUsers)

      val team = Team.new {
        teamName = "DataLab"
      }
      Team.new {
        teamName = "TFT"
      }

      val users = listOf(AppUser.new { userName = "debop" },
                         AppUser.new { userName = "sunghyouk bae" })
      team.members = SizedCollection(users)

      Team.all().forEach { team ->
        println("Team: ${team.teamName}")
        team.members.forEach { user ->
          println("\tUser: ${user.userName}")
        }
      }

      println("Team Users relationship:")
      TeamUsers.selectAll().forEach {
        println("\tTeamId = ${it[TeamUsers.team]}, UserId = ${it[TeamUsers.user]}")
      }

      AppUser.all().forEach { user ->
        println("App User: ${user.userName}")
        user.teams.forEach { team ->
          println("\tAssigned team: ${team.teamName}")
        }
      }

      SchemaUtils.drop(TeamUsers, Teams, AppUsers)
    }
  }
}