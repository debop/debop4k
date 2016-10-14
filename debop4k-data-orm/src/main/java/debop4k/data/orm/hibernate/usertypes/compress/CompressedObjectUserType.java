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

package debop4k.data.orm.hibernate.usertypes.compress;

import debop4k.core.collections.Arrayx;
import debop4k.core.io.serializers.Serializers;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.BinaryType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 객체를 압축하여 바이트 배열로 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 26.
 */
public abstract class CompressedObjectUserType extends CompressedUserType {

  public byte[] compress(Object value) {
    if (value == null)
      return null;

    return compressor().compress(Serializers.FST.serialize(value));
  }

  public Object decompress(byte[] compressedByte) {
    if (Arrayx.isNullOrEmpty(compressedByte))
      return null;

    return Serializers.FST.deserialize(compressor().decompress(compressedByte));
  }

  @Override
  public Class returnedClass() {
    return Serializable.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SharedSessionContractImplementor session,
                            Object owner) throws HibernateException, SQLException {
    byte[] compressedBytes = BinaryType.INSTANCE.nullSafeGet(rs, names[0], session);
    return decompress(compressedBytes);
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SharedSessionContractImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      BinaryType.INSTANCE.nullSafeSet(st, null, index, session);
    } else {
      byte[] compressedBytes = compress(value);
      BinaryType.INSTANCE.nullSafeSet(st, compressedBytes, index, session);
    }
  }
}
