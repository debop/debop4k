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

import debop4k.core.utils.Objects;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;


/**
 * @author sunghyouk.bae@gmail.com
 */
public abstract class BaseCompositeUserType implements CompositeUserType {

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    return Objects.hashCode(x);
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
    return (Serializable) deepCopy(value);
  }

  @Override
  public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
    return deepCopy(cached);
  }

  @Override
  public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
    return deepCopy(original);
  }

}
