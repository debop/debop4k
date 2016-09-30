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

package debop4k.core.java8.concurrency.artistAnalyzer;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(Parameterized.class)
public class ArtistAnalyzerTest {

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    FakeLookupService lookupService = new FakeLookupService();
    return FastList.newListWith(
        new Object[]{new CallbackArtistAnalyzer(lookupService::lookupArtistName)},
        new Object[]{new CompletableFutureArtistAnalyzer(lookupService::lookupArtistName)}
                               );
  }

  private final ArtistAnalyzer analyzer;

  public ArtistAnalyzerTest(ArtistAnalyzer analyzer) {
    this.analyzer = analyzer;
  }

  @Test
  public void largeGroupsAreLarger() {
    assertLargerGroup(true, "The Beatles", "John Coltrane");
  }

  @Test
  public void smallerGroupsArentLarger() {
    assertLargerGroup(false, "John Coltrane", "The Beatles");
  }

  private void assertLargerGroup(boolean expected, String artistName, String otherArtistName) {
    AtomicBoolean isLarger = new AtomicBoolean(!expected);
    analyzer.isLargerGroup(artistName, otherArtistName, isLarger::set);
    assertThat(isLarger.get()).isEqualTo(expected);
  }
}
