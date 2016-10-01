# debop4k-data-orm

Database 접속과 기본적인 SQL 구문을 실행할 수 있도록 해주는 라이브러리입니다.

### 기능

	1. Hibernate 와 JPA 의 기본 엔티티
	2. 여러 형태의 질의를 빌드할 수 있도록 해주는 Helper class
	3. Spring framework 와 연계한 환경 설정 파일
	4. Custom 수형을 DB Type 으로 변환해주는 UserType 과 Converter (예: joda-time 의 DateTime, Password 암호화 등)
	5. 여러가지 형태의 Mapping 예제와 테스트 코드
 
*KESTI 에서는 기본적으로 iBatis 를 쓰지 않고, Hibernate 를 쓰도록 할 것입니다.*