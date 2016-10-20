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

package debop4k.redisson.spring

import org.redisson.api.RedissonClient

/**
 * {@link RedissonClient} 를 이용하여 여러가지 추가 기능을 제공할 예정입니다.
 * <p>
 * 참고: https://github.com/mrniko/redisson/wiki/8.-Redis-commands-mapping
 * Kotlin 의 class delegate 기능을 이용한다
 *
 * @author sunghyouk.bae@gmail.com
 */
class RedissonTemplate(val redisson: RedissonClient) : RedissonClient by redisson {

}