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

package debop4k.data.orm.mapping.tree;

import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.AbstractTreeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class TreeNode extends AbstractTreeEntity<TreeNode, Integer> {

  public TreeNode(String title) {
    this(title, null);
  }

  public TreeNode(String title, TreeNode parent) {
    this.title = title;

    if (parent != null) {
      parent.addChildren(this);
    }
  }

  private String title;
  private String data;
  private String description;

  @Override
  public int hashCode() {
    return Hashx.compute(super.hashCode(), title);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("title", title)
                .add("data", data)
                .add("description", description);
  }

  private static final long serialVersionUID = -5380603939596222494L;
}
