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

package debop4k.data.orm.mapping.associations.onetomany.set;

import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.data.orm.mapping.associations.onetomany.set.models.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {OneToManySetConfiguration.class})
public class OneToManySetTest extends AbstractMappingTest {

  @Test
  public void bidding() {

    BiddingItem item = new BiddingItem("TV");
    Bid bid1 = new Bid(BigDecimal.valueOf(100.0));
    Bid bid2 = new Bid(BigDecimal.valueOf(200.0));

    item.addBids(bid1, bid2);
    assertThat(item.getBids()).hasSize(2);

    em.persist(item);
    em.flush();
    em.clear();

    BiddingItem loaded = em.find(BiddingItem.class, item.getId());
    assertThat(loaded).isNotNull().isEqualTo(item);
    assertThat(loaded.getBids()).hasSize(2).containsOnly(bid1, bid2);

    Bid removed = loaded.getBids().iterator().next();
    loaded.getBids().remove(removed);

    em.remove(removed);
    em.persist(loaded);
    em.flush();
    em.clear();

    loaded = em.find(BiddingItem.class, item.getId());
    assertThat(loaded).isNotNull().isEqualTo(item);
    assertThat(loaded.getBids()).hasSize(1);

    em.remove(loaded);
    em.flush();

    assertThat(em.find(BiddingItem.class, item.getId())).isNull();
  }

  @Test
  public void product() {
    ProductItem item = new ProductItem("TV");

    ProductImage image1 = new ProductImage("forward");
    ProductImage image2 = new ProductImage("backward");

    item.addImages(image1, image2);
    item.setStatus(ProductStatus.Active);

    em.persist(item);
    em.flush();
    em.clear();

    ProductItem loaded = em.find(ProductItem.class, item.getId());
    assertThat(loaded).isNotNull().isEqualTo(item);
    assertThat(loaded.getImages()).hasSize(2).containsOnly(image1, image2);

    loaded.getImages().clear();
    em.persist(loaded);
    em.flush();
    em.clear();

    loaded = em.find(ProductItem.class, item.getId());
    assertThat(loaded).isNotNull().isEqualTo(item);
    assertThat(loaded.getImages()).hasSize(0);

    em.remove(loaded);
    em.flush();

    assertThat(em.find(ProductItem.class, item.getId())).isNull();
  }
}
