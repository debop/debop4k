# Auditable 기능
    
spring-data-jpa 1.9.1.RELEASE 에서 새롭게 도입된 Auditable 기능은 엔티티의 @CreatedDate, @LastModifiedDate 값을 관리해줍니다.

다만 spring data jpa 가 QueryDsl 3.x 대를 이용하여 컴파일해서 QueryDsl 4.x 를 사용하게 되면 문제가 발생합니다.

QueryDsl 4.x 를 사용하기 위해 spring data jpa 에서 제공하는 AbstractAuditable 클래스를 사용하지 않도록 해야 합니다