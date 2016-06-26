/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("beanutils")

package debop4k.spring.beans

import org.springframework.beans.BeanInstantiationException
import org.springframework.beans.BeanUtils
import org.springframework.beans.BeansException
import org.springframework.beans.PropertyAccessorUtils
import org.springframework.core.MethodParameter
import java.beans.PropertyDescriptor
import java.beans.PropertyEditor
import java.lang.reflect.Constructor
import java.lang.reflect.Method

/**
 * var 로 선언된 필드 중 non null 수형에 대해 초기화 값을 지정하고자 할 때 사용합니다.
 * 특히 ```@Autowired```, ```@Inject``` var 수형에 사용하기 좋다.
 * @see lateinit
 * @see Delegates.nonNull
 */
fun <T> uninitialized(): T = null as T

@Throws(BeanInstantiationException::class)
fun <T> Class<T>.instantiate(): T = BeanUtils.instantiate(this)

@Throws(BeanInstantiationException::class)
fun <T> Class<T>.instantiateClass(): T = BeanUtils.instantiateClass(this)

@Throws(BeanInstantiationException::class)
fun <T> Constructor<T>.instanticateClass(vararg args: Any): T = BeanUtils.instantiateClass(this, *args)

@Throws(BeanInstantiationException::class)
fun <T> Class<*>.instantiateClass(assignableTo: Class<T>): T = BeanUtils.instantiateClass(this, assignableTo)

fun Class<*>.findMethod(methodName: String, vararg paramTypes: Class<*>): Method
    = BeanUtils.findMethod(this, methodName, *paramTypes)

fun Class<*>.findMethodWithMinimalParameters(methodName: String): Method
    = BeanUtils.findMethodWithMinimalParameters(this, methodName)

fun Array<Method>.findMethodWithMinimalParameters(methodName: String): Method
    = BeanUtils.findMethodWithMinimalParameters(this, methodName)

fun Class<*>.findDeclaredMethodWithMinimalParameters(methodName: String): Method
    = BeanUtils.findDeclaredMethodWithMinimalParameters(this, methodName)


fun String.resolveSignature(clazz: Class<*>): Method = BeanUtils.resolveSignature(this, clazz)

fun Class<*>.getPropertyDescriptors(): Array<PropertyDescriptor>
    = BeanUtils.getPropertyDescriptors(this)

fun Class<*>.getPropertyDescriptor(propertyName: String): PropertyDescriptor
    = BeanUtils.getPropertyDescriptor(this, propertyName)

fun Method.findPropertyForMethod(): PropertyDescriptor
    = BeanUtils.findPropertyForMethod(this)

fun Method.findPropertyForMethod(clazz: Class<*>): PropertyDescriptor
    = BeanUtils.findPropertyForMethod(this, clazz)

fun Class<*>.findEditorByConvention(): PropertyEditor
    = BeanUtils.findEditorByConvention(this)

fun String.findPropertyType(vararg beanClasses: Class<*>): Class<*>
    = BeanUtils.findPropertyType(this, *beanClasses)

fun PropertyDescriptor.getWriteMethodParameter(): MethodParameter
    = BeanUtils.getWriteMethodParameter(this)

fun Class<*>.isSimpleProperty(): Boolean = BeanUtils.isSimpleProperty(this)

fun Class<*>.isSimpleValueType(): Boolean = BeanUtils.isSimpleValueType(this)

@Throws(BeansException::class)
fun Any.copyProperties(target: Any): Unit = BeanUtils.copyProperties(this, target)

@Throws(BeansException::class)
fun Any.copyProperties(target: Any, vararg ignoredProperties: String): Unit
    = BeanUtils.copyProperties(this, target, *ignoredProperties)

@Throws(BeansException::class)
fun Any.copyProperties(target: Any, editable: Class<*>): Unit
    = BeanUtils.copyProperties(this, target, editable)

//@Throws(BeansException::class)
//fun Any.copyProperties(target: Any, editable: Class<*>, vararg ignoredProperties:String): Unit
//    = BeanUtils.copyProperties(this, target, editable, *ignoredProperties)

fun String.getPropertyName(): String = PropertyAccessorUtils.getPropertyName(this)

fun String.isNestedOrIndexedProperty(): Boolean = PropertyAccessorUtils.isNestedOrIndexedProperty(this)

fun String.getFirstNestedPropertySeparatorIndex(): Int
    = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(this)

fun String.getLastNestedPropertySeparatorIndex(): Int
    = PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(this)

fun String.matchesProperty(propertyPath: String): Boolean
    = PropertyAccessorUtils.matchesProperty(this, propertyPath)

fun String.canonicalPropertyName(): String = PropertyAccessorUtils.canonicalPropertyName(this)

fun Array<String>.canonicalPropertyNames(): Array<String>
    = PropertyAccessorUtils.canonicalPropertyNames(this)
