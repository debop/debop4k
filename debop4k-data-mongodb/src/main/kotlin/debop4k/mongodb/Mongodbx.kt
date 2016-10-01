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

package debop4k.mongodb

import debop4k.core.io.serializers.Serializers
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.query.Criteria

/**
 * Mongo용 Document를 복사합니다.
 *
 * @param <T> Document 의 수형
 * @return 복사된 Document
 */
fun <T : AbstractMongoDocument> T.copy(): T {
  return Serializers.FST.copy(this)
}

/**
 * Mongo용 Document를 복사합니다. 단 복사된 Document 는 Transient object (id 값이 할당되지 않은) 입니다.
 *
 * @param <T> Document 의 수형
 * @return 복사된 Document
 */
fun <T : AbstractMongoDocument> T.copyAndResetId(): T {
  return Serializers.FST.copy(this).apply {
    resetIdentifier()
  }
}

fun aggregationOf(vararg operations: AggregationOperation): Aggregation {
  return Aggregation.newAggregation(*operations)
}

fun aggregationOf(operations: List<AggregationOperation>): Aggregation {
  return Aggregation.newAggregation(operations)
}

fun MutableList<AggregationOperation>.addCriterias(vararg criterias: Criteria) {
  criterias.forEach {
    add(Aggregation.match(it))
  }
}
