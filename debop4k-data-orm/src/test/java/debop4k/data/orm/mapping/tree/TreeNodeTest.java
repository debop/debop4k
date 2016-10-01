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

import debop4k.data.orm.jpa.dao.JpaQuerydslDao;
import debop4k.data.orm.mapping.AbstractMappingTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TreeConfiguration.class})
@SuppressWarnings("unchecked")
public class TreeNodeTest extends AbstractMappingTest {

  @Inject JpaQuerydslDao dao;

  @Test
  public void checkConfiguration() {
    assertThat(dao).isNotNull();
  }

  @Test
  public void buildTree() {
    TreeNode root = new TreeNode("root");
    TreeNode child1 = new TreeNode("child1", root);
    TreeNode child2 = new TreeNode("child2", root);

    TreeNode child11 = new TreeNode("child11", child1);
    TreeNode child12 = new TreeNode("child12", child1);

    em.persist(root);
    em.flush();
    em.clear();

    TreeNode node = em.find(TreeNode.class, child1.getId());
    assertThat(node).isNotNull().isEqualTo(child1);
    assertThat(node.getChildren()).hasSize(2).containsOnly(child11, child12);
    assertThat(node.getParent()).isEqualTo(root);

    // JPQL 로 root ( getParent is null) 을 조회합니다.
    List<TreeNode> roots = (List<TreeNode>) em.createQuery("select n from TreeNode n where n.parent is null")
                                              .getResultList();

    Assertions.assertThat(roots).hasSize(1);
    assertThat(roots.get(0)).isEqualTo(root);

    em.remove(roots.get(0));
    em.flush();
  }


}
