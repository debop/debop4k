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

package debop4k.data.mybatis.migration;

import debop4k.data.DataSources;
import debop4k.data.mybatis.AbstractMyBatisTest;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.Test;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class FlywayMigrationTest extends AbstractMyBatisTest {

  @Inject DataSource dataSource;

  @Test
  public void migration() {
    Flyway flyway = new Flyway();

    flyway.setDataSource(DataSources.ofEmbeddedH2());

    flyway.migrate();

    MigrationInfo[] existingProdInfos = flyway.info().all();
    log.debug("production status:");
    for (MigrationInfo info : existingProdInfos) {
      log.debug("Status: {}, version={}", info.getState(), info.getVersion());
    }

  }
}
