/*
 * Copyright (c) 2016. KESTI co, ltd
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

@file:JvmName("Backoffs")

package debop4k.core.retry.backoff

const val DEFAULT_MULTIPLIER = 0.1

const val DEFAULT_MIN_DELAY_MILLIS = 100L
const val DEFAULT_MAX_DELAY_MILLIS = 10000L

const val DEFAULT_PERIOD_MILLIS = 1000L

const val DEFAULT_RANDOM_RANGE_MILLIS = 100L

@JvmField val DEFAULT_BACKOFF: Backoff = FixedIntervalBackoff()
