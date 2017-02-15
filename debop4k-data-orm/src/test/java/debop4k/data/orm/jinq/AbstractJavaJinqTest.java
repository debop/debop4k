/*
 * Copyright (c) 2017-2020 by Coupang. Some rights reserved.
 * See the project homepage at: http://gitlab.coupang.net
 * Licensed under the Coupang License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://coupang.net/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.data.orm.jinq;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AbstractJavaJinqTest
 *
 * @author debop
 * @since 2017. 2. 15.
 */
@Slf4j
abstract public class AbstractJavaJinqTest {

  public static final String persistenceName = "jinq_pu";
  protected static EntityManagerFactory emf;
  protected EntityManager em;

  @BeforeClass
  public static void setupClass() {
    emf = Persistence.createEntityManagerFactory(persistenceName);
    assertThat(emf).isNotNull();
  }

  @Before
  public void setup() {
    em = emf.createEntityManager();
  }

  @After
  public void cleanup() {
    em.close();
  }

  @AfterClass
  public static void cleanupClass() {
    emf.close();
  }
}
