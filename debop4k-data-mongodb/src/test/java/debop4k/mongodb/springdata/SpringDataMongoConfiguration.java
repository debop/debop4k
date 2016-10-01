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

package debop4k.mongodb.springdata;

import debop4k.mongodb.spring.boot.autoconfigure.MongodbAutoConfiguration;
import debop4k.mongodb.springdata.model.Customer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.inject.Inject;
import java.util.List;

@Configuration
@EnableMongoRepositories(basePackageClasses = {SpringDataMongoConfiguration.class})
@ComponentScan(basePackageClasses = SpringDataMongoConfiguration.class)
public class SpringDataMongoConfiguration extends MongodbAutoConfiguration {

  @Override
  public String getMappingBasePackage() {
    return Customer.class.getPackage().getName();
  }

  @Inject private List<Converter<?, ?>> converters;

  @Override
  public CustomConversions customConversions() {
    return new CustomConversions(converters);
  }
}
