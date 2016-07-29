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

@file:JvmName("RedissonConst")

package debop4k.redis

const val DEFAULT_HOST: String = "127.0.0.1"
const val DEFAULT_PORT: Int = 6379

val DEFAULT_ADDRESS = DEFAULT_HOST + ":" + DEFAULT_PORT

const val DEFAULT_SENTINEL_PORT = 26379
const val DEFAULT_TIMEOUT = 2000
const val DEFAULT_DATABASE = 0

const val DEFAULT_CHARSET = "UTF-8"

const val DEFAULT_LOGBACK_CHANNEL = "channel:logback:logs"

const val DEFAULT_DELIMETER = ":"