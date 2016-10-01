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

package debop4k.redisson.examples.reactive;//package debop4k.redisson.examples.reactive;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.redisson.api.RListReactive;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * RedissonListReactiveTest
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Slf4j
//public class RedissonListReactiveTest extends AbstractReactiveTest {
//
//  @Test
//  public void testEquals() {
//    RListReactive<String> list1 = redisson.getList("list1");
//    sync(list1.add("1"));
//    sync(list1.add("2"));
//    sync(list1.add("3"));
//
//    RListReactive<String> list2 = redisson.getList("list2");
//    sync(list2.add("1"));
//    sync(list2.add("2"));
//    sync(list2.add("3"));
//
//    RListReactive<String> list3 = redisson.getList("list3");
//    sync(list3.add("0"));
//    sync(list3.add("2"));
//    sync(list3.add("3"));
//
//    assertThat(list1).isEqualTo(list2);
//    assertThat(list1).isNotEqualTo(list3);
//  }
//
//  @Test
//  public void testHashCode() throws Exception {
//    RListReactive<String> list = redisson.getList("list");
//    sync(list.add("a"));
//    sync(list.add("b"));
//    sync(list.add("c"));
////        list.add("a");
////        list.add("b");
////        sync(list.add("c"));
//
//    assertThat(list.hashCode()).isEqualTo(126145);
//  }
//}
