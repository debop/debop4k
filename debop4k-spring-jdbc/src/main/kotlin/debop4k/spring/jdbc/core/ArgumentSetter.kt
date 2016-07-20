/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.spring.jdbc.core

import java.io.InputStream
import java.io.Reader
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob

interface ArgumentSetter<T> {

  val setter: (Int, T) -> Unit

  operator fun set(index: Int, value: T) {
    setter(index, value)
  }
}

interface ArgumentSetter2<T, A> {

  val setter2: (Int, T, A) -> Unit

  operator fun set(index: Int, arg: A, value: T) {
    setter2(index, value, arg)
  }
}

open class ArgumentWithLengthSetter<T>(override val setter: (Int, T) -> Unit,
                                       override val setter2: (Int, T, Int) -> Unit,
                                       val setterWithLong: (Int, T, Long) -> Unit) :
    ArgumentSetter<T>, ArgumentSetter2<T, Int> {

  operator fun set(index: Int, length: Long, value: T) {
    setterWithLong(index, value, length)
  }
}

abstract class AbstractBlobArgumentSetter<R>(override val setter: (Int, R) -> Unit,
                                             override val setter2: (Int, R, Long) -> Unit) :
    ArgumentSetter<R>, ArgumentSetter2<R, Long>

class BlobArgumentSetter(val blobSetter: (Int, Blob) -> Unit,
                         override val setter: (Int, InputStream) -> Unit,
                         override val setter2: (Int, InputStream, Long) -> Unit) :
    AbstractBlobArgumentSetter<InputStream>(setter, setter2) {

  operator fun set(index: Int, blob: Blob) {
    blobSetter(index, blob)
  }
}

class ClobArgumentSetter(val blobSetter: (Int, Clob) -> Unit,
                         override val setter: (Int, Reader) -> Unit,
                         override val setter2: (Int, Reader, Long) -> Unit) :
    AbstractBlobArgumentSetter<Reader>(setter, setter2) {

  operator fun set(index: Int, clob: Clob) {
    blobSetter(index, clob)
  }
}

open class NClobArgumentSetter(val blobSetter: (Int, NClob) -> Unit,
                               override val setter: (Int, Reader) -> Unit,
                               override val setter2: (Int, Reader, Long) -> Unit) :
    AbstractBlobArgumentSetter<Reader>(setter, setter2) {

  operator fun set(index: Int, nclob: NClob) {
    blobSetter(index, nclob)
  }
}

open class CombinedArgumentSetter<T, A>(override val setter: (Int, T) -> Unit,
                                        override val setter2: (Int, T, A) -> Unit) :
    ArgumentSetter<T>, ArgumentSetter2<T, A>

open class ObjectArgumentSetter(setter: (Int, Any) -> Unit,
                                setter2: (Int, Any, Int) -> Unit,
                                val setter4: (Int, Any, Int, Int) -> Unit) :
    CombinedArgumentSetter<Any, Int>(setter, setter2) {

  operator fun set(index: Int, targetSqlType: Int, scaleOrLength: Int, value: Any)
      = setter4(index, value, targetSqlType, scaleOrLength)
}

open class DefaultArgumentSetter<T>(override val setter: (Int, T) -> Unit) : ArgumentSetter<T>