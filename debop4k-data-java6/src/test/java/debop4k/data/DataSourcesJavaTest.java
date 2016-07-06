package debop4k.data;

import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSourcesJavaTest {

  @Test
  public void testEmbeddedHsql() throws Exception {
    DataSource ds = DataSources.ofEmbeddedHsql();
    assertThat(ds).isNotNull();

    Connection conn = ds.getConnection();
    try {
      assertThat(conn).isNotNull();
    } finally {
      conn.close();
    }
  }
}
