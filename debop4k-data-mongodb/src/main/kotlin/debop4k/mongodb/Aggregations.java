//package debop4k.mongodb;
//
//import lombok.extern.slf4j.Loggingx;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
//import org.springframework.data.mongodb.core.query.Criteria;
//
//import java.util.List;
//
///**
// * MongoDB Aggregations 작업에 필요한 메소드를 제공합니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Loggingx
//public final class Aggregations {
//
//  public static Aggregation of(List<AggregationOperation> operations) {
//    return Aggregation.newAggregation(operations);
//  }
//
//  public static void addAggregations(List<AggregationOperation> operations, Criteria... criterias) {
//    for (Criteria crit : criterias) {
//      operations.add(Aggregation.match(crit));
//    }
//  }
//}
