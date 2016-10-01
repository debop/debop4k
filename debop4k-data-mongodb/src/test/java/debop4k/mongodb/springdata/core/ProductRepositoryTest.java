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

package debop4k.mongodb.springdata.core;

import debop4k.mongodb.springdata.SpringDataMongoConfigurationTest;
import debop4k.mongodb.springdata.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class ProductRepositoryTest extends SpringDataMongoConfigurationTest {

  @Before
  public void setup() {
    super.setup();
  }

  @Test
  public void saveProduct() {
    Product product = new Product("Camera bag", BigDecimal.valueOf(49.99));
    Product saved = productRepo.save(product);

    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isNotNull();
    assertThat(saved).isEqualTo(product);
  }

  @Test
  public void findByDescription() {
    Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC, "name");
    Page<Product> results = productRepo.findByDescriptionContaining("Apple", pageable);

    assertThat(results.getContent()).hasSize(1);
    assertThat(results.isFirst()).isTrue();
    assertThat(results.isLast()).isFalse();
    assertThat(results.hasNext()).isTrue();
  }

  @Test
  public void findByAttributes() {
    List<Product> products = productRepo.findByAttributes("attributes.connector", "plug");
    assertThat(products).hasSize(2);

    boolean containsDock = false;
    for (Product product : products) {
      if (product.getName().contains("Dock")) {
        containsDock = true;
        break;
      }
    }
    assertThat(containsDock).isTrue();
  }
}
