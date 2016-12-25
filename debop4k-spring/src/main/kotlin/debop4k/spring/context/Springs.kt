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
 */

package debop4k.spring.context

import debop4k.core.Local
import debop4k.core.loggerOf
import org.springframework.beans.PropertyValue
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionValidationException
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.stereotype.Component
import java.lang.*
import java.util.*
import java.util.concurrent.atomic.*
import javax.inject.Inject

/**
 * Springs
 * @author debop sunghyouk.bae@gmail.com
 */
// TODO: Object로 변경하고, ApplicationContext를 static variable에 inject 하도록 합니다.
@Component
open class Springs @Inject constructor(_context: ApplicationContext) {

  init {
    globalContext.compareAndSet(null, _context)
  }

  private val log = loggerOf(Springs::class)

  companion object {
    private val DEFAULT_APPLICATION_CONTEXT_XML = "applicationContext.xml"
    private val LOCAL_SPRING_CONTEXT = Springs::class.java.name + ".globalContext"
    private val NOT_INITIONALIZED_MSG =
        "Springs의 ApplicationContext가 초기화되지 않았습니다 Springs 를 ComponentScan 에 추가하셔야 합니다."

    private val globalContext = AtomicReference<ApplicationContext?>(null)

    private val localContextStack = object : ThreadLocal<Stack<GenericApplicationContext?>>() {
      override fun initialValue(): Stack<GenericApplicationContext?> {
        return Stack<GenericApplicationContext?>()
      }
    }
  }

  fun isInitialized(): Boolean = globalContext.get() != null
  fun isNotInitialized(): Boolean = globalContext.get() == null

  fun assertInitialized(): Unit {
    assert(isInitialized()) { NOT_INITIONALIZED_MSG }
  }

  val context: GenericApplicationContext
    get() {
      var context = localContext
      if (context == null)
        context = globalContext.get() as? GenericApplicationContext

      assert(context != null)
      return context!!
    }

  val localContext: GenericApplicationContext?
    get() = localContextStack.get().peek()

  fun init(): Unit {
    init(DEFAULT_APPLICATION_CONTEXT_XML)
  }

  fun init(vararg resourceLocations: String): Unit {
    init(GenericXmlApplicationContext(*resourceLocations))
  }

  fun init(appContext: ApplicationContext): Unit {
    log.info("Spring ApplicationContext 초기화 작업을 시작합니다...")
    if (isInitialized()) {
      log.info("Spring ApplicationContext 가 이미 초기화 되었으므로, skip 합니다. init 전에 reset을 호출하세요")
    } else {
      globalContext.set(appContext)
      log.info("Spring ApplicationContext 초기화 작업을 완료했습니다.")
    }
  }

  fun initByClasses(vararg classes: Class<*>) {
    init(AnnotationConfigApplicationContext(*classes))
  }

  fun initByPackage(vararg basePackages: String) {
    init(AnnotationConfigApplicationContext(*basePackages))
  }

  fun useLocalContext(localContext: GenericApplicationContext): AutoCloseable {
    log.debug("로컬 컨텍스트를 사용하려고 합니다. localContext={}", localContext)
    localContextStack.get().push(localContext)
    return AutoCloseable { reset(localContext) }
  }

  @Synchronized
  fun reset(contextToReset: ApplicationContext?): Unit {
    if (contextToReset == null) {
      globalContext.set(null)
      log.info("Global Context 를 reset 했습니다.")
      return
    }
    log.debug("ApplicationContext를 reset 합니다. {}", contextToReset)

    if (localContext === contextToReset) {
      localContextStack.get().pop()
      if (localContextStack.get().size == 0) {
        Local[LOCAL_SPRING_CONTEXT] = null
      }
      log.info("Local application context 를 reset 했습니다.")
      return
    }
    if (globalContext.compareAndSet(contextToReset, null)) {
      log.info("Global application context 를 reset 했습니다.")
    }
  }

  fun reset() {
    if (localContext != null) {
      reset(localContext)
    } else {
      reset(globalContext.get())
    }
  }

  fun getBean(name: String): Any? {
    require(name.isNotBlank())
    assertInitialized()
    return context.getBean(name)
  }

  fun getBean(name: String, vararg args: Any?): Any? {
    require(name.isNotBlank())
    assertInitialized()
    return context.getBean(name, *args)
  }

  fun <T> getBean(beanClass: Class<T>): T? {
    assertInitialized()
    return context.getBean(beanClass)
  }

  fun <T> getBean(name: String, beanClass: Class<T>): T? {
    require(name.isNotBlank())
    assertInitialized()
    return context.getBean(name, beanClass)
  }

  @JvmOverloads
  fun getBeanNamesType(beanClass: Class<*>,
                       includeNonSingletons: Boolean = true,
                       allowEagerInit: Boolean = true): Array<String> {
    assertInitialized()
    log.debug("해당 수형의 모든 Bean의 이름을 조회합니다. beanClass=[{}], includeNonSingletons=[{}], allowEagerInit=[{}]",
              beanClass.name, includeNonSingletons, allowEagerInit)

    return context.getBeanNamesForType(beanClass, includeNonSingletons, allowEagerInit)
  }

