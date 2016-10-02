//package debop4k.mongodb.spring.boot.autoconfigure;
//
//import com.mongodb.Mongo;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.ServerAddress;
//import lombok.extern.slf4j.Loggingx;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//
//import javax.inject.Inject;
//
///**
// * MongoDB 환경설정을 Spring Boot AutoConfiguration 을 이용하여 수행할 수 있도록 합니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Loggingx
//@Configuration
//@EnableConfigurationProperties({ MongodbProperties.class })
//public abstract class MongodbAutoConfiguration extends AbstractMongoConfiguration {
//
//  @Inject MongodbProperties mongoProps;
//
//  @Override
//  protected String getDatabaseName() {
//    return mongoProps.getDatabaseName();
//  }
//
//  @Override
//  public Mongo mongo() throws Exception {
//    ServerAddress address = new ServerAddress(mongoProps.getHost(), mongoProps.getPort());
//    return new MongoClient(address, getMongoOptions());
//  }
//
//  public MongoClientOptions getMongoOptions() {
//    MongodbProperties.Options options = mongoProps.getOptions();
//    log.debug("mongo options={}", options);
//
//    return MongoClientOptions.builder()
//                             .minConnectionsPerHost(options.getMinConnectionPerHost())
//                             .connectionsPerHost(options.getMaxConnectionPerHost())
//                             .threadsAllowedToBlockForConnectionMultiplier(options.getThreadsAllowedToBlockForConnectionMultiplier())
//                             .socketKeepAlive(options.getSocketKeepAlive())
//                             .writeConcern(options.getWriteConcern())
//                             .connectTimeout(options.getConnectionTimeout())
//                             .build();
//  }
//}
