/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.spring.jdbc.core

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

/**
 * @author sunghyouk.bae@gmail.com
 */
class PreparedStatementArgumentSetter(val preparedStatement: PreparedStatement)
: PreparedStatement by preparedStatement {

  val array: DefaultArgumentSetter<Array>
    get() = DefaultArgumentSetter { i, v -> setArray(i, v) }

  val asciiStream: ArgumentWithLengthSetter<InputStream>
    get() = ArgumentWithLengthSetter({ i, v -> setAsciiStream(i, v) },
                                     { i, v, l -> setAsciiStream(i, v, l) },
                                     { i, v, l -> setAsciiStream(i, v, l) })

  val bigDecimal: DefaultArgumentSetter<BigDecimal>
    get() = DefaultArgumentSetter { i, v -> setBigDecimal(i, v) }

  val binaryStream: ArgumentWithLengthSetter<InputStream>
    get() = ArgumentWithLengthSetter({ i, v -> setBinaryStream(i, v) },
                                     { i, v, l -> setBinaryStream(i, v, l) },
                                     { i, v, l -> setBinaryStream(i, v, l) })

  val blob: BlobArgumentSetter
    get() = BlobArgumentSetter({ i, v -> setBlob(i, v) },
                               { i, v -> setBlob(i, v) },
                               { i, v, l -> setBlob(i, v, l) })

  val boolean: DefaultArgumentSetter<Boolean>
    get() = DefaultArgumentSetter { i, v -> setBoolean(i, v) }

  val byte: DefaultArgumentSetter<Byte>
    get() = DefaultArgumentSetter { i, v -> setByte(i, v) }

  val bytes: DefaultArgumentSetter<ByteArray>
    get() = DefaultArgumentSetter { i, v -> setBytes(i, v) }

  val characterStream: ArgumentWithLengthSetter<Reader>
    get() = ArgumentWithLengthSetter({ i, v -> setCharacterStream(i, v) },
                                     { i, v, l -> setCharacterStream(i, v, l) },
                                     { i, v, l -> setCharacterStream(i, v, l) })

  val clob: ClobArgumentSetter
    get() = ClobArgumentSetter({ i, v -> setClob(i, v) },
                               { i, v -> setClob(i, v) },
                               { i, v, l -> setClob(i, v, l) })

  val date: CombinedArgumentSetter<Date, Calendar>
    get() = CombinedArgumentSetter({ i, d -> setDate(i, d) },
                                   { i, d, c -> setDate(i, d, c) })

  val double: DefaultArgumentSetter<Double>
    get() = DefaultArgumentSetter { i, v -> setDouble(i, v) }

  val float: DefaultArgumentSetter<Float>
    get() = DefaultArgumentSetter { i, v -> setFloat(i, v) }

  val int: DefaultArgumentSetter<Int>
    get() = DefaultArgumentSetter { i, v -> setInt(i, v) }

  val long: DefaultArgumentSetter<Long>
    get() = DefaultArgumentSetter { i, v -> setLong(i, v) }

  val nCharacterStream: CombinedArgumentSetter<Reader, Long>
    get() = CombinedArgumentSetter({ i, r -> setNCharacterStream(i, r) },
                                   { i, r, l -> setNCharacterStream(i, r, l) })

  val nClob: NClobArgumentSetter
    get() = NClobArgumentSetter({ i, nc -> setNClob(i, nc) },
                                { i, r -> setNClob(i, r) },
                                { i, r, l -> setNClob(i, r, l) })

  val nString: DefaultArgumentSetter<String>
    get() = DefaultArgumentSetter { i, v -> setNString(i, v) }

  val `null`: CombinedArgumentSetter<Int, String>
    get() = CombinedArgumentSetter({ i, s -> setNull(i, s) },
                                   { i, s, t -> setNull(i, s, t) })

  val `object`: ObjectArgumentSetter
    get() = ObjectArgumentSetter({ i, x -> setObject(i, x) },
                                 { i, x, t -> setObject(i, x, t) },
                                 { i, x, t, s -> setObject(i, x, t, s) })

  val ref: DefaultArgumentSetter<Ref>
    get() = DefaultArgumentSetter { i, v -> setRef(i, v) }

  val rowId: DefaultArgumentSetter<RowId>
    get() = DefaultArgumentSetter { i, v -> setRowId(i, v) }


  val sqlXml: DefaultArgumentSetter<SQLXML>
    get() = DefaultArgumentSetter { i, v -> setSQLXML(i, v) }

  val string: DefaultArgumentSetter<String>
    get() = DefaultArgumentSetter { i, v -> setString(i, v) }

  val time: CombinedArgumentSetter<Time, Calendar>
    get() = CombinedArgumentSetter({ i, t -> setTime(i, t) },
                                   { i, t, c -> setTime(i, t, c) })

  val timestamp: CombinedArgumentSetter<Timestamp, Calendar>
    get() = CombinedArgumentSetter({ i, t -> setTimestamp(i, t) },
                                   { i, t, c -> setTimestamp(i, t, c) })

  val url: DefaultArgumentSetter<URL>
    get() = DefaultArgumentSetter { i, v -> setURL(i, v) }


}