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

package debop4k.mongodb.models;

import lombok.Value;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.io.Serializable;
import java.util.List;

@Value
public class Address implements Serializable {

  private final String street;
  private final String phone;
  private final List<String> properties = FastList.newList();

  private static final long serialVersionUID = 8742633801370918778L;
}
