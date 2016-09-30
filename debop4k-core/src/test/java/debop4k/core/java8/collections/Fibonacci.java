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

import org.eclipse.collections.api.map.ConcurrentMutableMap;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;

/**
 * Fibonacci
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 10.
 */
public final class Fibonacci {

  private Fibonacci() {}

  private static final ConcurrentMutableMap<Integer, Long> cache;

  static {
    cache = ConcurrentHashMap.newMap();
    cache.put(0, 0L);
    cache.put(1, 1L);
  }

  public static Long get(int x) {
    return cache.computeIfAbsent(x, n -> get(n - 1) + get(n - 2));
  }
}
