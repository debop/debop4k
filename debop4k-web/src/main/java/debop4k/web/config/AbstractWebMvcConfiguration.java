package debop4k.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVC 환경설정에 대한 추상화 클래스
 *
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableWebMvc
public abstract class AbstractWebMvcConfiguration extends WebMvcConfigurerAdapter {

  @Bean
  public ViewResolver viewResolver() {
    // JSP View Resolver
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("WEB-INF/views/");
    resolver.setSuffix(".jsp");
    resolver.setExposeContextBeansAsAttributes(true);
    return resolver;
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }
}
