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

package debop4k.data.orm.springdata.interceptors;

import debop4k.data.orm.jpa.config.databases.JpaTestConfiguration;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackageClasses = {CustomerRepository.class})
public class InterceptorConfiguration extends JpaTestConfiguration {

  @Override
  public String[] getMappedPackageNames() {
    return new String[]{Customer.class.getPackage().getName()};
  }

  @Bean
  public CustomizableTraceInterceptor interceptor() {
    // AOP 를 이용하여 메소드 실행 시작/완료 시점에 관련 정보를 Log에 쓸 수 있도록 합니다.
    CustomizableTraceInterceptor interceptor = new CustomizableTraceInterceptor();
    interceptor.setEnterMessage("Entering $[methodName]($[arguments]).");
    interceptor.setExitMessage("Leaving $[methodName](..) with return value $[returnValue], took $[invocationTime]ms.");

    return interceptor;
  }

  @Bean
  public Advisor traceAdvisor() {
    // Repository 를 구현한 Class에 대해 interceptor를 연결해줍니다.

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.*(..))");

    return new DefaultPointcutAdvisor(pointcut, interceptor());
  }


}
