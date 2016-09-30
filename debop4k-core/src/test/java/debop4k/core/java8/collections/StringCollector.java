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

package debop4k.core.java8.collections;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class StringCollector implements Collector<String, StringCombiner, String> {

  private static final Set<Characteristics> characteristics = Collections.emptySet();

  private final String delim;
  private final String prefix;
  private final String suffix;

  public StringCollector(String delim, String prefix, String suffix) {
    this.delim = delim;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public Supplier<StringCombiner> supplier() {
    return () -> new StringCombiner(delim, prefix, suffix);
  }

  @Override
  public BiConsumer<StringCombiner, String> accumulator() {
    return StringCombiner::add;
  }

  @Override
  public BinaryOperator<StringCombiner> combiner() {
    return StringCombiner::merge;
  }

  @Override
  public Function<StringCombiner, String> finisher() {
    return StringCombiner::toString;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return characteristics;
  }
}