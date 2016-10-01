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

package debop4k.data.orm.hibernate.usertypes;

/**
 * TODO: UserType 에 해당하는 ClassName 을 문자열로 정의해서 사용하기 쉽게 한다.
 * UserTypes
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 8.
 */
public interface UserTypes {

  String SNAPPY_STRING_USER_TYPE =
      "debop4k.data.orm.hibernate.usertypes.compress.SnappyStringUserType";

  String SNAPPY_BYTE_ARRAY_USER_TYPE =
      "debop4k.data.orm.hibernate.usertypes.compress.SnappyByteArrayUserType";
}
