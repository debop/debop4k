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

package debop4k.core.io.serializers

import debop4k.core.compress.*

/**
 * 압축을 수행해주는 Serializer 클래스입니다.
 * @author sunghyouk.bae@gmail.com
 */
open class CompressableSerializer
@JvmOverloads constructor(serializer: Serializer,
                          val compressor: Compressor = SnappyCompressor()) : SerializerDecorator(serializer) {

  open override fun serialize(graph: Any?): ByteArray {
    return compressor.compress(super.serialize(graph))
  }

  open override fun <T> deserialize(bytes: ByteArray?): T? {
    return super.deserialize(compressor.decompress(bytes))
  }
}

class DeflaterBinarySerializer : CompressableSerializer(Serializers.BINARY, DEFLATER)
class DeflaterFstSerializer : CompressableSerializer(Serializers.FST, DEFLATER)

class GZipBinarySerializer : CompressableSerializer(Serializers.BINARY, GZIP)
class GZipFstSerializer : CompressableSerializer(Serializers.FST, GZIP)

class LZ4BinarySerializer : CompressableSerializer(Serializers.BINARY, LZ4)
class LZ4FstSerializer : CompressableSerializer(Serializers.FST, LZ4)

class SnappyBinarySerializer() : CompressableSerializer(Serializers.BINARY, SNAPPY)
class SnappyFstSerializer() : CompressableSerializer(Serializers.FST, SNAPPY)