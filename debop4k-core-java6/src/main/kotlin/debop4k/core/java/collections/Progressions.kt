/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.java.collections

import debop4k.core.max
import debop4k.core.min
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.CharArrayList
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList

fun charProgressionOf(start: Char, endInclusive: Char, step: Int): CharProgression {
  return CharProgression.fromClosedRange(start, endInclusive, step)
}

fun intProgressionOf(start: Int, endInclusive: Int, step: Int): IntProgression {
  return IntProgression.fromClosedRange(start, endInclusive, step)
}

fun longProgressionOf(start: Long, endInclusive: Long, step: Long): LongProgression {
  return LongProgression.fromClosedRange(start, endInclusive, step)
}

fun CharProgression.toCharArrayList(): CharArrayList {
  val array = CharArrayList(this.size())
  this.forEach { array.add(it) }
  return array
}

fun IntProgression.toIntArrayList(): IntArrayList {
  val array = IntArrayList(this.size())
  this.forEach { array.add(it) }
  return array
}

fun LongProgression.toLongArrayList(): LongArrayList {
  val array = LongArrayList(this.size())
  this.forEach { array.add(it) }
  return array
}


/**
 * [IntProgression] 의 요소들을 `groupSize` 만큼의 요소를 가진 서브 그룹의로 만든다.
 * 100개의 요소, groupSize = 25 이면 4개의 IntProgress 의 컬렉션을 반환합니다.
 */
fun IntProgression.grouped(groupSize: Int): FastList<IntProgression> {
  require(groupSize > 0)

  val partitionCount = this.size() / groupSize + (if (this.size() % groupSize == 0) 0 else 1)
  return this.partitioning(partitionCount)
}

fun groupedFromCount(count: Int, groupSize: Int): FastList<IntProgression> {
  return IntProgression
      .fromClosedRange(0, count - 1, 1)
      .grouped(groupSize)
}

@JvmOverloads
fun groupedFromRange(start: Int, endInclusive: Int, step: Int = 1, groupSize: Int = 1): FastList<IntProgression> {
  return IntProgression
      .fromClosedRange(start, endInclusive, step)
      .grouped(groupSize)
}

/**
 * [IntProgression] 을 지정한 파티션 갯수로 그룹을 나눕니다.
 * 100개의 요소가 있는 IntRange 에 partitionCount = 4 라면 요소 25개를 가진 IntRange 4개를 가지는 리스트를 반환합니다.
 */
@JvmOverloads
fun IntProgression.partitioning(partitionCount: Int = 1): FastList<IntProgression> {
  val rangeSize = this.size()
  val stepSign = if (this.step >= 0) 1 else -1
  val step = this.step
  val partitionSize = this.size() / partitionCount
  var remainder = this.size() % partitionCount

  val partitions: FastList<IntProgression> = FastList.newList<IntProgression>()

  var start = this.first
  for (i in 0 until partitionCount) {
    var endInclusive = start + (partitionSize + (if (remainder > 0) 1 else 0)) * stepSign
    if (remainder > 0)
      remainder--

    endInclusive =
        if (step > 0) endInclusive min this.last - 1 * stepSign
        else endInclusive max this.last - 1 * stepSign
    endInclusive += stepSign

    val partition: IntProgression = IntProgression.fromClosedRange(start, endInclusive, step)
    partitions.add(partition)
    start = endInclusive + 1
  }

  return partitions
}

fun partitioningFromCount(count: Int, partitionCount: Int): FastList<IntProgression> {
  return IntProgression.fromClosedRange(0, count - 1, 1).partitioning(partitionCount)
}

@JvmOverloads
fun partitioningFromRange(start: Int,
                          endInclusive: Int,
                          step: Int = 1,
                          partitionCount: Int = 1): FastList<IntProgression> {
  return IntProgression.fromClosedRange(start, endInclusive, step).partitioning(partitionCount)
}

/**
 * [LongProgression] 의 요소들을 `groupSize` 만큼의 요소를 가진 서브 그룹의로 만든다.
 * 100개의 요소, groupSize = 25 이면 4개의 IntProgress 의 컬렉션을 반환합니다.
 */
fun LongProgression.grouped(groupSize: Long): FastList<LongProgression> {
  require(groupSize > 0)

  val partitionCount = this.size() / groupSize + (if (this.size() % groupSize == 0L) 0 else 1)
  return this.partitioning(partitionCount)
}

fun groupedLongFromCount(count: Long, groupSize: Long): FastList<LongProgression> {
  return LongProgression
      .fromClosedRange(0, count - 1, 1)
      .grouped(groupSize)
}

@JvmOverloads
fun groupedLongFromRange(start: Long,
                         endInclusive: Long,
                         step: Long = 1,
                         groupSize: Long = 1L): FastList<LongProgression> {
  return LongProgression
      .fromClosedRange(start, endInclusive, step)
      .grouped(groupSize)
}

/**
 * [LongProgression] 을 지정한 파티션 갯수로 그룹을 나눕니다.
 * 100개의 요소가 있는 LongRange 에 partitionCount = 4 라면 요소 25개를 가진 LongRange 4개를 가지는 리스트를 반환합니다.
 */
@JvmOverloads
fun LongProgression.partitioning(partitionCount: Long = 1L): FastList<LongProgression> {
  val rangeSize = this.size()
  val stepSign = if (this.step >= 0) 1 else -1
  val step = this.step
  val partitionSize = this.size() / partitionCount
  var remainder = this.size() % partitionCount

  val partitions: FastList<LongProgression> = FastList.newList<LongProgression>()

  var start = this.first
  for (i in 0 until partitionCount) {
    var endInclusive = start + (partitionSize + (if (remainder > 0) 1 else 0)) * stepSign
    if (remainder > 0)
      remainder--

    endInclusive =
        if (step > 0) endInclusive min this.last - 1 * stepSign
        else endInclusive max this.last - 1 * stepSign
    endInclusive += stepSign

    val partition: LongProgression = LongProgression.fromClosedRange(start, endInclusive, step)
    partitions.add(partition)
    start = endInclusive + 1
  }

  return partitions
}

fun partitioningFromCount(count: Long, partitionCount: Long): FastList<LongProgression> {
  return LongProgression.fromClosedRange(0, count - 1, 1).partitioning(partitionCount)
}

@JvmOverloads
fun partitioningFromRange(start: Long,
                          endInclusive: Long,
                          step: Long = 1,
                          partitionCount: Long = 1): FastList<LongProgression> {
  return LongProgression.fromClosedRange(start, endInclusive, step).partitioning(partitionCount)
}

