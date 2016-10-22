//package debop4k.web.listeners;
//
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
///**
// * {@link javax.servlet.ServletContextListener} 를 구현한 추상화 클래스입니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Slf4j
//public abstract class AbstractServletContext implements ServletContextListener {
//
//  @Override
//  public void contextInitialized(ServletContextEvent sce) {
//    log.debug("ServletContext Listener를 시작합니다. servlet name={}",
//              sce.getServletContext().getServletContextName());
//  }
//
//  @Override
//  public void contextDestroyed(ServletContextEvent sce) {
//    log.debug("ServletContext Listener를 종료합니다. servlet name={}",
//              sce.getServletContext().getServletContextName());
//  }
//}
