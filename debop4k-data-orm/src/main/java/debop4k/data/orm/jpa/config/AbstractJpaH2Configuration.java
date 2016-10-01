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
import debop4k.data.JdbcDrivers;
import org.hibernate.cfg.Environment;

import java.util.Properties;

/**
 * H2 Database 용 JPA Configuration
 *
 * @author sunghyouk.bae@gmail.com
 */
public abstract class AbstractJpaH2Configuration extends AbstractJpaConfiguration {

  @Override
  protected DatabaseSetting getDatabaseSetting() {
    return DatabaseSetting.builder()
                          .driverClass(JdbcDrivers.DRIVER_CLASS_H2)
                          .jdbcUrl("jdbc:h2:mem:" + getDatabaseName() + ";DB_CLOSE_ON_EXIT=FALSE;MVCC=TRUE;")
                          .username("sa")
                          .password("")
                          .build();
  }

  @Override
  public Properties jpaProperties() {
    Properties props = super.jpaProperties();
    props.put(Environment.DIALECT, JdbcDrivers.DIALECT_H2);
    return props;
  }
}
