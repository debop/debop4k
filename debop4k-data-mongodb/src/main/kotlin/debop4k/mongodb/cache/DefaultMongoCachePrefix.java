//package debop4k.mongodb.cache;
//
//import debop4k.core.utils.StringEx;
//import lombok.NonNull;
//
//public class DefaultMongoCachePrefix implements MongoCachePrefix {
//
//  private String delimeter;
//
//  public DefaultMongoCachePrefix() {
//    this(null);
//  }
//
//  public DefaultMongoCachePrefix(String delimeter) {
//    this.delimeter = delimeter;
//  }
//  @Override
//  public String prefix(@NonNull String cacheName) {
//    return StringEx.isEmpty(delimeter)
//           ? cacheName
//           : cacheName.concat(delimeter);
//  }
//}
