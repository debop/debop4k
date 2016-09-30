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

package debop4k.core.java8.collections;


public class StringCombiner {

  private final String delim;
  private final String prefix;
  private final String suffix;
  private final StringBuilder builder;

  public StringCombiner(String delim, String prefix, String suffix) {
    this.delim = delim;
    this.prefix = prefix;
    this.suffix = suffix;
    this.builder = new StringBuilder();
  }

  private boolean isAtStart() {
    return builder.length() == 0;
  }

  public StringCombiner add(String element) {
    if (isAtStart())
      builder.append(prefix);
    else
      builder.append(delim);

    builder.append(element);
    return this;
  }

  public StringCombiner merge(StringCombiner other) {
    if (other.builder.length() > 0) {
      if (isAtStart()) {
        builder.append(prefix);
      } else {
        builder.append(delim);
      }
      builder.append(other.builder, prefix.length(), other.builder.length());
    }
    return this;
  }

  @Override
  public String toString() {
    if (isAtStart()) {
      builder.append(prefix);
    }
    builder.append(suffix);
    return builder.toString();
  }
}
