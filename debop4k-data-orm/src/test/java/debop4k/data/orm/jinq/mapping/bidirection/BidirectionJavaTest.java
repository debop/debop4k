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

package debop4k.data.orm.jinq.mapping.bidirection;

import debop4k.data.orm.jinq.AbstractJavaJinqTest;
import lombok.extern.slf4j.Slf4j;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BidirectionJavaTest
 *
 * @author debop
 * @since 2017. 2. 15.
 */
@Slf4j
public class BidirectionJavaTest extends AbstractJavaJinqTest {

  @Test
  public void initJinqTest() {
    JinqJPAStreamProvider streams = new JinqJPAStreamProvider(emf);
    assertThat(streams).isNotNull();

    Member member = streams.streamAll(em, Member.class)
                           .where(m -> m.getName().equals("debop"))
                           .findAny()
                           .orElse(null);

    log.debug("member={}", member);
  }
}
