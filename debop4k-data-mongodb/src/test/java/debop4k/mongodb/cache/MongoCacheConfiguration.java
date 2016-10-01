//package debop4k.mongodb.cache;
//
//import debop4k.mongodb.AbstractMongoTest;
//import AbstractMongoConfig;
//import lombok.SneakyThrows;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCaching(proxyTargetClass = true)
//@ComponentScan(basePackageClasses = { UserRepository.class })
//public class MongoCacheConfiguration extends AbstractMongoConfig {
//
//  @Override
//  protected String getDatabaseName() {
//    return AbstractMongoTest.DATABASE_NAME;
//  }
//
//  @Bean
//  @SneakyThrows(Exception.class)
//  public MongoCacheManager mongoCacheManager() {
//    return new MongoCacheManager(mongoTemplate(), 60);
//  }
//}
