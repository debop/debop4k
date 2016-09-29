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

package debop4k.data;

import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSourcesJavaTest {

  @Test
  public void testEmbeddedHsql() throws Exception {
    DataSource ds = DataSources.ofEmbeddedHsql();
    assertThat(ds).isNotNull();

    Connection conn = ds.getConnection();
    try {
      assertThat(conn).isNotNull();
    } finally {
      conn.close();
    }
  }
}
