///*
// * Copyright (c) 2016. KESTI co, ltd
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.mongodb.logback;
//
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.classic.spi.IThrowableProxy;
//import ch.qos.logback.classic.spi.LoggingEvent;
//import ch.qos.logback.classic.spi.ThrowableProxyUtil;
//import ch.qos.logback.core.CoreConstants;
//import ch.qos.logback.core.UnsynchronizedAppenderBase;
//import com.mongodb.MongoClient;
//import com.mongodb.ServerAddress;
//import debop4k.core.asyncs.AsyncEx;
//import debop4k.core.utils.StringEx;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Loggingx;
//import org.joda.time.DateTime;
//import org.springframework.data.authentication.UserCredentials;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.net.UnknownHostException;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * logging 정보를 MongoDB 에 저장하는 appender 입니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Loggingx
//@Getter
//@Setter
//public class MongoDBLogAppender extends UnsynchronizedAppenderBase<LoggingEvent> {
//
//  public static String DEFAULT_DB_NAME = "logDB";
//  public static String DEFAULT_COLLECTION_NAME = "logs";
//
//  private MongoTemplate template;
//  private MongoClient client;
//
//  private String serverName;
//  private String applicationName;
//  private String host = ServerAddress.defaultHost();
//  private int port = ServerAddress.defaultPort();
//  private String dbName = DEFAULT_DB_NAME;
//  private String collectionName = DEFAULT_COLLECTION_NAME;
//  private String username;
//  private String password;
//
//  @Override
//  public void start() {
//    try {
//      connect();
//      super.start();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Override
//  protected void append(final LoggingEvent eventObject) {
//    if (eventObject == null)
//      return;
//
//    AsyncEx.future(new Runnable() {
//      @Override
//      public void run() {
//        MongoLogDocument doc = newLogDocument(eventObject);
//        template.save(doc, collectionName);
//      }
//    });
//  }
//
//  private void connect() throws UnknownHostException {
//    client = new MongoClient(host, port);
//
//    UserCredentials credentials = (username != null && password != null)
//                                  ? new UserCredentials(username, password)
//                                  : UserCredentials.NO_CREDENTIALS;
//
//    template = new MongoTemplate(client, dbName, credentials);
//
//    if (StringEx.isEmpty(collectionName))
//      collectionName = DEFAULT_COLLECTION_NAME;
//
//    if (!template.collectionExists(collectionName))
//      template.createCollection(collectionName);
//  }
//
//  private MongoLogDocument newLogDocument(ILoggingEvent event) {
//
//    MongoLogDocument doc =
//        MongoLogDocumentBuilder.of()
//                               .setServerName(this.serverName)
//                               .setApplicationName(this.applicationName)
//                               .setLogger(event.getLoggerName())
//                               .setLevelInt(event.getLevel().levelInt)
//                               .setLevelStr(event.getLevel().levelStr)
//                               .setThreadName(event.getThreadName())
//                               .setTimestamp(new DateTime(event.getTimeStamp()))
//                               .setMessage(event.getFormattedMessage())
//                               .build();
//
//    if (event.getMarker() != null) {
//      doc.setMarker(event.getMarker().getName());
//    }
//
//    IThrowableProxy tp = event.getThrowableProxy();
//
//    if (tp != null) {
//      String str = ThrowableProxyUtil.asString(tp);
//      List<String> stacktrace = Arrays.asList(str.replace("\t", "").split(CoreConstants.LINE_SEPARATOR));
//
//      if (stacktrace.size() > 0)
//        doc.setException(stacktrace.get(0));
//      if (stacktrace.size() > 1)
//        doc.setStackTrace(stacktrace.subList(1, stacktrace.size()));
//    }
//
//    return doc;
//  }
//}
