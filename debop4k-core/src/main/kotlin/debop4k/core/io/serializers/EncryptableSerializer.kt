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

import debop4k.core.cryptography.RC2
import debop4k.core.cryptography.SymmetricEncryptor
import debop4k.core.cryptography.TRIPLE_DES
import debop4k.core.cryptography.encryptors.RC2Encryptor

/**
 * 암호화를 병행하는 직렬화
 * @author sunghyouk.bae@gmail.com
 */
open class EncryptableSerializer
@JvmOverloads constructor(serializer: Serializer,
                          val encryptor: SymmetricEncryptor = RC2Encryptor()) : SerializerDecorator(serializer) {

  open override fun serialize(graph: Any?): ByteArray {
    return encryptor.encrypt(super.serialize(graph))
  }

  open override fun <T> deserialize(bytes: ByteArray?): T {
    return super.deserialize(encryptor.decrypt(bytes))
  }
}

class RC2BinarySerializer : EncryptableSerializer(Serializers.BINARY, RC2)
class RC2FstSerializer : EncryptableSerializer(Serializers.FST, RC2)

class TripleDESBinarySerializer : EncryptableSerializer(Serializers.BINARY, TRIPLE_DES)
class TripleDESFstSerializer : EncryptableSerializer(Serializers.FST, TRIPLE_DES)