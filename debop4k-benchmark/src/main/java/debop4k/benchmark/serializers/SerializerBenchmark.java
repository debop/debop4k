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

package debop4k.benchmark.serializers;


import debop4k.benchmark.models.Cell;
import debop4k.core.cryptography.Cryptographyx;
import debop4k.core.io.serializers.BinarySerializer;
import debop4k.core.io.serializers.FstSerializer;
import debop4k.core.io.serializers.Serializer;
import debop4k.core.utils.Resources;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class SerializerBenchmark {

  private static final Serializer binary = new BinarySerializer();
  private static final Serializer fst = new FstSerializer();

  private static final int CELL_COUNT = 100;
  private List<Cell> cells;

  @Setup
  public void setup() {
    String text = Resources.getString("Utf8Samples.txt");

    cells = FastList.newList(CELL_COUNT * CELL_COUNT);

    for (int r = 0; r < CELL_COUNT; r++) {
      for (int c = 0; c < CELL_COUNT; c++) {
        Cell cell = Cell.of(r, c);
        cell.setText(text);
        cell.setBytes(Cryptographyx.randomBytes(256));
        cells.add(cell);
      }
    }
  }

  @Benchmark
  public int binarySerializer() {
    byte[] array = binary.serialize(cells);
    List<Cell> converted = binary.deserialize(array);
//    assertThat(converted).isNotNull().hasSize(cells.size());
    return converted.size();
  }

  @Benchmark
  public int fstSerializer() {
    byte[] array = fst.serialize(cells);
    List<Cell> converted = fst.deserialize(array);
//    assertThat(converted).isNotNull().hasSize(cells.size());
    return converted.size();
  }
}
