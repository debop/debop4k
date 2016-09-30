/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

@file:JvmName("StringEncoders")

package debop4k.core.utils.codecs

import debop4k.core.compress.DEFLATER
import debop4k.core.compress.GZIP
import debop4k.core.compress.SNAPPY

/** Base64 String Encoder */
val base64 by lazy { Base64StringEncoder() }

/** Hex Decimal String Encoder */
val hexDecimal by lazy { HexStringEncoder() }

/** GZip Base64 String Encoder */
val gzipBase64 by lazy { CompressableStringEncoder(base64, GZIP) }

/** GZip Hex Decimal String Encoder */
val gzipHexDecimal by lazy { CompressableStringEncoder(hexDecimal, GZIP) }

/** Deflate Base64 String Encoder */
val deflaterBase64 by lazy { CompressableStringEncoder(base64, DEFLATER) }

/** Deflate Hex Decimal String Encoder */
val deflaterHexDecimal by lazy { CompressableStringEncoder(hexDecimal, DEFLATER) }

/** Snappy Base64 String Encoder */
val snappyBase64 by lazy { CompressableStringEncoder(base64, SNAPPY) }

/** Snappy Hex Decimal String Encoder */
val snappyHexDecimal by lazy { CompressableStringEncoder(hexDecimal, SNAPPY) }