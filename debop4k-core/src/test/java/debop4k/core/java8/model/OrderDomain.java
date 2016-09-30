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

package debop4k.core.java8.model;

import java.util.List;
import java.util.function.ToLongFunction;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class OrderDomain extends Order {

  public OrderDomain(List<Album> albums) {
    super(albums);
  }

  public long countFeature(ToLongFunction<Album> function) {
    return albums.stream()
                 .mapToLong(function)
                 .sum();
  }

  @Override
  public long countRunningTime() {
    return countFeature(album -> album.getTracks().mapToLong(Track::getLength).sum());
  }

  @Override
  public long countMusicians() {
    return countFeature(album -> album.getMusicians().count());
  }

  @Override
  public long countTracks() {
    return countFeature(album -> album.getTracks().count());
  }
}
