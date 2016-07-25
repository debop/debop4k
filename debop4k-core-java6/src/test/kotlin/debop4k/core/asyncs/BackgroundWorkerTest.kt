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

package debop4k.core.asyncs

import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.inject.Inject

/**
 * BackgroundWorkerTest
 * @author debop sunghyouk.bae@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(BackgroundWorkerConfiguration::class))
class BackgroundWorkerTest {

  private val log = LoggerFactory.getLogger(javaClass)

  @Inject val worker: DummyBackgroundWorker = uninitialized()

  @Test
  fun injectDummyBackgroundWorker() {
    assertThat(worker).isNotNull()
    Thread.sleep(500)
    assertThat(worker.isRunning).isTrue()
    Thread.sleep(500)
  }
}