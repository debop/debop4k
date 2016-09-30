package debop4k.core.collections;///*
// * Copyright 2015-2020 KESTI s.r.o.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.core.collections;
//
//import AbstractCoreTest;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * NumberRangeTest
// *
// * @author sunghyouk.bae@gmail.com
// * @since 2015. 8. 14.
// */
//@Slf4j
//public class RangeExTest extends AbstractCoreTest {
//
//  @Test
//  public void intRange() {
//    assertThat(RangeEx.range(5).size()).isEqualTo(5);
//
//    assertThat(RangeEx.range(1, 5).iterator()).contains(1, 2, 3, 4);
//    assertThat(RangeEx.range(1, 5, 2).iterator()).contains(1, 3);
//  }
//
//  @Test
//  public void intRangeGroup() {
//    List<RangeEx.IntRange> group = RangeEx.grouped(0, 50, 10);
//    assertThat(group.size()).isEqualTo(5);
//
//    assertThat(group.get(0).size()).isEqualTo(10);
//    assertThat(group.get(0).getFromInclude()).isEqualTo(0);
//    assertThat(group.get(0).getToExclude()).isEqualTo(10);
//
//    assertThat(group.get(4).size()).isEqualTo(10);
//    assertThat(group.get(4).getFromInclude()).isEqualTo(40);
//    assertThat(group.get(4).getToExclude()).isEqualTo(50);
//  }
//
//  @Test
//  public void longRange() {
//    assertThat(RangeEx.range(5L).size()).isEqualTo(5);
//
//    assertThat(RangeEx.range(1L, 5L).iterator()).contains(1L, 2L, 3L, 4L);
//    assertThat(RangeEx.range(1L, 5L, 2L).iterator()).contains(1L, 3L);
//  }
//
//  @Test
//  public void longRangeGroup() {
//    List<RangeEx.LongRange> group = RangeEx.grouped(0L, 50L, 10L);
//    assertThat(group.size()).isEqualTo(5);
//
//    assertThat(group.get(0).size()).isEqualTo(10);
//    assertThat(group.get(0).getFromInclude()).isEqualTo(0);
//    assertThat(group.get(0).getToExclude()).isEqualTo(10);
//
//    assertThat(group.get(4).size()).isEqualTo(10);
//    assertThat(group.get(4).getFromInclude()).isEqualTo(40);
//    assertThat(group.get(4).getToExclude()).isEqualTo(50);
//  }
//}
