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

package debop4k.spring.jdbc.core

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array

/**
 * @author sunghyouk.bae@gmail.com
 */
open class ResultSetGetFieldTokens(resultSet: ResultSet) : ResultSet by resultSet {

  val array: GetFieldsToken<Array?>
    get() = GetFieldsToken({ columnName -> getArray(columnName) },
                           { columnIndex -> getArray(columnIndex) })

  val asciiStream: GetFieldsToken<InputStream?>
    get() = GetFieldsToken({ columnName -> getAsciiStream(columnName) },
                           { columnIndex -> getAsciiStream(columnIndex) })

  val bigDecimal: GetFieldsToken<BigDecimal?>
    get() = GetFieldsToken({ columnName -> getBigDecimal(columnName) },
                           { columnIndex -> getBigDecimal(columnIndex) })

  val binaryStream: GetFieldsToken<InputStream?>
    get() = GetFieldsToken({ columnName -> getBinaryStream(columnName) },
                           { columnIndex -> getBinaryStream(columnIndex) })

  val blob: GetFieldsToken<Blob?>
    get() = GetFieldsToken({ columnName -> getBlob(columnName) },
                           { columnIndex -> getBlob(columnIndex) })

  val boolean: GetFieldsToken<Boolean?>
    get() = GetFieldsToken({ columnName -> getBoolean(columnName) },
                           { columnIndex -> getBoolean(columnIndex) })

  val byte: GetFieldsToken<Byte?>
    get() = GetFieldsToken({ columnName -> getByte(columnName) },
                           { columnIndex -> getByte(columnIndex) })

  val bytes: GetFieldsToken<ByteArray?>
    get() = GetFieldsToken({ columnName -> getBytes(columnName) },
                           { columnIndex -> getBytes(columnIndex) })

  val characterStream: GetFieldsToken<Reader?>
    get() = GetFieldsToken({ columnName -> getCharacterStream(columnName) },
                           { columnIndex -> getCharacterStream(columnIndex) })

  val clob: GetFieldsToken<Clob?>
    get() = GetFieldsToken({ columnName -> getClob(columnName) },
                           { columnIndex -> getClob(columnIndex) })

  val date: GetFieldsToken<Date>
    get() = GetFieldsToken({ columnName -> getDate(columnName) },
                           { columnIndex -> getDate(columnIndex) })

  val double: GetFieldsToken<Double?>
    get() = GetFieldsToken({ columnName -> getDouble(columnName) },
                           { columnIndex -> getDouble(columnIndex) })

  val float: GetFieldsToken<Float?>
    get() = GetFieldsToken({ columnName -> getFloat(columnName) },
                           { columnIndex -> getFloat(columnIndex) })

  val int: GetFieldsToken<Int?>
    get() = GetFieldsToken({ columnName -> getInt(columnName) },
                           { columnIndex -> getInt(columnIndex) })

  val long: GetFieldsToken<Long?>
    get() = GetFieldsToken({ columnName -> getLong(columnName) },
                           { columnIndex -> getLong(columnIndex) })

  val nCharacterStream: GetFieldsToken<Reader?>
    get() = GetFieldsToken({ columnName -> getNCharacterStream(columnName) },
                           { columnIndex -> getNCharacterStream(columnIndex) })

  val nClob: GetFieldsToken<NClob?>
    get() = GetFieldsToken({ columnName -> getNClob(columnName) },
                           { columnIndex -> getNClob(columnIndex) })

  val nString: GetFieldsToken<String?>
    get() = GetFieldsToken({ columnName -> getNString(columnName) },
                           { columnIndex -> getString(columnIndex) })

  val ref: GetFieldsToken<Ref?>
    get() = GetFieldsToken({ columnName -> getRef(columnName) },
                           { columnIndex -> getRef(columnIndex) })

  val rowId: GetFieldsToken<RowId?>
    get() = GetFieldsToken({ columnName -> getRowId(columnName) },
                           { columnIndex -> getRowId(columnIndex) })

  val short: GetFieldsToken<Short?>
    get() = GetFieldsToken({ columnName -> getShort(columnName) },
                           { columnIndex -> getShort(columnIndex) })

  val SQLXML: GetFieldsToken<SQLXML?>
    get() = GetFieldsToken({ columnName -> getSQLXML(columnName) },
                           { columnIndex -> getSQLXML(columnIndex) })

  val string: GetFieldsToken<String?>
    get() = GetFieldsToken({ columnName -> getString(columnName) },
                           { columnIndex -> getString(columnIndex) })

  val time: GetFieldsToken<Time?>
    get() = GetFieldsToken({ columnName -> getTime(columnName) },
                           { columnIndex -> getTime(columnIndex) })

  val timestamp: GetFieldsToken<Timestamp?>
    get() = GetFieldsToken({ columnName -> getTimestamp(columnName) },
                           { columnIndex -> getTimestamp(columnIndex) })

  val URL: GetFieldsToken<URL?>
    get() = GetFieldsToken({ columnName -> getURL(columnName) },
                           { columnIndex -> getURL(columnIndex) })

  // TODO: 향후 PostGIS 용 geometry 같은 것도 추가한다.

}