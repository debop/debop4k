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

package debop4k.data.mybatis.config;

import debop4k.data.mybatis.AbstractMyBatisTest;
import org.apache.ibatis.session.SqlSessionFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class MyBatisConfigurationTest extends AbstractMyBatisTest {

  @Inject SqlSessionFactory sqlSessionFactory;

  @Test
  public void testConfiguration() {
    Assertions.assertThat(sqlSessionFactory).isNotNull();
  }
}
