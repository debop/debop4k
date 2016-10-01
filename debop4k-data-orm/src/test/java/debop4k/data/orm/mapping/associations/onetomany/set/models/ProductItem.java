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

package debop4k.data.orm.mapping.associations.onetomany.set.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "OneToMany_ProductItem")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class ProductItem extends IntEntity {

  public ProductItem(String name) {
    this.name = name;
  }

  private String name;
  private String description;
  private BigDecimal initialPrice;
  private BigDecimal reservePrice;

  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Temporal(TemporalType.DATE)
  private Date endDate;

  @Enumerated
  private ProductStatus status = ProductStatus.Active;

  @CollectionTable(name = "ProductImages", joinColumns = {@JoinColumn(name = "productItemId")})
  @ElementCollection(targetClass = ProductImage.class)
  @Cascade(CascadeType.ALL)
  private Set<ProductImage> images = new HashSet<ProductImage>();

  public void addImages(ProductImage... images) {
    for (ProductImage img : images) {
      this.images.add(img);
      img.setItem(this);
    }
  }

  public void removeImages(ProductImage... images) {
    for (ProductImage img : images) {
      this.images.remove(img);
      img.setItem(null);
    }
  }

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 6959915952029927722L;
}
