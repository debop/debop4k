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

package debop4k.data.orm.mapping.usertypes;

import debop4k.core.collections.Arrayx;
import debop4k.core.kodatimes.KodaTimes;
import debop4k.core.utils.Stringx;
import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.utils.Durations;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Query;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserTypeConfiguration.class})
public class UserTypeTest extends AbstractMappingTest {

  static String PLAIN_TEXT = "동해물과 백두산이 마르고 닳도록, !@#$%^123456 Hello World!";

  @Test
  public void compressedEntity() {
    CompressedEntity entity = new CompressedEntity();
    entity.setStringData(Stringx.replicate(PLAIN_TEXT, 1000));
    entity.setBinaryData(Arrayx.getRandomBytes(81920));

    em.persist(entity);
    em.flush();
    em.clear();

    CompressedEntity loaded = em.find(CompressedEntity.class, entity.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getStringData()).isEqualTo(entity.getStringData());
    assertThat(loaded.getBinaryData()).isEqualTo(entity.getBinaryData());

    em.remove(loaded);
    em.flush();
    em.clear();

    assertThat(em.find(CompressedEntity.class, entity.getId())).isNull();
  }

  @Test
  public void encryptedEntity() {
    String password = "고기사주세요";
    EncryptedEntity entity = new EncryptedEntity("debop", password);
    em.persist(entity);
    em.flush();
    em.clear();

    EncryptedEntity loaded = em.find(EncryptedEntity.class, entity.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getPassword()).isEqualTo(password);

    loaded.setUsername("debop68");
    em.persist(loaded);
    em.flush();
    em.clear();

    loaded = em.find(EncryptedEntity.class, entity.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getPassword()).isEqualTo(password);

    loaded = (EncryptedEntity) em.createQuery("select x from EncryptedEntity x where x.password = :password")
                                 .setParameter("password", password)
                                 .getSingleResult();

    assertThat(loaded).isNotNull();
    assertThat(loaded.getPassword()).isEqualTo(password);

    em.remove(loaded);
    em.flush();

    assertThat(em.find(EncryptedEntity.class, entity.getId())).isNull();
  }

  @Test
  public void hashedString() {
    String password = "rhrltkwntpdy";
    HashedEntity entity = new HashedEntity("debop", password);

    em.persist(entity);
    em.flush();
    em.clear();

    HashedEntity loaded = em.find(HashedEntity.class, entity.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getUsername()).isEqualTo(entity.getUsername());
    assertThat(loaded.getPassword()).isNotEqualTo(entity.getPassword());

    Query query = em.createQuery("select x from HashedEntity x where x.password = :password")
                    .setParameter("password", password);
    loaded = (HashedEntity) query.getSingleResult();
    assertThat(loaded).isNotNull().isEqualTo(entity);

    em.remove(loaded);
    em.flush();

    assertThat(em.find(HashedEntity.class, entity.getId())).isNull();
  }

  @Test
  public void koreanChusung() {
    KoreanChosungEntity entity = new KoreanChosungEntity("배성혁");
    entity.setDescription("한글과 한글의 초성만을 나타태는 컬럼에 값을 저장합니다.");
    em.persist(entity);
    em.flush();
    em.clear();

    KoreanChosungEntity loaded = em.find(KoreanChosungEntity.class, entity.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getName()).isEqualTo(entity.getName());

    em.remove(loaded);
    em.flush();

    assertThat(em.find(KoreanChosungEntity.class, entity.getId())).isNull();
  }

  @Test
  public void jodaDateTime() {
    JodaTimeEntity entity = new JodaTimeEntity();
    entity.setStart(KodaTimes.today());
    entity.setEnd(entity.getStart().plusDays(1));

    entity.setStartTZ(Times.now());
    entity.setEndTZ(entity.getStartTZ().plusDays(1));

    entity.setPeriod1(new TimeRange(entity.getStart(), entity.getEnd()));
    entity.setPeriod2(entity.getPeriod1().copy(Durations.Day));

    entity.setTimeAsString(entity.getStartTZ());

    em.persist(entity);
    em.flush();
    em.clear();

    JodaTimeEntity loaded = em.find(JodaTimeEntity.class, entity.getId());
    assertThat(loaded).isEqualTo(entity);
    assertThat(loaded.getStart()).isEqualTo(entity.getStart());
    assertThat(loaded.getEnd()).isEqualTo(entity.getEnd());

    // NOTE: MySQL은 milliseconds 를 저장하지 않고, H2 는 Milliseconds 까지 저장합니다.
    assertThat(loaded.getStartTZ()).isEqualTo(entity.getStartTZ());
    assertThat(loaded.getEndTZ()).isEqualTo(entity.getEndTZ());

    assertThat(loaded.getPeriod1()).isEqualTo(entity.getPeriod1());
    assertThat(loaded.getPeriod2()).isEqualTo(entity.getPeriod2());

    assertThat(loaded.getTimeAsString().getMillis()).isEqualTo(entity.getTimeAsString().getMillis());

    //
    // TimeZone 이 시스템 기본과 다른 경우
    //
    DateTime startTZ = KodaTimes.asLocal(DateTime.now(), DateTimeZone.forID("EST"));
    DateTime endTZ = startTZ.plusDays(1);

    entity = new JodaTimeEntity();
    entity.setStart(startTZ);
    entity.setEnd(endTZ);
    entity.setStartTZ(startTZ);
    entity.setEndTZ(endTZ);
    entity.setTimeAsString(startTZ);

    em.persist(entity);
    em.flush();
    em.clear();

    loaded = em.find(JodaTimeEntity.class, entity.getId());
    assertThat(loaded).isEqualTo(entity);

    // NOTE: Joda DateTime 비교는 compareTo 로 해야 한다.
    assertThat(loaded.getStart().getMillis()).isEqualTo(entity.getStart().getMillis());
    assertThat(loaded.getEnd().getMillis()).isEqualTo(entity.getEnd().getMillis());

    // NOTE: MySQL은 milliseconds 를 저장하지 않고, H2 는 Milliseconds 까지 저장합니다.
    assertThat(loaded.getStartTZ().getMillis()).isEqualTo(entity.getStartTZ().getMillis());
    assertThat(loaded.getEndTZ().getMillis()).isEqualTo(entity.getEndTZ().getMillis());

    assertThat(loaded.getTimeAsString().getMillis()).isEqualTo(startTZ.getMillis());

    em.remove(loaded);
    em.flush();
    em.clear();

    assertThat(em.find(JodaTimeEntity.class, entity.getId())).isNull();
  }

}
