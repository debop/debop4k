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

package debop4k.data.exposed.examples

import com.mysql.management.MysqldResource
import com.mysql.management.driverlaunched.MysqldResourceNotFoundException
import com.mysql.management.driverlaunched.ServerLauncherSocketFactory
import com.mysql.management.util.Files
import org.jetbrains.exposed.dao.EntityCache
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTimeZone
import ru.yandex.qatools.embed.postgresql.PostgresStarter
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig
import ru.yandex.qatools.embed.postgresql.distribution.Version
import java.sql.Connection
import java.util.*
import kotlin.concurrent.thread


enum class TestDB(val connection: String,
                  val driver: String,
                  val beforeConnection: () -> Any,
                  val afterConnection: () -> Unit) {

  H2("jdbc:h2:mem:", "org.h2.Driver", { Unit }, {}),
  MYSQL("jdbc:mysql:mxj://localhost:12345/test?createDatabaseIfNotExist=true&server.initialize-user=false&user=root&password=",
        "com.mysql.jdbc.Driver",
        beforeConnection = {
          System.setProperty(Files.USE_TEST_DIR, java.lang.Boolean.TRUE!!.toString()); Files().cleanTestDir(); Unit
        },
        afterConnection = {
          try {
            val baseDir = Files().tmp(MysqldResource.MYSQL_C_MXJ)
            ServerLauncherSocketFactory.shutdown(baseDir, null)
          } catch (e: MysqldResourceNotFoundException) {
            exposedLogger.warn(e.message, e)
          } finally {
            Files().cleanTestDir()
          }
        }),

  POSTGRESQL("jdbc:postgresql://localhost:12346/template1?user=root&password=root",
             "org.postgresql.Driver",
             beforeConnection = { postgresSQLProcess.start() },
             afterConnection = { postgresSQLProcess.stop() });

  companion object {
    fun enabledInTests(): List<TestDB> {
      val concreteDialects = System.getProperty("exposed.test.dialects", "").let {
        if (it == "") emptyList()
        else it.split('.').map { it.trim().toUpperCase() }
      }
      return values().filter {
        concreteDialects.isEmpty() || it.name in concreteDialects
      }
    }
  }
}

private val registeredOnShutdown = HashSet<TestDB>()

private val postgresSQLProcess by lazy {
  val config = PostgresConfig(
      Version.Main.PRODUCTION, AbstractPostgresConfig.Net("localhost", 12346),
      AbstractPostgresConfig.Storage("template1"), AbstractPostgresConfig.Timeout(),
      AbstractPostgresConfig.Credentials("root", "root")
                             )

  PostgresStarter.getDefaultInstance().prepare(config)
}

abstract class DatabaseTestBase() {

  fun withDb(dbSettings: TestDB, statement: Transaction.() -> Unit) {
    if (dbSettings !in TestDB.enabledInTests()) return

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    DateTimeZone.setDefault(DateTimeZone.UTC)

    if (dbSettings !in registeredOnShutdown) {
      dbSettings.beforeConnection()
      Runtime.getRuntime().addShutdownHook(thread(false) { dbSettings.afterConnection() })
      registeredOnShutdown += dbSettings
    }

    val database = Database.connect(dbSettings.connection,
                                    user = "root",
                                    driver = dbSettings.driver)

    transaction(Connection.TRANSACTION_SERIALIZABLE, 1) {
      logger.addLogger(StdOutSqlLogger())

      statement()
    }
  }

  fun withDb(statement: Transaction.() -> Unit) {
    TestDB.enabledInTests().forEach {
      withDb(it, statement)
    }
  }

  fun withTables(excludeSettings: List<TestDB>,
                 vararg tables: Table,
                 statement: Transaction.() -> Unit) {
    (TestDB.enabledInTests().toList() - excludeSettings).forEach {
      withDb(it) {
        SchemaUtils.create(*tables)
        try {
          statement()
          commit()
        } finally {
          SchemaUtils.drop(*EntityCache.sortTablesByReferences(tables.toList()).reversed().toTypedArray())
        }
      }
    }
  }

  fun withTables(vararg tables: Table,
                 statement: Transaction.() -> Unit)
      = withTables(excludeSettings = emptyList(), tables = *tables, statement = statement)

  fun <T> Transaction.assertEquals(a: T, b: T)
      = kotlin.test.assertEquals(a, b, "Failed equals. a=$a, b=$b")

  fun <T> Transaction.assertEquals(a: T, b: List<T>)
      = kotlin.test.assertEquals(a, b.single(), "Failed equals. a=$a, b=${b.single()}")

}

