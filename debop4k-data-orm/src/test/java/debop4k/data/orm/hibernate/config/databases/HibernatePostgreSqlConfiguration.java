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

package debop4k.data.orm.hibernate.config.databases;

import debop4k.data.orm.hibernate.config.AbstractHibernatePostgreSqlConfiguration;
import debop4k.data.orm.mapping.Employee;
import org.hibernate.cache.redis.hibernate5.SingletonRedisRegionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class HibernatePostgreSqlConfiguration extends AbstractHibernatePostgreSqlConfiguration {

  @Override
  public String[] getMappedPackageNames() {
    return new String[]{Employee.class.getPackage().getName()};
  }

  @Override
  public Properties hibernateProperties() {
    Properties props = super.hibernateProperties();

    props.setProperty(HBM2DDL_AUTO, "create");

    // add second cache provider using redis
    props.setProperty(USE_SECOND_LEVEL_CACHE, "true");
    props.setProperty(USE_QUERY_CACHE, "true");
    props.setProperty(CACHE_REGION_PREFIX, "");
    props.setProperty(CACHE_REGION_FACTORY, SingletonRedisRegionFactory.class.getName());
    props.setProperty(CACHE_PROVIDER_CONFIG, "hibernate-redis.properties");

    return props;
  }
}
