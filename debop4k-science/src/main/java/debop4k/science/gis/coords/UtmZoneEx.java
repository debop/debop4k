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
//package debop4k.science.gis.coords;
//
//import debop4k.science.gis.coords.BoundingBox;
//import debop4k.science.gis.coords.GeoLocation;
//import debop4k.science.gis.coords.UtmZone;
//import debop4k.science.gis.coords.UtmZonex;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.extern.slf4j.Loggingx;
//import org.eclipse.collections.api.list.MutableList;
//import org.eclipse.collections.impl.factory.Lists;
//
//import java.util.Comparator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentSkipListMap;
//
//import static debop4k.science.gis.coords.UtmZonex.*;
//
///**
// * UtmZoneEx
// *
// * @author sunghyouk.bae@gmail.com
// * @since 2015. 12. 11.
// * @deprecated use {@link UtmZonex}
// */
//@Loggingx
//@Deprecated
//public final class UtmZoneEx {
//
//  private UtmZoneEx() {}
//
//  /**
//   * UTM Zone 의 위도를 구분한 '구역 코드 - 시작 위도' 정보
//   */
//  @Getter(lazy = true)
//  private static final Map<Character, Double> utmLatitudes = buildUtmLatitudes();
//
//  /**
//   * UTM Zone 의 위도를 구분한 '구역 코드 - 시작 위도' 정보를 빌드합니다.
//   *
//   * @return UTM Zone 의 위도를 구분한 '구역 코드 - 시작 위도' 정보
//   */
//  private static Map<Character, Double> buildUtmLatitudes() {
//    Map<Character, Double> map = new LinkedHashMap<Character, Double>();
//    map.put('C', -80D);
//    map.put('D', -72D);
//    map.put('E', -64D);
//    map.put('F', -56D);
//    map.put('G', -48D);
//    map.put('H', -40D);
//    map.put('J', -32D);
//    map.put('K', -24D);
//    map.put('L', -16D);
//    map.put('M', -8D);
//    map.put('N', 0D);
//    map.put('P', 8D);
//    map.put('Q', 16D);
//    map.put('R', 24D);
//    map.put('S', 32D);
//    map.put('T', 40D);
//    map.put('U', 48D);
//    map.put('V', 56D);
//    map.put('W', 64D);
//    map.put('X', 72D);
//
//    return map;
//
//  }
//
//  /**
//   * latitudeZone이 UTM 의 Latitude Zone 에 해당하는 문자인지 여부를 파악합니다.
//   * 구분을 위해 UTM Latitude Zone에는 'I' 가 없습니다.
//   *
//   * @param latitudeZone 위도 방향의 Zone을 나타내는 문자
//   * @return UTM Latitude Zone에 해당하는 문자인지 여부
//   */
//  public static boolean isUtmLatitude(char latitudeZone) {
//    return getUtmLatitudes().keySet().contains(Character.toUpperCase(latitudeZone));
//  }
//
//  /**
//   * UTM Zone에 해당하는 영역에 대한 정보
//   * UtmZone 의 위경도 좌표의 Bounding box 의 맵
//   */
//  @Getter(lazy = true)
//  private static final Map<UtmZone, BoundingBox> utmZoneBoundingBoxes = buildUtmZoneBoundingBoxes();
//
//  /**
//   * UTM Zone 의 영역과 영역을 위경도 좌표로 표현한 BondingBox 의 맵을 빌드합니다.
//   *
//   * @return UTM Zone에 해당하는 영역에 대한 정보
//   */
//  private static Map<UtmZone, BoundingBox> buildUtmZoneBoundingBoxes() {
//    Map<UtmZone, BoundingBox> map = new ConcurrentSkipListMap<UtmZone, BoundingBox>();
//
//    for (int lon = UtmZonex.UTM_LONGITUDE_MIN; lon <= UtmZonex.UTM_LONGITUDE_MAX; lon++) {
//      for (char lat : getUtmLatitudes().keySet()) {
//        UtmZone zone = utmZoneOf(lon, lat);
//
//        double longitude = getLongitudeByUtm(lon);
//        double latitude = getLatitudeByUtm(lat);
//
//        BoundingBox bbox = BoundingBox.of(longitude,
//                                          latitude + UtmZonex.UTM_LATITUDE_SIZE,
//                                          longitude + UtmZonex.UTM_LONGITUDE_SIZE,
//                                          latitude);
//
//        map.put(zone, bbox);
//      }
//    }
//    return map;
//  }
//
//  /**
//   * UTM Longitude Zone 의 최소 경도를 구한다.
//   *
//   * @param utmLongitude UTM Longitude Zone (ex 31, 51 등)
//   * @return UTM Longitude Zone의 시작 경도
//   */
//  public static double getLongitudeByUtm(int utmLongitude) {
//    return (utmLongitude - 31) * UtmZonex.UTM_LONGITUDE_SIZE;
//  }
//
//  /**
//   * UTM Latitude Zone의 최소 위도 값을 구한다.
//   *
//   * @param utmLatitude UTM Latitude Zone ('S', 'T')
//   * @return UTM Latitude Zone의 최소 위도
//   */
//  public static double getLatitudeByUtm(char utmLatitude) {
//    return getUtmLatitudes().get(Character.toUpperCase(utmLatitude));
//  }
//
//  /**
//   * 경도가 속한 UTM Longitude Zone을 구한다.
//   *
//   * @param longitude 경도
//   * @return UTM Longitude Zone
//   */
//  public static int getUtmLongitude(double longitude) {
//    return (int) (longitude / 6 + 31);
//  }
//
//  /**
//   * 지정한 위도가 속한 UTM Latitude Zone 을 구합니다.
//   *
//   * @param latitude 위도
//   * @return UTM Latitude Zone (예 : 'S', 'T')
//   */
//  public static char getUtmLatitude(double latitude) {
//
//    MutableList<Character> latitudes = Lists.mutable.withAll(getUtmLatitudes().keySet());
//    latitudes.sortThis(new Comparator<Character>() {
//      @Override
//      public int compare(Character c1, Character c2) {
//        return -c1.compareTo(c2);
//      }
//    });
//
//    for (Character c : latitudes) {
//      if (getUtmLatitudes().get(c) <= latitude) {
//        return c;
//      }
//    }
//
//    throw new RuntimeException("해당 UTM Zone Latitude 를 찾지 못했습니다. latitude=" + latitude);
////    return getUtmLatitudes().entrySet()
////                            .stream()
////                            .sorted((e1, e2) -> -e1.getKey().compareTo(e2.getKey()))
////                            .filter(entry -> entry.getValue() <= latitude)
////                            .findFirst()
////                            .orElseThrow(() -> new RuntimeException("해당 UTM Zone Latitude 를 찾지 못했습니다. latitude=" + latitude))
////                            .getKey();
//  }
//
//  /**
//   * 위경도가 속한 UtmZone 반환합니다.
//   *
//   * @param geoLocation 위경도 정보
//   * @return 위경도가 속한 UtmZone
//   */
//  public static UtmZone getUtmZone(@NonNull GeoLocation geoLocation) {
//    int longitudeZone = getUtmLongitude(geoLocation.getLongitude());
//    char latitudeZone = getUtmLatitude(geoLocation.getLatitude());
//
//    return utmZoneOf(longitudeZone, latitudeZone);
//  }
//
//  /**
//   * UTM Zone 의 위경도 좌표로 Left Top 지점을 구한다.
//   *
//   * @param utmZone UtmZone 인스턴스
//   * @return 위경도 정보
//   */
//  public static GeoLocation getGeoLocation(@NonNull UtmZone utmZone) {
//
//    double longitude = getLongitudeByUtm(utmZone.getLongitudeZone());
//    double latitude = getLatitudeByUtm(utmZone.getLatitudeZone());
//
//    return GeoLocation.of(latitude + UtmZonex.UTM_LATITUDE_SIZE, longitude);
//  }
//
//  /**
//   * UTM Zone에 해당하는 영역을 위경도 좌표로 표현한 Bounding Box 을 반환합니다.
//   *
//   * @param utm UtmZone
//   * @return UTM Zone 영역을 위경도 좌표로 표현한 Bounding Box
//   */
//  public static BoundingBox getBoundingBox(@NonNull UtmZone utm) {
//    return getUtmZoneBoundingBoxes().get(utm);
//  }
//
//  /**
//   * UTM Zone 의 특정 Cell의 Bounding Box 를 계산합니다.
//   *
//   * @param utm  UTM Zone
//   * @param size Cell 의 크기 (경위도의 단위)
//   * @param row  Cell 의 row index (0부터 시작)
//   * @param col  Cell의 column index (0부터 시작)
//   * @return UtmZone의 특정 cell의 Bounding Box를 구합니다.
//   */
//  public static BoundingBox getCellBoundingBox(@NonNull UtmZone utm, double size, int row, int col) {
//    UtmZonex.log.trace("utm={}, size={}, row={}, col={}", utm, size, row, col);
//    BoundingBox utmBbox = getBoundingBox(utm);
//
//    double left = utmBbox.getLeft() + size * (double) col;
//    double top = utmBbox.getTop() - size * (double) row;
//    double right = left + size;
//    double bottom = top - size;
//
//    BoundingBox cellBbox = BoundingBox.of(left, top, right, bottom);
//
//    UtmZonex.log.trace("utm bbox={}, size={}, row={}, col={}, cell bbox={}", utmBbox, size, row, col, cellBbox);
//
//    return cellBbox;
//  }
//}
