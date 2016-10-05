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
@file:JvmName("annotations")

package debop4k.spring.config

import org.springframework.context.annotation.Profile
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.TYPE


@Target(TYPE)
@Retention(RUNTIME)
@Profile("local")
annotation class LocalProfile

@Target(TYPE)
@Retention(RUNTIME)
@Profile("dev", "develop", "development")
annotation class DevelopProfile

@Target(TYPE)
@Retention(RUNTIME)
@Profile("test")
annotation class TestProfile

@Target(TYPE)
@Retention(RUNTIME)
@Profile("prod", "production")
annotation class ProductionProfile