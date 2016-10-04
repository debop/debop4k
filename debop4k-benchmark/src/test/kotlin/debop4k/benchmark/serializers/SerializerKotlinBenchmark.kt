/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.benchmark.serializers

import debop4k.benchmark.models.Cell
import debop4k.core.cryptography.randomBytes
import debop4k.core.io.serializers.BinarySerializer
import debop4k.core.io.serializers.FstSerializer
import debop4k.core.uninitialized
import debop4k.core.utils.Resourcex
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.collections.impl.list.mutable.FastList
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

//
// NOTE: IntelliJ IDEA 용 jmh plugin 은 Java 에서만 가능하고, Kotlin 은 안된다.
//
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
open class SerializerKotlinBenchmark {

  val binary = BinarySerializer()
  val fst = FstSerializer()

  val CELL_COUNT = 100
  var cells: FastList<Cell> = uninitialized()

  @Setup
  open fun setup(): Unit {
    val utfText = Resourcex.getString("Utf8Samples.txt")

    cells = FastList.newList(CELL_COUNT * CELL_COUNT)

    (0 until CELL_COUNT).forEach { r ->
      (0 until CELL_COUNT).forEach { c ->
        val cell = Cell(r, c).apply {
          text = utfText
          bytes = randomBytes(256)
        }
        cells.add(cell)
      }
    }
  }

  @Benchmark
  open fun binarySerializer(): Int {
    val array = binary.serialize(cells)
    val converted = binary.deserialize<FastList<Cell>>(array)
    assertThat(converted).isNotNull().hasSize(cells.size)
    return converted.size
  }

  @Benchmark
  open fun fstSerializer(): Int {
    val array = fst.serialize(cells)
    val converted = fst.deserialize<FastList<Cell>>(array)
    assertThat(converted).isNotNull().hasSize(cells.size)
    return converted.size
  }
}