  /**
   * 지정한 타입의 Bean 들의 인스턴스를 가져옵니다. (Prototype Bean 도 포함됩니다.)

   * @param beanClass bean 의 수형
   * *
   * @return bean 인스턴스 컬렉션
   */
  @JvmOverloads
  fun <T> getBeansByType(beanClass: Class<T>,
                         includeNonSingletons: Boolean = true,
                         allowEagerInit: Boolean = true): Collection<T> {
    val beanMap = getBeansOfType(beanClass, includeNonSingletons, allowEagerInit)

    return beanMap.values
  }

  @JvmOverloads
  fun <T> getFirstBeanByType(beanClass: Class<T>,
                             includeNonSingletons: Boolean = true,
                             allowEagerInit: Boolean = true): T? {
    val beans = getBeansByType(beanClass, includeNonSingletons, allowEagerInit)
    if (beans.isNotEmpty())
      return beans.first()
    else
      return null as? T
  }

  @Synchronized
  fun <T> getBeansOfType(beanClass: Class<T>): Map<String, T> {
    return getBeansOfType(beanClass, true, true)
  }

  /**
   * 지정된 수형 또는 상속한 수형으로 등록된 bean 들을 조회합니다.

   * @param beanClass            Bean 수형
   * @param includeNonSingletons Singleton 타입의 Bean 이 아닌 경우도 포함
   * @param allowEagerInit       미리 초기화를 수행할 것인가?
   * @return Bean 명 - Bean 인스턴스의 맵
   */
  @Synchronized
  fun <T> getBeansOfType(beanClass: Class<T>?,
                         includeNonSingletons: Boolean,
                         allowEagerInit: Boolean): Map<String, T> {
    assert(beanClass != null)

    log.debug("해당 수형의 모든 Bean을 조회합니다. beanClass=[{}], includeNonSingletons=[{}], allowEagerInit=[{}]",
              beanClass!!.name, includeNonSingletons, allowEagerInit)

    return context.getBeansOfType(beanClass,
                                  includeNonSingletons,
                                  allowEagerInit)
  }

  /**
   * 지정된 수형의 Bean 을 조회합니다. 등록되지 않았으면 등록하고 반환합니다.

   * @param beanClass Bean 수형
   * @return Bean 인스턴스
   */
  @Synchronized
  fun <T> getOrRegisterBean(beanClass: Class<T>): T {
    return getOrRegisterBean(beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON)
  }

  /**
   * 지정된 수형의 Bean 을 조회합니다. 등록되지 않았으면 등록하고 반환합니다.

   * @param beanClass Bean 수형
   * @param scope     scope ( singleton, prototype )
   * @return Bean 인스턴스
   */
  @Synchronized
  fun <T> getOrRegisterBean(beanClass: Class<T>,
                            scope: String): T {
    return getOrRegisterBean(beanClass, beanClass, scope)
  }

  /**
   * 등록된 beanClass 를 조회 (보통 Interface) 하고, 없다면, registerBeanClass (Concrete Class) 를 등록합니다.

   * @param beanClass       조회할 Bean의 수형 (보통 인터페이스)
   * @param registBeanClass 등록되지 않은 beanClass 일때, 실제 등록할 Bean의 수형 (Concrete Class)
   * @param scope           "singleton", "prototype"
   * @return 등록된 Bean의 인스턴스
   */
  @JvmOverloads
  @Synchronized
  fun <T> getOrRegisterBean(beanClass: Class<T>,
                            registBeanClass: Class<T>,
                            scope: String = ConfigurableBeanFactory.SCOPE_SINGLETON): T {
    val bean = getFirstBeanByType(beanClass, true, true)
    if (bean != null)
      return bean

    registerBean(registBeanClass.name, registBeanClass, scope)
    return context.getBean(registBeanClass)
  }

  /**
   * Singleton Bean을 가져옵니다. 없으면 새로 등록하고 Bean을 반환합니다.
   * @param beanClass Bean의 수형
   * @return Bean 인스턴스
   */
  @Synchronized
  fun <T> getOrRegisterSingletonBean(beanClass: Class<T>): T {
    return getOrRegisterBean(beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON)
  }

