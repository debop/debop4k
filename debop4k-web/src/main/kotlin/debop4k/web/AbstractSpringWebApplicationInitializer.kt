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

package debop4k.web

import debop4k.core.loggerOf
import debop4k.spring.config.Profiles
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import javax.servlet.Filter

/**
 * Spring MVC 용 Servlet Initializer 입니다.
 * web.xml 을 사용하지 않고, 코드 상에서 정의합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractSpringWebApplicationInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {

  protected val log = loggerOf(javaClass)

  override fun getServletMappings(): Array<out String> {
    return arrayOf<String>()
  }

  override fun getServletFilters(): Array<out Filter> {
    val filter = CharacterEncodingFilter().apply {
      encoding = Charsets.UTF_8.toString()
    }
    return arrayOf(filter)
  }

  override fun createServletApplicationContext(): WebApplicationContext {
    val context = super.createServletApplicationContext()
    return setProfiles(context)
  }

  private fun setProfiles(context: WebApplicationContext): WebApplicationContext {
    val ctx = context as? ConfigurableApplicationContext
    val profile = System.getProperty("profile", Profiles.Local.name).toLowerCase()

    log.info("환경설정 중 active profile을 지정합니다. active profile={}", profile)

    if (ctx != null)
      ctx.environment.setActiveProfiles(profile)

    return ctx as WebApplicationContext
  }
}