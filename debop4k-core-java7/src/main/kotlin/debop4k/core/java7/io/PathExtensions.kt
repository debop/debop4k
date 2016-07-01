/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("paths")

package debop4k.core.java7.io

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun Path.exists(): Boolean = Files.exists(this)
fun Path.notExists(): Boolean = !this.exists()

fun Path.deleteRecursively(): Boolean = this.toFile().deleteRecursively()

fun Path.get(vararg subpaths: String): Path = Paths.get(this.toString(), *subpaths)
