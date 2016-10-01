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

package debop4k.mongodb.bulk

import com.mongodb.BasicDBObjectBuilder
import debop4k.core.asyncs.readyAll
import debop4k.core.collections.fastListOf
import debop4k.core.collections.parForEachWithIndex
import debop4k.core.collections.parGroupBy
import debop4k.core.uninitialized
import debop4k.mongodb.AbstractMongoKotlinTest
import debop4k.mongodb.config.KotlinMongoConfiguration
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.collections.impl.list.mutable.FastList
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.BulkOperations.BulkMode.UNORDERED
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.inject.Inject

/**
 * NOTE: 대용량 데이터를 Insert 하는 경우
 * NOTE: [MongoRepository]를 사용하는 것보다 [MongoTemplate]를 이용하여 bulk operation 을 수행하면 수행 속도가 150% 정도 빠르다!!!
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(KotlinMongoConfiguration::class))
class BulkInsertTest : AbstractMongoKotlinTest() {

  val itemCount = 4000000
  val batchSize = 1000

  @Inject private val mongo: MongoTemplate = uninitialized()
  @Inject private val repo: BulkEntityRepository = uninitialized()

  val rnd: Random = Random()

  @Before
  fun setup() {
    val coll = mongo.db.getCollection("bulkEntity")
    coll.drop()
  }

  @Test
  fun configurationTest() {
    assertThat(mongo).isNotNull()
    assertThat(repo).isNotNull()
  }

  // NOTE: SpringRepository 말고, 직접 MongoTemplate를 사용해서 bulkInsert 를 수행하면 속도가 엄청 빨라진다.
  @Test
  fun bulkInsertByMongoTemplate() {

    mongo.bulkOps(UNORDERED, BulkEntity::class.java)
    val coll = mongo.db.getCollection("bulkEntity")

    var bop = coll.initializeUnorderedBulkOperation()

    for (i in 1..itemCount) {
      val dbo = BasicDBObjectBuilder()
          .append("name", "name-$i")
          .append("longitude", rnd.nextDouble())
          .append("latitude", rnd.nextDouble())
          .get()

      bop.insert(dbo)

      if (i % batchSize == 0) {
        bop.execute()
        bop = coll.initializeUnorderedBulkOperation()
      }
    }

    assertThat(repo.count()).isEqualTo(itemCount.toLong())
  }

  // 로컬에서 테스트해서 그런지 Serial Batch Insert 보다 느리다
  @Test
  fun bulkInsertAsParallelByMongoTemplate() {

    mongo.bulkOps(UNORDERED, BulkEntity::class.java)
    val coll = mongo.db.getCollection("bulkEntity")

    val tasks = fastListOf<Promise<Int, Exception>>()

    (1..itemCount)
        .parGroupBy(4, { it % 4 })
        .multiValuesView()
        .parForEachWithIndex { richIterable, i ->
          println("execute batch insert. group id=$i")
          val insertTask = task {
            var bop = coll.initializeUnorderedBulkOperation()

            richIterable.forEach {
              val dbo = BasicDBObjectBuilder()
                  .append("name", "name-$i")
                  .append("longitude", rnd.nextDouble())
                  .append("latitude", rnd.nextDouble())
                  .get()

              bop.insert(dbo)
            }
            bop.execute()
            richIterable.size()
          }

          tasks.add(insertTask)
        }

    tasks.readyAll()

    assertThat(repo.count()).isEqualTo(itemCount.toLong())
  }

  @Test
  fun bulkInsertBySpringDataMongo() {
    val entities = FastList.newList<BulkEntity>(batchSize)
    for (i in 1..itemCount) {
      val entity = BulkEntity().apply {
        name = "name-$i"
        longitude = rnd.nextDouble()
        latitude = rnd.nextDouble()
      }
      entities.add(entity)

      if (i % batchSize == 0) {
        repo.insert(entities)
        entities.clear()
      }
    }

    assertThat(repo.count()).isEqualTo(itemCount.toLong())
  }

  @Test
  fun bulkInsertAsParallelBySpringDataMongo() {

    val tasks = fastListOf<Promise<Int, Exception>>()

    (1..itemCount)
        .parGroupBy(4, { it % 4 })
        .multiValuesView()
        .parForEachWithIndex { richIterable, i ->
          println("execute batch insert. group id=$i")
          val insertTask = task {

            val entities = FastList.newList<BulkEntity>(batchSize)
            richIterable.forEach {
              val entity = BulkEntity().apply {
                name = "name-$i"
                longitude = rnd.nextDouble()
                latitude = rnd.nextDouble()
              }
              entities.add(entity)
            }
            repo.insert(entities)
            richIterable.size()
          }

          tasks.add(insertTask)
        }

    tasks.readyAll()

    assertThat(repo.count()).isEqualTo(itemCount.toLong())
  }
}

