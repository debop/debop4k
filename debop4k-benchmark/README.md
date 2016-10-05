# debop4k-benchmark

여러가지 모듈에 대한 성능 측정을 위한 코드를 포함한 모듈입니다.

IntelliJ IDEA 에 jmh plugin이 java 코드에 대해서만 지원하므로, IDEA 상에서 benchmark 할 때에는 java 로 구현해야 하고,
jar 로 만들어서 console 에서 테스트 시에는 kotlin 으로 구현해도 됩니다.

### Build:

    mvn clean package

### Run: 

    java -jar target/benchmarks.jar

