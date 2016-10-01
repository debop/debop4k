//package debop4k.mongodb.spring.boot.autoconfigure;
//
//import com.mongodb.WriteConcern;
//import debop4k.core.utils.StringEx;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
///**
// * MongoDB 접속을 위한 Spring Boot 용 환경설정 값
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Setter
//@ConfigurationProperties(prefix = MongodbProperties.PREFIX)
//public class MongodbProperties {
//
//  public static final String PREFIX = "debop4k.mongodb";
//
//  private String host = "localhost";
//  private Integer port = 27017;
//  private String databaseName = "test";
//  private String username = null;
//  private String password = null;
//
//  private Options options;
//
//  @Getter
//  @Setter
//  public static class Options {
//
//    public static final String DEFAULT_WRITE_CONCERN = "ACKNOWLEDGED";
//
//    private Integer connectionTimeout = 1000 * 10;
//    private Integer minConnectionPerHost = 0;
//    private Integer maxConnectionPerHost = 100;
//    private Integer threadsAllowedToBlockForConnectionMultiplier = 32;
//    private Boolean socketKeepAlive = false;
//    private Boolean sslEnabled = false;
//    private String writeConcernName = DEFAULT_WRITE_CONCERN;
//
//    public WriteConcern getWriteConcern() {
//      if (StringEx.isEmpty(writeConcernName)) {
//        writeConcernName = DEFAULT_WRITE_CONCERN;
//      }
//      WriteConcern writeConcern = WriteConcern.valueOf(writeConcernName);
//
//      return (writeConcern != null) ? writeConcern : WriteConcern.ACKNOWLEDGED;
//    }
//  }
//
//}
