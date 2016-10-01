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

package debop4k.data.mybatis.typehandlers;/*
 * Copyright 2015-2020 KESTI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// NOTE: MyBatis 용 Custom TypeHandler 는 사용 포기 - 너무 복잡하다.
//package debop4k.data.mybatis.typehandlers;
//
//import debop4k.data.mybatis.AbstractMyBatisTest;
//import debop4k.data.mybatis.typehandlers.mappers.UUIDBeanMapper;
//import debop4k.data.mybatis.typehandlers.models.UUIDBean;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.mybatis.spring.SqlSessionTemplate;
//
//import javax.inject.Inject;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Slf4j
//public class UUIDTypeHandlerTest extends AbstractMyBatisTest {
//
//  @Inject SqlSessionTemplate template;
//  @Inject UUIDBeanMapper uuidBeanMapper;
//
//  @Test
//  public void testUUIDTypeHandlerByXml() {
//
//    UUID id = UUID.randomUUID();
//    UUIDBean obj = UUIDBean.of(id, "xml", "debop");
//
//    int rowCount = template.insert("debop4k.data.mybatis.typehandlers.insertUUIDBean", obj);
//    assertThat(rowCount).isEqualTo(1);
//
//    UUIDBean loaded = template.selectOne("debop4k.data.mybatis.typehandlers.selectUUIDBeanById", obj);
//    log.debug("loaded={}", loaded);
//    assertThat(loaded).isNotNull();
//
//    int deletedCount = template.delete("debop4k.data.mybatis.typehandlers.deleteUUIDBeanById", obj);
//    assertThat(deletedCount).isEqualTo(1);
//  }
//
//  @Test
//  public void testUUIDTypeHandlerByAnnotation() {
//    UUID id = UUID.randomUUID();
//    UUIDBean obj = UUIDBean.of(id, "annotation", "debop");
//
//    int rowCount = uuidBeanMapper.save(obj);
//    assertThat(rowCount).isEqualTo(1);
//
//    UUIDBean loaded = uuidBeanMapper.findById(obj.getTestId());
//    log.debug("loaded={}", loaded);
//    assertThat(loaded).isNotNull();
//
//    uuidBeanMapper.deleteById(loaded.getTestId());
//  }
//}
