package debop4k.core.spring.annotations;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로컬 개발 상태의 환경설정에 사용할 Annotation입니다. (Local -&gt; Develop -&gt; Test -&gt; Production)
 *
 * @author sunghyouk.bae@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("local")
public @interface Local {
}
