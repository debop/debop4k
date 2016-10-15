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

package debop4k.data.orm.springdata.jpa21;//package debop4k.data.orm.springdata.jpa21;
//
//import lombok.extern.slf4j.Loggingx;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.ParameterMode;
//import javax.persistence.PersistenceContext;
//import javax.persistence.StoredProcedureQuery;
//
//import static org.assertj.core.api.Assertionx.assertThat;
//
//@Loggingx
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { StoredProcedureConfiguration.class })
//@Transactional
//public class StoredProcedureTest {
//
//  @Inject UserRepository repository;
//
//  //@Test
//  @Ignore("Spring Data JPA 에서 아직 버그가 있는 듯 하네요.")
//  public void entityAnnotatedCustomNamedProcedurePlus1() {
//    assertThat(repository.plus1BackedByOtherNamedStoredProcedure(1)).isEqualTo(2);
//  }
//
//  @Test
//  public void invokeDerivedStoredProcedure() {
//    assertThat(repository.plus1inout(1)).isEqualTo(2);
//  }
//
//  // Manual 작업으로도 가능
//  //
//  @PersistenceContext EntityManager em;
//
//  @Test
//  public void plainJpa21() {
//    StoredProcedureQuery proc = em.createStoredProcedureQuery("plus1inout");
//    proc.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//    proc.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
//
//    proc.setParameter(1, 1);
//    proc.execute();
//
//    assertThat(proc.getOutputParameterValue(2)).isEqualTo(2);
//  }
//
//  @Ignore("Spring Data JPA 에서 아직 버그가 있는 듯 하네요.")
//  public void plainJpa21ByNamedStoredProcedure() {
//    StoredProcedureQuery proc = em.createNamedStoredProcedureQuery("User.plus1");
//
//    proc.setParameter("arg", 1);
//    proc.execute();
//
//    assertThat(proc.getOutputParameterValue("res")).isEqualTo(2);
//  }
//}
