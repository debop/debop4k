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

package debop4k.core.json

import debop4k.core.compress.Compressor
import debop4k.core.cryptography.Encryptor
import debop4k.core.utils.toUtf8Bytes
import debop4k.core.utils.toUtf8String

/**
 * Json 직렬화를 위한 Decorator
 * @author sunghyouk.bae@gmail.com
 */
abstract class JsonSerializerDecorator(val serializer: JsonSerializer) : JsonSerializer by serializer {

//  override fun <T : Any> toByteArray(graph: T?): ByteArray {
//    return serializer.toByteArray(graph)
//  }
//
//  override fun <T : Any> fromByteArray(jsonBytes: ByteArray?, clazz: Class<T>): T? {
//    return serializer.fromByteArray(jsonBytes, clazz)
//  }

  override fun <T : Any> toString(graph: T?): String {
    return toByteArray(graph).toUtf8String()
  }

  override fun <T : Any> fromString(jsonText: String?, clazz: Class<T>): T? {
    return fromByteArray(jsonText.toUtf8Bytes(), clazz)
  }
}

/**
 * Json 직렬화 정보를 압축하여 제공합니다.
 */
class CompressableJsonSerializer
@JvmOverloads constructor(val compressor: Compressor,
                          serializer: JsonSerializer = JacksonSerializer())
: JsonSerializerDecorator(serializer) {

  override fun <T : Any> toByteArray(graph: T?): ByteArray {
    return compressor.compress(super.toByteArray(graph))
  }

  override fun <T : Any> fromByteArray(jsonBytes: ByteArray?, clazz: Class<T>): T? {
    return super.fromByteArray(compressor.decompress(jsonBytes), clazz)
  }
}

/**
 * Json 직렬화 정보를 암호화하여 제공합니다.
 */
class EncryptableJsonSerializer
@JvmOverloads constructor(val encryptor: Encryptor,
                          serializer: JsonSerializer = JacksonSerializer())
: JsonSerializerDecorator(serializer) {

  override fun <T : Any> toByteArray(graph: T?): ByteArray {
    return encryptor.encrypt(super.toByteArray(graph))
  }

  override fun <T : Any> fromByteArray(jsonBytes: ByteArray?, clazz: Class<T>): T? {
    return super.fromByteArray(encryptor.decrypt(jsonBytes), clazz)
  }
}