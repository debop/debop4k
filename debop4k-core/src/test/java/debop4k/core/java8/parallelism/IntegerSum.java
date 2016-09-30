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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * 병렬 작업 시 DataSource 중에 분할이 용이한 자료구조가 Serial 작업 대비 병렬 작업으로 변경의 효과가 크다.
 * <p>
 * 1. Excellent : array, IntStream.range, IntArrayList, List, FastList
 * 2. Good : HashSet, HashMap
 * 3. Bad : LinkedList
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class IntegerSum {

  @Rule
  public TestRule benchmarkRun = new BenchmarkRule();

  private static final int size = 10000000;

  public static int[] array;
  public static List<Integer> arrayList;
  public static FastList<Integer> fastArrayList;
  public static IntArrayList intArrayList;
  public static LinkedList<Integer> linkedList;
  public static TreeSet<Integer> treeSet;
  public static HashSet<Integer> hashSet;

  @BeforeClass
  public static void createDataSource() {
    array = IntStream.range(0, size).toArray();
    arrayList = IntStream.range(0, size).boxed().collect(toList());
    fastArrayList = IntStream.range(0, size).boxed().collect(toCollection(FastList::new));
    intArrayList = new IntArrayList(array);
    linkedList = new LinkedList<>(arrayList);
    treeSet = new TreeSet<>(arrayList);
    hashSet = new HashSet<>(arrayList);
  }

  @Test
  public void range() {
    int sum = IntStream.range(0, size).parallel().sum();
  }

  @Test
  public void serialRange() {
    int sum = IntStream.range(0, size).sum();
  }

  @Test
  public void array() {
    int sum = IntStream.of(array).parallel().sum();
  }

  @Test
  public void arrayList() {
    int sum = arrayList.parallelStream().mapToInt(i -> i).sum();
  }

  @Test
  public void fastArrayList() {
    int sum = fastArrayList.parallelStream().mapToInt(i -> i).sum();
  }

  @Test
  public void intArrayList() {
    long sum = intArrayList.sum();
  }

  @Test
  public void linkedList() {
    int sum = linkedList.parallelStream().mapToInt(i -> i).sum();
  }

  @Test
  public void treeSet() {
    int sum = treeSet.parallelStream().mapToInt(i -> i).sum();
  }

  @Test
  public void hashSet() {
    int sum = hashSet.parallelStream().mapToInt(i -> i).sum();
  }

  @Test
  public void serialArray() {
    int sum = IntStream.of(array).sum();
  }

  @Test
  public void serialArrayList() {
    int sum = arrayList.stream().mapToInt(i -> i).sum();
  }

  @Test
  public void serialFastArrayList() {
    int sum = fastArrayList.stream().mapToInt(i -> i).sum();
  }

  @Test
  public void serialIintArrayList() {
    long sum = intArrayList.sum();
  }

  @Test
  public void serialLinkedList() {
    int sum = linkedList.stream().mapToInt(i -> i).sum();
  }

  @Test
  public void serialTreeSet() {
    int sum = treeSet.stream().mapToInt(i -> i).sum();
  }

  @Test
  public void serialHashSet() {
    int sum = hashSet.stream().mapToInt(i -> i).sum();
  }


}
