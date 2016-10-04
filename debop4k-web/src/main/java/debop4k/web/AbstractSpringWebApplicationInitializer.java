package debop4k.web;

import debop4k.core.spring.Profiles;
import kotlin.text.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Spring MVC 용 Servlet Initializer 입니다.
 * web.xml 을 사용하지 않고, 코드 상에서 정의합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 13.
 */
@Slf4j
public abstract class AbstractSpringWebApplicationInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  @Override
  protected Filter[] getServletFilters() {
    CharacterEncodingFilter filter = new CharacterEncodingFilter();
    filter.setEncoding(Charsets.UTF_8.toString());

    return new Filter[]{filter};
  }

  @Override
  protected WebApplicationContext createServletApplicationContext() {
    WebApplicationContext webAppContext = super.createServletApplicationContext();
    return setProfiles(webAppContext);
  }

  private WebApplicationContext setProfiles(WebApplicationContext context) {
    ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
    String profile = System.getProperty("profile", Profiles.Local.name()).toLowerCase();

    log.info("환경설정 중 active profile 을 지정합니다. profile={}", profile);

    if (ctx != null) {
      ctx.getEnvironment().setActiveProfiles(profile);
    }
    return (WebApplicationContext) ctx;
  }
}
