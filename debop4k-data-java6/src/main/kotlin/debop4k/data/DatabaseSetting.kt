package debop4k.data

import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import java.io.Serializable

/**
 * Database 접속을 위한 설정정보를 표현하는 ValueObject 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
data class DatabaseSetting(var host: String = "localhost",
                           var driverClassName: String = "",
                           var jdbcUrl: String = "",
                           var username: String = "",
                           var password: String = "",
                           var maxPoolSize: Int = DataSources.MAX_POOL_SIZE,
                           var minIdleSize: Int = DataSources.MIN_IDLE_SIZE,
                           var testQuery: String? = "SELECT 1") : Serializable {

  val props: MutableMap<String, String>? = Maps.mutable.of<String, String>()
}
