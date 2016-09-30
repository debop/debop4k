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

package debop4k.core.java8.patterns.command;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class MockEditor implements Editor {

  private final List<String> actions = FastList.newList();

  @Override
  public void save() {
    actions.add("save");
  }

  @Override
  public void open() {
    actions.add("open");
  }

  @Override
  public void close() {
    actions.add("close");
  }

  public void check() {
    assertThat(actions.get(0)).isEqualTo("open");
    assertThat(actions.get(1)).isEqualTo("save");
    assertThat(actions.get(2)).isEqualTo("close");
  }
}
