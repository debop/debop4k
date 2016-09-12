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

import de.ruedigermoeller.serialization.FSTConfiguration
import io.netty.buffer.ByteBufInputStream
import org.redisson.client.codec.Codec
import org.redisson.client.protocol.Decoder
import org.redisson.client.protocol.Encoder
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * FstJava6Codec
 * @author debop sunghyouk.bae@gmail.com
 */
open class FstJava6Codec
@JvmOverloads constructor(val config: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()) : Codec {

  private val encoder: Encoder = Encoder { input ->
    ByteArrayOutputStream().use { bos ->
      val oos = config.getObjectOutput(bos)
      oos.writeObject(input)
      oos.flush()
      return@Encoder bos.toByteArray()
    }
  }

  private val decoder: Decoder<Any> = Decoder<Any> { buf, state ->
    try {
      ByteBufInputStream(buf).use { bis ->
        val ois = config.getObjectInput(bis)
        return@Decoder ois.readObject()
      }
    } catch(e: Exception) {
      throw IOException(e)
    }
  }

  override fun getValueEncoder(): Encoder = encoder

  override fun getMapKeyEncoder(): Encoder = valueEncoder

  override fun getMapValueEncoder(): Encoder = valueEncoder

  override fun getValueDecoder(): Decoder<Any> = decoder

  override fun getMapKeyDecoder(): Decoder<Any> = valueDecoder

  override fun getMapValueDecoder(): Decoder<Any> = valueDecoder
}