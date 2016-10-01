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

package debop4k.mongodb.music.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class Stars {
  private final int value;

  public static Stars ZERO = new Stars(0);
  public static Stars ONE = new Stars(1);
  public static Stars TWO = new Stars(2);
  public static Stars THREE = new Stars(3);
  public static Stars FOUR = new Stars(4);
  public static Stars FIVE = new Stars(5);

  public static ImmutableSet<Stars> ALL = Sets.immutable.of(ZERO, ONE, TWO, THREE, FOUR, FIVE);
}
