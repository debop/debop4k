package debop4k.data.factory

import debop4k.data.DatabaseSetting
import javax.sql.DataSource

/**
 * [DataSource] 를 생성해주는 Factory 인터페이스
 * @author sunghyouk.bae@gmail.com
 */
interface DataSourceFactory {

  /**
   * [DataSource] 생성
   */
  fun create(setting: DatabaseSetting): DataSource

}