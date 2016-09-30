/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.core.java8.lombokproject;

/**
 * StringExtensions
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 29.
 */
public class StringExtensions {

  public static <T> T or(T obj, T ifNull) {
    return (obj != null) ? obj : ifNull;
  }

  public static String toTitleCase(String in) {
    if (in.isEmpty()) return in;
    return "" + Character.toTitleCase(in.charAt(0)) + in.substring(1).toLowerCase();
  }
}
