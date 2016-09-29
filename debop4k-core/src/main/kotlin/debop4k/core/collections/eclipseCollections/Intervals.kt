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

package debop4k.core.collections.eclipseCollections

import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList
import org.eclipse.collections.impl.list.primitive.IntInterval


/**
 * eclipse-collections 의 [IntInterval] 을 사용하여 [IntArrayList], [LongArrayList] 를 만들 수 있다
 */
object Intervals {
  object Ints {
    @JvmOverloads
    fun range(from: Int, to: Int, step: Int = 1): IntArrayList {
      return IntArrayList.newList(IntInterval.fromToBy(from, to, step))
    }

    fun grouped(interval: IntInterval, groupSize: Int): FastList<IntArrayList> {
      assert(groupSize > 0)
      val partitionCount = interval.size() / groupSize + if (interval.size() % groupSize == 0) 0 else 1

      val lists = FastList.newList<IntArrayList>()
      for (i in 0..partitionCount - 1) {
        lists.add(IntArrayList())
      }
      interval.forEachWithIndex { value, index ->
        val partition = index / groupSize
        lists[partition].add(value)
      }

      return lists
    }
  }

  object Longs {
    @JvmOverloads fun
        range(from: Int, to: Int, step: Int = 1): LongArrayList {
      return range(IntInterval.fromToBy(from, to, step))
    }

    fun range(interval: IntInterval): LongArrayList {
      val array = LongArrayList(interval.size())
      val iter = interval.intIterator()
      while (iter.hasNext()) {
        array.add(iter.next().toLong())
      }
      return array
    }

    fun group(interval: IntInterval, groupSize: Int): FastList<LongArrayList> {
      assert(groupSize > 0)
      val partitionCount = interval.size() / groupSize + if (interval.size() % groupSize == 0) 0 else 1

      val lists = FastList.newList<LongArrayList>()
      for (i in 0..partitionCount - 1) {
        lists.add(LongArrayList())
      }
      interval.forEachWithIndex { value, index ->
        val partition = index / groupSize
        lists[partition].add(value.toLong())
      }

      return lists
    }
  }
}
