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

package debop4k.redisson.kotlin

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.redisson.kotlin.config.RedissonKotlinConfigration
import org.junit.runner.RunWith
import org.redisson.api.RedissonClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.inject.Inject

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = arrayOf(RedissonKotlinConfigration::class))
abstract class AbstractRedissonKotlinTest {

  protected val log = loggerOf(javaClass)

  @Inject val redisson: RedissonClient = uninitialized()
}