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

package debop4k.core.java8.testing;

import debop4k.core.java8.model.Album;
import debop4k.core.java8.model.OrderDomain;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class OrderDomainExampleTest {

  @Test
  public void canCountFeature() {
    OrderDomain order = new OrderDomain(asList(
        newAlbum("Exile on Main St."),
        newAlbum("Beggars Banquet"),
        newAlbum("Aftermath"),
        newAlbum("Let it Bleed")));

    assertThat(order.countFeature(album -> 2)).isEqualTo(8);
  }

  private Album newAlbum(String name) {
    return new Album(name, FastList.newList(), FastList.newList());
  }
}
