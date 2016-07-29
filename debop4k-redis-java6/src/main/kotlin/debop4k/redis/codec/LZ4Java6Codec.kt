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

package debop4k.redis.codec

import org.redisson.client.codec.Codec
import org.redisson.codec.LZ4Codec

/**
 * LZ4Java6Codec
 * @author debop sunghyouk.bae@gmail.com
 */
class LZ4Java6Codec(val codec: Codec = FstJava6Codec()) : LZ4Codec(codec) {
}