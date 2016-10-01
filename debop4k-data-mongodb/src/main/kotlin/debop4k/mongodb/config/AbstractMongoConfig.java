//package debop4k.mongodb.config;
//
//import com.mongodb.*;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//
///**
// * MongoDB 접속을 위한 Spring Framework의 JavaConfig 용 환경설정 정보
// *
// * @author sunghyouk.bae@gmail.com
// * @since 2015. 8. 8.
// */
//@Configuration
//public abstract class AbstractMongoConfig extends AbstractMongoConfiguration {
//
//  @Override
//  public Mongo mongo() throws Exception {
//    return new MongoClient(ServerAddress.defaultHost(), mongoOptions());
//  }
//
//  public MongoClientOptions mongoOptions() {
//    return MongoClientOptions.builder()
//                             .connectionsPerHost(100)
//                             .threadsAllowedToBlockForConnectionMultiplier(32)
//                             .socketKeepAlive(true)
//                             .writeConcern(getWriteConcern())
//                             .build();
//  }
//
//  protected WriteConcern getWriteConcern() {
//    return WriteConcern.ACKNOWLEDGED;
//  }
//}
