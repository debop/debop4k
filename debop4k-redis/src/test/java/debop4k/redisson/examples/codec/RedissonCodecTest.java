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

package debop4k.redisson.examples.codec;

import debop4k.redisson.examples.AbstractRedissonTest;
import debop4k.redisson.examples.TestObject;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.client.RedisException;
import org.redisson.client.codec.Codec;
import org.redisson.codec.*;
import org.redisson.config.Config;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RedissonCodecTest extends AbstractRedissonTest {

  private Codec codec = new SerializationCodec();
  private Codec kyroCodec = new KryoCodec();
  private Codec jsonCodec = new JsonJacksonCodec();
  //  private Codec cborCodec = new CborJacksonCodec();
  private Codec fstCodec = new FstCodec();
  private Codec snappyCodec = new SnappyCodec();
  private Codec msgPackCodec = new MsgPackJacksonCodec();
  private Codec lz4Codec = new LZ4Codec();

  @Test
  public void testLZ4() {
    Config config = createConfig();
    config.setCodec(lz4Codec);
    redisson = Redisson.create(config);

    test();
  }

  @Test
  public void testJdk() {
    Config config = createConfig();
    config.setCodec(codec);
    redisson = Redisson.create(config);

    test();
  }

  @Test(expected = RedisException.class)
  public void testMsgPack() {
    Config config = createConfig();
    config.setCodec(msgPackCodec);
    redisson = Redisson.create(config);

    test();
  }

  @Test
  public void testFst() {
    Config config = createConfig();
    config.setCodec(fstCodec);
    redisson = Redisson.create(config);

    test();
  }

  @Test
  public void testSnappy() {
    Config config = createConfig();
    config.setCodec(snappyCodec);
    redisson = Redisson.create(config);

    test();
  }

  @Test(expected = RedisException.class)
  public void testJson() {
    Config config = createConfig();
    config.setCodec(jsonCodec);
    redisson = Redisson.create(config);

    test();
  }

  @Test(expected = RedisException.class)
  public void testKryo() {
    Config config = createConfig();
    config.setCodec(kyroCodec);
    redisson = Redisson.create(config);

    test();
  }

//  @Test(expected = RedisException.class)
//  public void testCbor() {
//    Config config = createConfig();
//    config.setCodec(cborCodec);
//    redisson = Redisson.create(config);
//
//    test();
//  }


  private void test() {
    RMap<Integer, Map<String, Object>> map = redisson.getMap("getAll");
    Map<String, Object> a = new HashMap<String, Object>();
    a.put("double", new Double(100000.0));
    a.put("float", 100.0f);
    a.put("int", 100);
    a.put("long", 10000000000L);
    a.put("boolt", true);
    a.put("boolf", false);
    a.put("string", "testString");
    a.put("array", new ArrayList<Object>(Arrays.asList(1, 2.0, "adsfasdfsdf")));
    a.put("date", new Date());
    // 위의 테스트 중 empty constructor 가 없는 joda-time 의 chrology 때문에 예외가 발생한다.
    // 예외가 발생하는 놈들은 사용하지 말자
    a.put("joda", DateTime.now());

    map.fastPut(1, a);
    Map<String, Object> resa = map.get(1);
    assertThat(a).isEqualTo(resa);

    RSet<TestObject> set = redisson.getSet("set");

    set.add(new TestObject("1", "2"));
    set.add(new TestObject("1", "2"));
    set.add(new TestObject("2", "3"));
    set.add(new TestObject("3", "4"));
    set.add(new TestObject("5", "6"));

    assertThat(set.contains(new TestObject("2", "3"))).isTrue();
    assertThat(set.contains(new TestObject("1", "2"))).isTrue();
    assertThat(set.contains(new TestObject("1", "9"))).isFalse();
  }
}
