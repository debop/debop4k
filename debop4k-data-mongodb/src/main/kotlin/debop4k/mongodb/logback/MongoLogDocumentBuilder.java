//package debop4k.mongodb.logback;
//
//import org.joda.time.DateTime;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author sunghyouk.bae@gmail.com
// */
//public final class MongoLogDocumentBuilder {
//
//  private String serverName;
//  private String applicationName;
//  private String logger;
//  private int levelInt;
//  private String levelStr;
//  private String threadName;
//  private String message;
//  private DateTime timestamp;
//  private String marker;
//  private String exception;
//  private List<String> stackTrace = new ArrayList<String>();
//
//  private MongoLogDocumentBuilder() {}
//
//  public static MongoLogDocumentBuilder of() { return new MongoLogDocumentBuilder();}
//
//  public MongoLogDocumentBuilder setServerName(String serverName) {
//    this.serverName = serverName;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setApplicationName(String applicationName) {
//    this.applicationName = applicationName;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setLogger(String logger) {
//    this.logger = logger;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setLevelInt(int levelInt) {
//    this.levelInt = levelInt;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setLevelStr(String levelStr) {
//    this.levelStr = levelStr;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setThreadName(String threadName) {
//    this.threadName = threadName;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setMessage(String message) {
//    this.message = message;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setTimestamp(DateTime timestamp) {
//    this.timestamp = timestamp;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setMarker(String marker) {
//    this.marker = marker;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setException(String exception) {
//    this.exception = exception;
//    return this;
//  }
//
//  public MongoLogDocumentBuilder setStackTrace(List<String> stackTrace) {
//    this.stackTrace = stackTrace;
//    return this;
//  }
//
//  public MongoLogDocument build() {
//    MongoLogDocument mongoLogDocument = new MongoLogDocument();
//    mongoLogDocument.setServerName(serverName);
//    mongoLogDocument.setApplicationName(applicationName);
//    mongoLogDocument.setLogger(logger);
//    mongoLogDocument.setLevelInt(levelInt);
//    mongoLogDocument.setLevelStr(levelStr);
//    mongoLogDocument.setThreadName(threadName);
//    mongoLogDocument.setMessage(message);
//    mongoLogDocument.setTimestamp(timestamp);
//    mongoLogDocument.setMarker(marker);
//    mongoLogDocument.setException(exception);
//    mongoLogDocument.setStackTrace(stackTrace);
//    return mongoLogDocument;
//  }
//}
