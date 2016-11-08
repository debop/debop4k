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

import debop4k.data.exposed.dao.*
import debop4k.data.exposed.emptySizedCollection
import debop4k.data.exposed.examples.DatabaseTestBase
import debop4k.data.exposed.examples.shared.ViaData.ConnectionTable
import debop4k.data.exposed.examples.shared.ViaData.NumbersTable
import debop4k.data.exposed.examples.shared.ViaData.StringsTable
import debop4k.data.exposed.examples.shared.ViaData.allTables
import debop4k.data.exposed.sizedCollectionOf
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.junit.Test
import java.util.*


//open class UUIDTable(name: String = "") : IdTable<UUID>(name) {
//  override val id: Column<EntityID<UUID>> =
//      uuid("id")
//          .clientDefault { Generators.timeBasedGenerator().generate() }
//          .primaryKey()
//          .entityId()
//}

object ViaData {
  object NumbersTable : UUIDIdTable() {
    val number = integer("number")
  }

  object StringsTable : LongIdTable() {
    val text = varchar("text", 10)
  }

  object ConnectionTable : Table() {
    val numId = reference("numId", NumbersTable, CASCADE)
    val stringId = reference("stringId", StringsTable, CASCADE)

    init {
      index(true, numId, stringId)
    }
  }

  val allTables = arrayOf(NumbersTable, StringsTable, ConnectionTable)
}

class VNumber(id: EntityID<UUID>) : UUIDEntity(id) {
  var number by NumbersTable.number
  var connectedStrings: SizedIterable<VString> by VString via ConnectionTable

  companion object : UUIDEntityClass<VNumber>(NumbersTable)
}

class VString(id: EntityID<Long>) : LongEntity(id) {
  var text by StringsTable.text

  companion object : LongEntityClass<VString>(StringsTable)
}

class ViaTest : DatabaseTestBase() {

  @Test
  fun testConnection01() {
    withTables(*allTables) {
      val n = VNumber.new { number = 10 }
      val s = VString.new { text = "aaa" }
      n.connectedStrings = SizedCollection(listOf(s))

      val row = ConnectionTable.selectAll().single()

      assertThat(row[ConnectionTable.numId]).isEqualTo(n.id)
      assertThat(row[ConnectionTable.stringId]).isEqualTo(s.id)
    }
  }

  @Test
  fun testConnection02() {
    withTables(*allTables) {
      val n1 = VNumber.new { number = 1 }
      val n2 = VNumber.new { number = 2 }
      val s1 = VString.new { text = "aaa" }
      val s2 = VString.new { text = "bbb" }
      val s3 = VString.new { text = "ccc" }

      n1.connectedStrings = sizedCollectionOf(s1, s2)

      val row = ConnectionTable.selectAll().toList()
      assertThat(row).hasSize(2)
      assertThat(row[0][ConnectionTable.numId]).isEqualTo(n1.id)
      assertThat(row[1][ConnectionTable.numId]).isEqualTo(n1.id)
      assertThat(row.map { it[ConnectionTable.stringId] }).isEqualTo(listOf(s1.id, s2.id))
    }
  }


  @Test
  fun testConnection03() {
    withTables(*allTables) {
      val n1 = VNumber.new { number = 1 }
      val n2 = VNumber.new { number = 2 }
      val s1 = VString.new { text = "aaa" }
      val s2 = VString.new { text = "bbb" }
      val s3 = VString.new { text = "ccc" }

      n1.connectedStrings = sizedCollectionOf(s1, s2)
      n2.connectedStrings = sizedCollectionOf(s1, s3)

      run {
        val row = ConnectionTable.selectAll().toList()
        assertThat(row).hasSize(4)
        assertThat(n1.connectedStrings).contains(s1, s2)
        assertThat(n2.connectedStrings).contains(s1, s3)
      }

      n1.connectedStrings = emptySizedCollection()

      run {
        val row = ConnectionTable.selectAll().toList()
        assertThat(row).hasSize(2)
        assertThat(n1.connectedStrings).isEmpty()

        assertThat(row[0][ConnectionTable.numId]).isEqualTo(n2.id)
        assertThat(row[1][ConnectionTable.numId]).isEqualTo(n2.id)
        assertThat(n2.connectedStrings).contains(s1, s3)
      }
    }
  }

  @Test
  fun testConnection04() {
    withTables(*allTables) {
      val n1 = VNumber.new { number = 1 }
      val n2 = VNumber.new { number = 2 }
      val s1 = VString.new { text = "aaa" }
      val s2 = VString.new { text = "bbb" }
      val s3 = VString.new { text = "ccc" }

      n1.connectedStrings = sizedCollectionOf(s1, s2)
      n2.connectedStrings = sizedCollectionOf(s1, s3)

      run {
        val row = ConnectionTable.selectAll().toList()
        assertThat(row).hasSize(4)
        assertThat(n1.connectedStrings).contains(s1, s2)
        assertThat(n2.connectedStrings).contains(s1, s3)
      }

      n1.connectedStrings = sizedCollectionOf(s1)

      run {
        val row = ConnectionTable.selectAll().toList()
        assertThat(row).hasSize(3)
        assertThat(n1.connectedStrings).containsOnly(s1)
        assertThat(n2.connectedStrings).containsOnly(s1, s3)
      }
    }
  }

}