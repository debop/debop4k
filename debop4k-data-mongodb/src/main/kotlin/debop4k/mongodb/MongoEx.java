///*
// *  Copyright (c) 2016. KESTI co, ltd
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.mongodb;
//
//import debop4k.core.io.serializers.Serializers;
//
///**
// * MongoDB 를 사용하기 위한 Helper method 를 제공합니다.
// *
// * @author sunghyouk.bae@gmail.com
// * @since 2015. 12. 11.
// */
//public final class MongoEx {
//
//  private MongoEx() {}
//
//  /**
//   * Mongo용 Document를 복사합니다.
//   *
//   * @param src 원본 Document
//   * @param <T> Document 의 수형
//   * @return 복사된 Document
//   */
//  public static <T extends AbstractMongoDocument> T copy(T src) {
//    return Serializers.FST_JAVA6.copy(src);
//  }
//
//  /**
//   * Mongo용 Document를 복사합니다. 단 복사된 Document 는 Transient object (id 값이 할당되지 않은) 입니다.
//   *
//   * @param src 원본 Document
//   * @param <T> Document 의 수형
//   * @return 복사된 Document
//   */
//  public static <T extends AbstractMongoDocument> T copyAndResetId(T src) {
//    T dest = Serializers.FST_JAVA6.copy(src);
//    dest.resetIdentifier();
//    return dest;
//  }
//}
