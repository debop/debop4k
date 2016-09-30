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

package debop4k.core.java8.concurrency.rxjava;

import debop4k.core.java8.model.Artist;
import debop4k.core.java8.model.SampleData;
import org.junit.Test;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author sunghyouk.bae@gmail.com
 */
public class RxExampleTest {

  @Test
  public void filtersAlbums() throws InterruptedException {
    RxExamples examples = new RxExamples(SampleData.getThreeArtists());
    Observable<Artist> observable = examples.search("John", "UK", 5);

    assertThat(observable).isNotNull();
    Artist artist = examples.search("John", "UK", 5)
                            .toBlocking()
                            .single();
    assertThat(artist).isEqualTo(SampleData.johnLennon);
  }
}
