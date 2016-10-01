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

package debop4k.data.orm.jpa.config;

import debop4k.config.database.DatabaseSetting;
import org.hibernate.cfg.Environment;

import java.util.Properties;

import static debop4k.data.JdbcDrivers.DIALECT_HSQL;
import static debop4k.data.JdbcDrivers.DRIVER_CLASS_HSQL;

/**
 * HSql Database 용 JPA Configuration
 *
 * @author sunghyouk.bae@gmail.com
 */
public abstract class AbstractJpaHSqlConfiguration extends AbstractJpaConfiguration {

  @Override
  protected DatabaseSetting getDatabaseSetting() {
    return DatabaseSetting.builder()
                          .driverClass(DRIVER_CLASS_HSQL)
                          .jdbcUrl("jdbc:hsqldb:mem:" + getDatabaseName() + ";MVCC=TRUE;")
                          .username("sa")
                          .password("")
                          .testQuery("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")
                          .build();
  }

  @Override
  public Properties jpaProperties() {
    Properties props = super.jpaProperties();
    props.put(Environment.DIALECT, DIALECT_HSQL);
    return props;
  }
}
