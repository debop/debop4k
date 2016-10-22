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

/**
 * 직렬화 인스턴스를 제공합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
object Serializers {

  /** Binary Serializer */
  @JvmField val BINARY = BinarySerializer()

  /** FST Serializer for Java 6 */
  @JvmField val FST = FstSerializer()

  /** Deflater + Binary Serializer */
  @JvmField val DEFLATER_BINARY = DeflaterBinarySerializer()

  /** Deflater + FST Serializer */
  @JvmField val DEFLATER_FST = DeflaterFstSerializer()

  /** GZip + Binary Serializer */
  @JvmField val GZIP_BINARY = GZipBinarySerializer()

  /** GZip + FST Serializer */
  @JvmField val GZIP_FST = GZipFstSerializer()

  /** LZ4 + Binary Serializer */
  @JvmField val LZ4_BINARY = LZ4BinarySerializer()

  /** LZ4 + FST Serializer */
  @JvmField val LZ4_FST = LZ4FstSerializer()

  /** SNAPPY + Binary Serializer */
  @JvmField val SNAPPY_BINARY = SnappyBinarySerializer()

  /** SNAPPY + FST Serializer */
  @JvmField val SNAPPY_FST_JAVA6 = SnappyFstSerializer()

}