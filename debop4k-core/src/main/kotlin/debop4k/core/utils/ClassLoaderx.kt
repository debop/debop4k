/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.utils

import java.security.AccessController
import java.security.PrivilegedAction


fun getClassLoader(clazz: Class<*>): ClassLoader {
  if (System.getSecurityManager() == null) {
    return clazz.classLoader
  } else {
    return AccessController.doPrivileged(PrivilegedAction { clazz.classLoader })
  }
}

fun getDefaultClassLoader(): ClassLoader {
  var cl = getContextClassLoader()
  // TODO: Caller 를 참고해야 한다..
  if (cl == null) {
    val callerClass = Thread.currentThread().javaClass
    cl = callerClass.classLoader
  }
  return cl
}

fun getContextClassLoader(): ClassLoader {
  if (System.getSecurityManager() != null) {
    return Thread.currentThread().contextClassLoader
  } else {
    return AccessController.doPrivileged(PrivilegedAction { Thread.currentThread().contextClassLoader })
  }
}

fun getSystemClassLoader(): ClassLoader {
  if (System.getSecurityManager() != null) {
    return ClassLoader.getSystemClassLoader()
  } else {
    return AccessController.doPrivileged(PrivilegedAction { ClassLoader.getSystemClassLoader() })
  }
}