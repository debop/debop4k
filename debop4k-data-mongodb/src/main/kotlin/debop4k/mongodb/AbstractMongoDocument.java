///*
// * Copyright 2015-2020 KESTI
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
//package debop4k.mongodb;
//
//import debop4k.core.AbstractValueObject;
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import debop4k.core.utils.Objects;
//import lombok.Getter;
//import org.bson.types.ObjectId;
//
///**
// * MongoDB 용 Document의 기본 클래스
// *
// * @author sunghyouk.bae@gmail.com
// * @since 2015. 8. 8.
// */
//@Getter
//public abstract class AbstractMongoDocument extends AbstractValueObject {
//
//  /**
//   * MongoDB Document 의 Identifier
//   * NOTE: Spring Data Repository를 만들 때, 꼭 ObjectId 로 해주어야 Id 로 조회하는 방식이 제대로 동작합니다.
//   */
//  @org.springframework.data.annotation.Id
//  ObjectId id;
//
//  /**
//   * Document의 identifier 값을 초기화 합니다.
//   * Document를 복사하여 사용할 때, identifer를 초기화하여 다른 Document로 만듭니다.
//   */
//  public void resetIdentifier() {
//    this.id = null;
//  }
//
//  @Override
//  public boolean equals(Object obj) {
//    if (obj != null && obj instanceof AbstractMongoDocument) {
//      AbstractMongoDocument doc = (AbstractMongoDocument) obj;
//      return id != null && doc.getId() != null
//             ? Objects.equals(id, doc.getId())
//             : id == null && doc.getId() == null && super.equals(obj);
//    }
//    return false;
//  }
//
//  @Override
//  public int hashCode() {
//    return (id != null) ? Hashx.compute(id) : toString().hashCode();
//  }
//
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("id", id);
//  }
//
//  private static final long serialVersionUID = 8472797851539024491L;
//}
