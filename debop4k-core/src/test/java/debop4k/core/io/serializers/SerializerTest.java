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

package debop4k.core.io.serializers;

import debop4k.core.YearWeek;
import debop4k.core.collections.Intervals;
import debop4k.core.compress.GZipCompressor;
import debop4k.core.compress.SnappyCompressor;
import debop4k.core.cryptography.encryptors.RC2Encryptor;
import debop4k.core.cryptography.encryptors.TripleDESEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SerializerTest {

  List<? extends Serializer> serializers =
      FastList.newListWith(BinarySerializer.of(),
                           FstSerializer.of(),
                           new CompressableSerializer(BinarySerializer.of(), new GZipCompressor()),
                           new CompressableSerializer(BinarySerializer.of(), new SnappyCompressor()),
                           new CompressableSerializer(FstSerializer.of(), new GZipCompressor()),
                           new CompressableSerializer(FstSerializer.of(), new SnappyCompressor()),
                           new EncryptableSerializer(BinarySerializer.of(), new RC2Encryptor()),
                           new EncryptableSerializer(BinarySerializer.of(), new TripleDESEncryptor()),
                           new EncryptableSerializer(FstSerializer.of(), new RC2Encryptor()),
                           new EncryptableSerializer(FstSerializer.of(), new TripleDESEncryptor())
                          );

  @Test
  public void serializeValueObject() {
    YearWeek yearWeek = new YearWeek(2015, 8);
    for (Serializer serializer : serializers) {
      YearWeek copied = serializer.deserialize(serializer.serialize(yearWeek));
      assertThat(copied).isEqualTo(yearWeek);
    }
  }

  @Test
  public void serializePrimitiveArray() {
    int[] array = Intervals.Ints.range(0, 1000).toArray();
    for (Serializer serializer : serializers) {
      int[] copied = serializer.deserialize(serializer.serialize(array));
      assertThat(copied).isEqualTo(array);
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void serializeCollection() {
    List<YearWeek> collection = FastList.newList();
    for (int i = 0; i < 1000; i++) {
      collection.add(new YearWeek(2015, 8));
    }
    for (Serializer serializer : serializers) {
      List<YearWeek> copied = serializer.deserialize(serializer.serialize(collection));
      assertThat(copied).isEqualTo(collection);
    }
  }

  @Test
  public void copyReferenceObject() {
    YearWeek yearWeek = new YearWeek(2015, 8);

    for (Serializer serializer : serializers) {
      YearWeek copied = serializer.copy(yearWeek);
      assertThat(copied).isEqualTo(yearWeek);
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void copyCollection() {
    List<YearWeek> collection = FastList.newList();
    for (int i = 0; i < 1000; i++) {
      collection.add(new YearWeek(2015, 8));
    }

    for (Serializer serializer : serializers) {
      log.debug("serializer={}", serializer);
      List<YearWeek> copied = serializer.copy(collection);
      assertThat(copied).isEqualTo(collection);
    }
  }
}
