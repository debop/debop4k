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

package debop4k.data.orm.hibernate.usertypes;

import debop4k.core.utils.KoreanString;
import org.eclipse.collections.impl.list.mutable.primitive.CharArrayList;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 한글 문자열일 경우 문자의 초성만을 추출하여 따로 저장하도록 합니다. (초성으로 검색할 때 좋습니다)
 * <p>
 * <pre><code>
 *  `@Column`(columns={ @Column(name="name"), @Column(name="nameChosung") })
 *  `@org.hibernate.annotation.Type`(`type`="debop4k.data.orm.hibernate.usertypes.KoreanChosungUserType")
 *   String name;
 * </code></pre>
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 26.
 */
public class KoreanChosungUserType extends BaseUserType {

  @Override
  public int[] sqlTypes() {
    return new int[]{StandardBasicTypes.STRING.sqlType(), StandardBasicTypes.STRING.sqlType()};
  }

  @Override
  public Class returnedClass() {
    return String.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SharedSessionContractImplementor session,
                            Object owner) throws HibernateException, SQLException {
    return StandardBasicTypes.STRING.nullSafeGet(rs, names[0], session, owner);
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SharedSessionContractImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      StandardBasicTypes.STRING.nullSafeSet(st, null, index, session);
      StandardBasicTypes.STRING.nullSafeSet(st, null, index + 1, session);
    } else {
      String text = (String) value;
      String chosung = CharArrayList.newListWith(KoreanString.getChosung(text)).makeString("");
      StandardBasicTypes.STRING.nullSafeSet(st, text, index, session);
      StandardBasicTypes.STRING.nullSafeSet(st, chosung, index + 1, session);
    }
  }
}
