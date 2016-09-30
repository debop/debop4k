/*
 *  Copyright (c) 2016. KESTI co, ltd
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

package debop4k.core;

import java.nio.charset.Charset;

/**
 * Charsets
 *
 * @author sunghyouk.bae@gmail.com
 * @see kotlin.text.Charsets
 */
public final class Charsets {

  private Charsets() {}

  /**
   * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
   */
  public static final Charset US_ASCII = Charset.forName("US-ASCII");

  /**
   * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
   */
  public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

  /**
   * Eight-bit UCS Transformation Format
   */
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  /**
   * Sixteen-bit UCS Transformation Format, big-endian byte order
   */
  public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

  /**
   * Sixteen-bit UCS Transformation Format, little-endian byte order
   */
  public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

  /**
   * Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
   */
  public static final Charset UTF_16 = Charset.forName("UTF-16");
}
