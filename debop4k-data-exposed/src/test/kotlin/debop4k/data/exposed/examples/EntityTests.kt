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

import com.fasterxml.uuid.Generators
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.junit.Test

object EntityData {

  object YTable : IdTable<String>("") {
    override val id: Column<EntityID<String>> = varchar("uuid", 36).primaryKey().entityId().clientDefault {
      EntityID(Generators.timeBasedGenerator().generate().toString(), YTable)
    }

    val x = bool("x").default(true)
  }

  object XTable : IntIdTable() {
    val b1 = bool("b1").default(true)
    val b2 = bool("b2").default(false)
    val y1 = optReference("y1", YTable)
  }

  class XEntity(id: EntityID<Int>) : Entity<Int>(id) {
    var b1 by XTable.b1
    var b2 by XTable.b2

    companion object : EntityClass<Int, XEntity>(XTable)
  }

  enum class XType {
    A, B
  }

  open class AEntity(id: EntityID<Int>) : IntEntity(id) {
    var b1 by XTable.b1

    companion object : IntEntityClass<AEntity>(XTable) {
      fun create(b1: Boolean, type: XType): AEntity {
        val init: AEntity.() -> Unit = {
          this.b1 = b1
        }
        val answer = when (type) {
          XType.B -> BEntity.create { init() }
          else    -> new { init() }
        }
        return answer
      }
    }
  }

  open class BEntity(id: EntityID<Int>) : AEntity(id) {
    var b2 by XTable.b2
    var y by YEntity optionalReferencedOn XTable.y1

    companion object : IntEntityClass<BEntity>(XTable) {
      fun create(init: AEntity.() -> Unit): BEntity {
        return new { init() }
      }
    }
  }

  class YEntity(id: EntityID<String>) : Entity<String>(id) {

    companion object : EntityClass<String, YEntity>(YTable)

    var x by YTable.x
    val b: BEntity? by BEntity.backReferencedOn(XTable.y1)
  }
}

class EntityTests : DatabaseTestBase() {

  @Test
  fun testDefaults01() {
    withTables(EntityData.YTable,
               EntityData.XTable) {
      val x = EntityData.XEntity.new {}

      assertThat(x.b1).isTrue()
      assertThat(x.b2).isFalse()
    }
  }

  @Test
  fun testDefaults02() {
    withTables(EntityData.YTable, EntityData.XTable) {
      val a: EntityData.AEntity = EntityData.AEntity.create(false, EntityData.XType.A)
      assertThat(a.b1).isFalse()

      val b: EntityData.BEntity =
          EntityData.AEntity.create(false, EntityData.XType.B) as EntityData.BEntity
      val y = EntityData.YEntity.new { x = false }

      b.y = y

      assertThat(b.y!!.x).isFalse()
      assertThat(y.b).isNotNull()
    }
  }

  @Test
  fun testBackReference01() {
    withTables(EntityData.YTable, EntityData.XTable) {
      val y = EntityData.YEntity.new {}
      flushCache()
      val b = EntityData.BEntity.new {}
      b.y = y

      assertThat(y.b).isEqualTo(b)
    }
  }

  @Test
  fun testBackReference02() {
    withTables(EntityData.YTable, EntityData.XTable) {
      val b = EntityData.BEntity.new {}
      flushCache()
      val y = EntityData.YEntity.new {}
      b.y = y

      assertThat(y.b).isEqualTo(b)
    }
  }


  object Boards : IntIdTable(name = "board") {
    val name = varchar("name", 255).index(isUnique = true)
  }

  object Posts : IntIdTable(name = "posts") {
    val board = optReference("board", Boards)
    val parent = optReference("parent", this)
  }

  class Board(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Board>(Boards)

    var name by Boards.name
  }

  class Post(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Post>(Posts)

    var board by Board optionalReferencedOn Posts.board
    var parent by Post optionalReferencedOn Posts.parent
  }

  @Test
  fun tableSelfReferenceTest() {
    val sorted = EntityCache.sortTablesByReferences(listOf(Posts, Boards))
    assertThat(sorted).isEqualTo(listOf(Boards, Posts))
  }

  @Test
  fun testInsertChildWithoutFlush() {
    withTables(Posts) {
      val parent = Post.new {}
      Post.new { this.parent = parent }
      assertThat(flushCache()).hasSize(2)
    }
  }

  @Test
  fun testInsertNonChildWithoutFlush() {
    withTables(Boards, Posts) {
      val board = Board.new { name = "irrelevant" }
      Post.new { this.board = board }
      assertThat(flushCache()).hasSize(2)
    }
  }

  @Test
  fun testInsertChildWithFlush() {
    withTables(Posts) {
      val parent = Post.new {}
      flushCache()
      assertThat(parent.id._value).isNotNull()

      Post.new { this.parent = parent }
      assertThat(flushCache()).hasSize(1)
    }
  }

  @Test
  fun testInsertChildWithChild() {
    withTables(Posts) {
      val parent = Post.new {}
      val child1 = Post.new { this.parent = parent }
      Post.new { this.parent = child1 }
      assertThat(flushCache()).hasSize(3)
    }
  }
}