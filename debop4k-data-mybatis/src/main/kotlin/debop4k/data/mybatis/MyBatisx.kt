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

@file:JvmName("MyBatisx")

package debop4k.data.mybatis

import org.apache.ibatis.session.ExecutorType
import org.apache.ibatis.session.ExecutorType.SIMPLE
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.TransactionIsolationLevel

/**
 * MyBtis [Session] 을 엽니다
 */
@JvmOverloads
fun SqlSessionFactory.getSession(executorType: ExecutorType = SIMPLE,
                                 autoCommit: Boolean = false): SqlSession {
  return openSession(executorType, autoCommit)
}

/**
 * MyBtis [Session] 을 엽니다
 */
fun SqlSessionFactory.getSession(executorType: ExecutorType = SIMPLE,
                                 level: TransactionIsolationLevel? = null): SqlSession {
  return openSession(executorType, level)
}

