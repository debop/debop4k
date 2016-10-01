//package debop4k.mongodb.cache;
//
//import debop4k.mongodb.models.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Repository;
//
//@Slf4j
//@Repository
//public class UserRepository {
//
//  @Cacheable(value = "user", key = "'user:' + #id")
//  public User getUser(String id, int favoriteMovieSize) {
//    log.debug("새로운 사용자를 생성합니다. id={}", id);
//    User user = User.of(favoriteMovieSize);
//    user.setId(id);
//
//    return user;
//  }
//
//  @CacheEvict(value = "user", key = "'user:' + #user.id")
//  public void updateUser(User user) {
//    log.debug("사용자 정보를 갱신합니다. 캐시에서 삭제됩니다...");
//  }
//}
