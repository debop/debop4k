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

package debop4k.data.exposed.spring.transactions

import debop4k.data.DataSources
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.TransactionManagementConfigurer

/**
 * ExposedTransactionManagerConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
open class ExposedTransactionManagerConfiguration : TransactionManagementConfigurer {

  @Bean
  open fun dataSource() = DataSources.ofEmbeddedH2()

  @Bean
  override fun annotationDrivenTransactionManager(): PlatformTransactionManager {
    return SpringTransactionManager(dataSource())
  }
}