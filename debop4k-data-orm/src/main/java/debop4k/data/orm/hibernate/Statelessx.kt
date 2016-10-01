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

package debop4k.data.orm.hibernate

import debop4k.core.loggerOf
import org.hibernate.SessionFactory
import org.hibernate.StatelessSession
import javax.persistence.EntityManager

private val log = loggerOf("Statelessx")


fun <T> SessionFactory.withStateless(func: (StatelessSession) -> T?): T? {
  this.openStatelessSession().use { stateless ->
    val tx = stateless.beginTransaction()
    try {
      val result = func.invoke(stateless)
      tx.commit()
      return result
    } catch(e: Exception) {
      try {
        tx.rollback()
      } catch(e: Throwable) {
      }
      return null
    }
  }
}

fun <T> SessionFactory.withStatelessReadOnly(func: (StatelessSession) -> T?): T? {
  this.openStatelessSession().use { stateless ->
    val conn = stateless.connection()
    conn.isReadOnly = true
    conn.autoCommit = false
    val tx = stateless.beginTransaction()
    try {
      val result = func.invoke(stateless)
      tx.commit()
      return result
    } catch(e: Exception) {
      tx.rollback()
      throw RuntimeException(e)
    }
  }
}

fun <T> EntityManager.withStateless(func: (StatelessSession) -> T?): T? {
  return sessionFactory.withStateless(func)
}

fun <T> EntityManager.withStatelessReadOnly(func: (StatelessSession) -> T?): T? {
  return sessionFactory.withStatelessReadOnly(func)
}


