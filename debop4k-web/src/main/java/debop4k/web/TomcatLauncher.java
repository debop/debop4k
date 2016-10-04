//package debop4k.web;
//
//import debop4k.core.Charsets;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.catalina.core.AprLifecycleListener;
//import org.apache.catalina.core.JreMemoryLeakPreventionListener;
//import org.apache.catalina.core.ThreadLocalLeakPreventionListener;
//import org.apache.catalina.mbeans.GlobalResourcesLifecycleListener;
//import org.apache.catalina.startup.Tomcat;
//import org.springframework.boot.CommandLineRunner;
//
//import java.io.File;
//
///**
// * Tomcat Server 를 Embedded 방식으로 실행할 수 있도록 지원합니다.
// * Stand alone 으로 실행해야 할 Web Application 이나 테스트용 Web Application을 만들 때 사용합니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Slf4j
//public class TomcatLauncher implements CommandLineRunner {
//
//  public int getPort() { return 8080; }
//  public String getContextPath() { return "/"; }
//  public String getResourceBase() { return "src/main/webapp"; }
//  public String getProtocol() { return "org.apache.coyote.http11.Http11Protocol"; }
//
//  protected void initContext(Context ctx) {}
//
//  @Override
//  public void run(String... args) throws Exception {
//    Tomcat tomcat = new Tomcat();
//
//    tomcat.getServer().addLifecycleListener(new AprLifecycleListener());
//    tomcat.getServer().addLifecycleListener(new JreMemoryLeakPreventionListener());
//    tomcat.getServer().addLifecycleListener(new GlobalResourcesLifecycleListener());
//    tomcat.getServer().addLifecycleListener(new ThreadLocalLeakPreventionListener());
//
//    Connector connector = new Connector(getProtocol());
//    connector.setPort(getPort());
//    connector.setURIEncoding(Charsets.UTF_8.toString());
//    connector.setEnableLookups(false);
//
//    tomcat.getService().addConnector(connector);
//    tomcat.setConnector(connector);
//
//    Context context = tomcat.addWebapp(getContextPath(), new File(getResourceBase()).getAbsolutePath());
//    initContext(context);
//
//    log.info("Start Tomcat Web Server...");
//
//    tomcat.start();
//    tomcat.getServer().await();
//  }
//}
