package debop4k.data.spring

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author sunghyouk.bae@gmail.com
 */
@SpringBootApplication
open class DataSpringBootApplication {

}

fun main(args: Array<String>): Unit {
  SpringApplication.run(DataSpringBootApplication::class.java, *args)
}