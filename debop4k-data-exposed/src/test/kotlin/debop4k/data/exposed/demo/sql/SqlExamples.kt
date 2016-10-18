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
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import java.sql.Connection

class SqlExamples : AbstractExposedTest() {

  object Courses : Table("course") {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 50)
  }

  object Students : Table("student") {
    val id = integer("student_id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val grade = integer("grade").default(1)
    val course = integer("course_id") references Courses.id
  }

  @Test
  fun testSimpleSql() {
    val dataSource = DataSources.ofEmbeddedH2()
    val database = Database.connect(dataSource)

    transaction(Connection.TRANSACTION_READ_COMMITTED, 3) {
      logger.addLogger(StdOutSqlLogger())

      SchemaUtils.create(Courses, Students)

      val mathId = Courses.insert {
        it[name] = "수학"
      }[Courses.id]

      val historyId = Courses.insert {
        it[name] = "역사"
      }[Courses.id]

      // Insert
      Students.insert {
        it[name] = "debop"
        it[grade] = 49
        it[course] = historyId
      }

      Students.insert {
        it[name] = "sunghyouk bae"
        it[grade] = 49
        it[course] = mathId
      }


      // Update
      Students.update({ Students.name.eq("debop") }) {
        it[grade] = 2
      }

      // Delete
      Students.deleteWhere { Students.grade.lessEq(2) and Students.name.like("de%") }

      // Read
      Students.select { Students.grade.greater(2) }

      Students.selectAll().map {
        "id=${it[Students.id]}, name=${it[Students.name]}, grade=${it[Students.grade]}"
      }.forEach {
        println(it)
      }

      SchemaUtils.drop(Students, Courses)
    }
  }
}