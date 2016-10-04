@file:JvmName("annotations")

package debop4k.core.spring

import org.springframework.context.annotation.Profile

/**
 *  개발 상태의 환경설정에 사용할 Annotation입니다.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Profile("develop")
annotation class Develop

/**
 * 로컬 개발 상태의 환경설정에 사용할 Annotation입니다.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Profile("local")
annotation class Local

/**
 *  운영 상태 환경설정에 사용할 Annotation입니다.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Profile("production")
annotation class Production

/**
 * 테스트 상태의 환경설정에 사용할 Annotation입니다.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Profile("testing")
annotation class Testing

