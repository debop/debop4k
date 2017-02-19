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
 */

package debop4k.data.orm.spring.boot.autoconfigure;

import debop4k.core.utils.Stringx;
import debop4k.data.JdbcDrivers;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * Hibernate 환경설정 정보
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
@ConfigurationProperties(prefix = HibernateProperties.PREFIX)
public class HibernateProperties {

  public static final String PREFIX = "debop4k.hibernate";

  private String[] mappedPackageNames;

  public String[] getMappedPackageNames() {
    return mappedPackageNames.clone();
  }

  public void setMappedPackageNames(String[] packageNames) {
    mappedPackageNames = packageNames.clone();
  }

  private String dialect = JdbcDrivers.DIALECT_H2;

  private String hbm2ddl = "none";
  private Boolean showSql = true;
  private Boolean formatSql = true;

  private Integer batchFetchSize = 30;

  private String isolation = null;  // ex : TRANSACTION_READ_COMMITTED,  TRANSACTION_REPEATABLE_READ
  private Boolean autoCommit = true;
  private String releaseMode = "after_transaction";


  private Boolean useSecondCache = false;
  private Resource cacheProviderConfig;

  @SneakyThrows({IOException.class})
  public Properties toProperties() {
    Properties props = new Properties();

    props.put(AvailableSettings.DIALECT, dialect);

    props.put(AvailableSettings.HBM2DDL_AUTO, hbm2ddl);
    props.put(AvailableSettings.SHOW_SQL, showSql);
    props.put(AvailableSettings.FORMAT_SQL, formatSql);

    props.put(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, batchFetchSize);

    if (Stringx.isNotEmpty(isolation)) {
      props.put(AvailableSettings.ISOLATION, isolation);
    }
    props.put(AvailableSettings.AUTOCOMMIT, autoCommit);
    props.put(AvailableSettings.RELEASE_CONNECTIONS, releaseMode);

    props.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, useSecondCache);
    props.put(AvailableSettings.CACHE_PROVIDER_CONFIG, cacheProviderConfig.getFile().getAbsolutePath());

    return props;
  }
}