  /**
   * Prototype Bean을 가져옵니다. 없으면 새로 등록하고 Bean을 반환합니다.
   * @param beanClass Bean의 수형
   * @return Bean 인스턴스
   */
  @Synchronized
  fun <T> getOrRegisterPrototypeBean(beanClass: Class<T>): T {
    return getOrRegisterBean(beanClass, ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  }

  /**
   * 지정된 Bean 이름이 사용되었는가?

   * @param beanName Bean 이름
   * @return 사용 여부
   */
  @Synchronized
  fun isBeanNameInUse(beanName: String): Boolean {
    return context.isBeanNameInUse(beanName)
  }

  /**
   * 지정된 Bean 이름이 현재 Context에 등록되었는가?

   * @param beanName Bean 이름
   * @return 사용 여부
   */
  @Synchronized
  fun isRegisteredBean(beanName: String): Boolean {
    return context.isBeanNameInUse(beanName)
  }

  /**
   * 지정한 수형의 Bean이 등록되었는지 여부를 반환한다.

   * @param beanClass the bean class
   * @return the boolean
   */
  @Synchronized
  fun <T> isRegisteredBean(beanClass: Class<T>): Boolean {
    try {
      return context.getBean(beanClass) != null
    } catch (e: Exception) {
      return false
    }
  }

  @JvmOverloads
  @Synchronized
  fun <T> registerBean(beanName: String,
                       beanClass: Class<T>,
                       scope: String = ConfigurableBeanFactory.SCOPE_SINGLETON,
                       vararg propertyValues: PropertyValue): Boolean {
    val definition = RootBeanDefinition(beanClass)
    definition.scope = scope

    for (pv in propertyValues) {
      definition.propertyValues.addPropertyValue(pv)
    }
    return registerBean(beanName, definition)
  }

  @Synchronized
  fun registerBean(beanName: String, beanDefinition: BeanDefinition): Boolean {
    require(beanName.isNotBlank())

    if (isBeanNameInUse(beanName))
      throw BeanDefinitionValidationException("이미 등록된 Bean입니다. beanName=" + beanName)

    log.info("새로운 Bean을 등록합니다. beanName=[{}], beanDefinition=[{}]", beanName, beanDefinition)

    try {
      context.registerBeanDefinition(beanName, beanDefinition)
      return true
    } catch (e: Exception) {
      log.error("새로운 Bean 등록에 실패했습니다. beanName=" + beanName, e)
    }

    return false
  }

  @Synchronized
  fun registerBean(beanName: String, instance: Any): Boolean {
    require(beanName.isNotBlank())

    try {
      context.beanFactory.registerSingleton(beanName, instance)
      return true
    } catch (e: Exception) {
      log.error("인스턴스를 빈으로 등록하는데 실패했습니다. beanName=" + beanName, e)
      return false
    }

  }

  @Synchronized
  fun registerSingletonBean(beanName: String,
                            instance: Any): Boolean {
    return registerBean(beanName, instance)
  }

  @Synchronized
  fun <T> registerSingletonBean(beanClass: Class<T>?,
                                vararg pvs: PropertyValue): Boolean {
    assert(beanClass != null)
    return registerSingletonBean(beanClass!!.name, beanClass, *pvs)
  }

  @Synchronized
  fun <T> registerSingletonBean(beanName: String,
                                beanClass: Class<T>,
                                vararg pvs: PropertyValue): Boolean {
    return registerBean(beanName, beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON, *pvs)
  }

  @Synchronized
  fun <T> registerPrototypeBean(beanClass: Class<T>,
                                vararg pvs: PropertyValue): Boolean {
    return registerPrototypeBean(beanClass.name, beanClass, *pvs)
  }

  @Synchronized
  fun <T> registerPrototypeBean(beanName: String,
                                beanClass: Class<T>,
                                vararg pvs: PropertyValue): Boolean {
    return registerBean(beanName, beanClass, ConfigurableBeanFactory.SCOPE_PROTOTYPE, *pvs)
  }

  @Synchronized
  fun removeBean(beanName: String) {
    require(beanName.isNotBlank())

    if (isBeanNameInUse(beanName)) {

      log.debug("ApplicationContext에서 name=[{}]인 Bean을 제거합니다.", beanName)
      context.removeBeanDefinition(beanName)
    }
  }

  @Synchronized
  fun <T> removeBean(beanClass: Class<T>) {
    log.debug("Bean 형식 [{}]의 모든 Bean을 ApplicationContext에서 제거합니다.", beanClass.name)

    val beanNames = context.getBeanNamesForType(beanClass, true, true)
    for (beanName in beanNames)
      removeBean(beanName)
  }

  @Synchronized
  fun tryGetBean(beanName: String): Any? {
    require(beanName.isNotBlank())
    try {
      return getBean(beanName)
    } catch (e: Exception) {
      log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanName=" + beanName, e)
      return null
    }

  }

  @Synchronized
  fun tryGetBean(beanName: String, vararg args: Any): Any? {
    require(beanName.isNotBlank())
    try {
      return getBean(beanName, *args)
    } catch (e: Exception) {
      log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanName=" + beanName, e)
      return null
    }

  }

  @Synchronized
  fun <T> tryGetBean(beanClass: Class<T>): T? {
    try {
      return getBean(beanClass)
    } catch (e: Exception) {
      log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanClass=" + beanClass.name, e)
      return null
    }

  }

  @Synchronized
  fun <T> tryGetBean(beanName: String, beanClass: Class<T>): T? {
    try {
      return getBean(beanName, beanClass)
    } catch (e: Exception) {
      log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanName=" + beanName, e)
      return null
    }

  }
}