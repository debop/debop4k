/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("HttpSecurityExtensions")

package debop4k.spring.security.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.openid.OpenIDLoginConfigurer

fun HttpSecurity.formLogin(body: FormLoginConfigurer<HttpSecurity>.()
-> FormLoginConfigurer<HttpSecurity>): HttpSecurity {
  return this.formLogin().body().and()
}

fun HttpSecurity.exceptionHandling(body: ExceptionHandlingConfigurer<HttpSecurity>.()
-> ExceptionHandlingConfigurer<HttpSecurity>): HttpSecurity {
  return this.exceptionHandling().body().and()
}

fun HttpSecurity.authorizeRequests(body: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry.() ->
ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry): HttpSecurity {
  return this.authorizeRequests().body().and()
}

fun HttpSecurity.logout(body: LogoutConfigurer<HttpSecurity>.() -> LogoutConfigurer<HttpSecurity>): HttpSecurity {
  return this.logout().body().and()
}

fun HttpSecurity.openidLogin(body: OpenIDLoginConfigurer<HttpSecurity>.() -> OpenIDLoginConfigurer<HttpSecurity>): HttpSecurity {
  return this.openidLogin().body().and()
}

fun HttpSecurity.handler(body: HeadersConfigurer<HttpSecurity>.() -> HeadersConfigurer<HttpSecurity>): HttpSecurity {
  return this.headers().body().and()
}

fun HttpSecurity.sessionManagement(body: SessionManagementConfigurer<HttpSecurity>.() -> SessionManagementConfigurer<HttpSecurity>): HttpSecurity {
  return this.sessionManagement().body().and()
}

fun HttpSecurity.portMapper(body: PortMapperConfigurer<HttpSecurity>.() -> PortMapperConfigurer<HttpSecurity>): HttpSecurity {
  return this.portMapper().body().and()
}

fun HttpSecurity.jee(body: JeeConfigurer<HttpSecurity>.() -> JeeConfigurer<HttpSecurity>): HttpSecurity {
  return this.jee().body().and()
}

fun HttpSecurity.x509(body: X509Configurer<HttpSecurity>.() -> X509Configurer<HttpSecurity>): HttpSecurity {
  return this.x509().body().and()
}

fun HttpSecurity.rememberMe(body: RememberMeConfigurer<HttpSecurity>.() -> RememberMeConfigurer<HttpSecurity>): HttpSecurity {
  return this.rememberMe().body().and()
}

fun HttpSecurity.requestCache(body: RequestCacheConfigurer<HttpSecurity>.() -> RequestCacheConfigurer<HttpSecurity>): HttpSecurity {
  return this.requestCache().body().and()
}

fun HttpSecurity.securityContext(body: SecurityContextConfigurer<HttpSecurity>.() -> SecurityContextConfigurer<HttpSecurity>): HttpSecurity {
  return this.securityContext().body().and()
}

fun HttpSecurity.servletApi(body: ServletApiConfigurer<HttpSecurity>.() -> ServletApiConfigurer<HttpSecurity>): HttpSecurity {
  return this.servletApi().body().and()
}

fun HttpSecurity.csrf(body: CsrfConfigurer<HttpSecurity>.() -> CsrfConfigurer<HttpSecurity>): HttpSecurity {
  return this.csrf().body().and()
}

fun HttpSecurity.anonymous(body: AnonymousConfigurer<HttpSecurity>.() -> AnonymousConfigurer<HttpSecurity>): HttpSecurity {
  return this.anonymous().body().and()
}

fun HttpSecurity.requiresChannel(body: ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry.()
-> ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry): HttpSecurity {
  return this.requiresChannel().body().and()
}

fun HttpSecurity.httpBasic(body: HttpBasicConfigurer<HttpSecurity>.() -> HttpBasicConfigurer<HttpSecurity>): HttpSecurity {
  return this.httpBasic().body().and()
}
