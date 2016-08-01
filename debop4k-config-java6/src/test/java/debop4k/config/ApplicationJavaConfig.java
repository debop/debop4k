/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.config;

import com.typesafe.config.Config;
import debop4k.config.database.DatabaseConfigElement;
import debop4k.config.database.DatabaseConfigSupport;
import debop4k.config.email.EmailConfigElement;
import debop4k.config.email.EmailConfigSupport;
import debop4k.config.hibernate.HibernateConfigElement;
import debop4k.config.hibernate.HibernateConfigSupport;
import debop4k.config.mongodb.MongoDBConfigElement;
import debop4k.config.mongodb.MongoDBConfigSupport;
import debop4k.config.redis.RedisConfigElement;
import debop4k.config.redis.RedisConfigSupport;
import debop4k.config.redis.RedissonConfigElement;
import debop4k.config.redis.RedissonConfigSupport;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * ApplicationJavaConfig
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class ApplicationJavaConfig implements
    DatabaseConfigSupport,
    RedisConfigSupport,
    RedissonConfigSupport,
    MongoDBConfigSupport,
    HibernateConfigSupport,
    EmailConfigSupport {

  private Config config;


  public ApplicationJavaConfig(Config config) {
    this.config = config;
  }


  @NotNull
  @Override
  public DatabaseConfigElement getDatabase() {
    return DatabaseConfigSupport.DefaultImpls.getDatabase(this);
  }

  @NotNull
  @Override
  public EmailConfigElement getEmail() {
    return EmailConfigSupport.DefaultImpls.getEmail(this);
  }

  @NotNull
  @Override
  public HibernateConfigElement getHibernate() {
    return HibernateConfigSupport.DefaultImpls.getHibernate(this);
  }

  @NotNull
  @Override
  public MongoDBConfigElement getMongodb() {
    return MongoDBConfigSupport.DefaultImpls.getMongodb(this);
  }

  @NotNull
  @Override
  public RedisConfigElement getRedis() {
    return RedisConfigSupport.DefaultImpls.getRedis(this);
  }

  @NotNull
  @Override
  public RedissonConfigElement getRedisson() {
    return RedissonConfigSupport.DefaultImpls.getRedisson(this);
  }
}
