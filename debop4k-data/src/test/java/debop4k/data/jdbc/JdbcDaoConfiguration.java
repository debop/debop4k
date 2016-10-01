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

package debop4k.data.jdbc;

import debop4k.data.config.AbstractDataSourceConfiguration;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class JdbcDaoConfiguration extends AbstractDataSourceConfiguration {

  @Bean
  public DataSource dataSource() {
    DataSource ds = new EmbeddedDatabaseBuilder().generateUniqueName(true)
                                                 .setType(EmbeddedDatabaseType.H2)
                                                 .setScriptEncoding("UTF-8")
                                                 .ignoreFailedDrops(true)
                                                 .addScript("database/schema.sql")
                                                 .addScript("database/data.sql")
                                                 .build();
    // NOTE: Log4jdbc Remix 를 사용하면 실행되는 SQL 구문과 결과를 읽기 쉬운 포맷으로 뿌려줍니다.
    return new Log4jdbcProxyDataSource(ds);
  }

}
