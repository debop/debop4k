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

package debop4k.data.orm.hibernate

// NOTE: QueryDSL 을 사용하기 위해서는 Java 만 사용할 수 있다

//package debop4k.data.orm.hibernate
//
//import debop4k.core.ToStringHelper
//import debop4k.data.AbstractNamedParameter
//import org.hibernate.type.StandardBasicTypes
//import org.hibernate.type.Type
//
///**
// * HibernateParameter
// * @author debop sunghyouk.bae@gmail.com
// */
//class HibernateParameter
//@JvmOverloads
//constructor(override val name: String,
//            override var value: Any?,
//            val paramType: Type = StandardBasicTypes.SERIALIZABLE) : AbstractNamedParameter(name, value) {
//
//  override fun buildStringHelper(): ToStringHelper {
//    return super.buildStringHelper()
//        .add("paramType", paramType)
//  }
//}