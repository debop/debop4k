//package debop4k.mongodb.cache;
//
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import AbstractMongoDocument;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//
///**
// * MongoDB에 저장할 캐시 값을 표현합니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Setter
//public class MongoCacheItem extends AbstractMongoDocument {
//
//  private Object key;
//  private byte[] value;
//  private long expireAt = 0;
//
//  public MongoCacheItem() {}
//
//  public MongoCacheItem(@NonNull Object key, byte[] value) {
//    this(key, value, 0);
//  }
//  public MongoCacheItem(@NonNull Object key, byte[] value, long expireAt) {
//    this.key = key;
//    this.value = value;
//    this.expireAt = expireAt;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(key);
//  }
//
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("key", key)
//                .add("value", value)
//                .add("expireAt", expireAt);
//  }
//
//  private static final long serialVersionUID = 5081372628460588627L;
//}
