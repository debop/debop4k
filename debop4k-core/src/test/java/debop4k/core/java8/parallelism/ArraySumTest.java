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

package debop4k.core.java8.parallelism;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import debop4k.core.java8.model.Album;
import debop4k.core.java8.model.SampleData;
import debop4k.core.java8.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class ArraySumTest {

  @Rule
  public TestRule benchmarkRun = new BenchmarkRule();

  public static List<Album> albums;

  @BeforeClass
  public static void initAlbums() {
    int n = 100000;
    albums = IntStream.range(0, n)
                      .parallel()
                      .mapToObj(i -> SampleData.aLoveSupreme.copy())
                      .collect(Collectors.toList());
  }

  @Test
  public void serialArraySum() {
    albums.stream()
          .flatMap(Album::getTracks)
          .mapToInt(Track::getLength)
          .sum();
  }

  @Test
  public void parallelArraySum() {
    albums.parallelStream()
          .flatMap(Album::getTracks)
          .mapToInt(Track::getLength)
          .sum();
  }

}
