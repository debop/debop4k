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

package debop4k.redisson.spring.factory;///*
// *  Copyright (c) 2016. KESTI co, ltd
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.redisson.spring.factory;
//
//import debop4k.redisson.spring.RedissonTemplate;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.FactoryBean;
//
///**
// * {@link RedissonTemplate} 생성을 위한 Spring Factory Bean
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Slf4j
//public class RedissonTemplateFactoryBean implements FactoryBean<RedissonTemplate> {
//
//  @Getter
//  @Setter
//  private Config config;
//
//  @Override
//  public RedissonTemplate getObject() throws Exception {
//    log.debug("create RedisTemplate instance ...");
//
//    RedissonClient redissonClient = Redisson.create(config);
//
//    log.debug("create RedisTemplate instance is success.");
//    return new RedissonTemplate(redissonClient);
//  }
//
//  @Override
//  public Class<?> getObjectType() {
//    return RedissonTemplate.class;
//  }
//
//  @Override
//  public boolean isSingleton() {
//    return true;
//  }
//
//}
