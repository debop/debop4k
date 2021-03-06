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

package debop4k.data.orm.hibernate.usertypes.encrypt;

import debop4k.core.cryptography.Cryptographyx;
import debop4k.core.cryptography.MD5StringDigester;
import debop4k.core.cryptography.StringDigester;

/**
 * {@link MD5StringDigester} 를 이용하여, 문자열을 암호화하여 저장하는 UserType 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
public class MD5StringUserType extends HashStringUserType {
  @Override
  public StringDigester digester() {
    return Cryptographyx.getMD5String();
  }
}
