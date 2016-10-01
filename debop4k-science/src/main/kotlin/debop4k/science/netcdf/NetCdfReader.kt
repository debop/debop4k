/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.science.netcdf

import debop4k.core.areEquals
import debop4k.core.loggerOf
import debop4k.core.utils.LINE_SEPARATOR
import debop4k.core.utils.TAB
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList
import org.eclipse.collections.impl.list.mutable.primitive.FloatArrayList
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList
import ucar.nc2.NetcdfFile
import ucar.nc2.Variable
import ucar.nc2.util.CancelTask
import java.io.File
import java.io.IOException
import java.net.URI

/**
 * Net CDF 파일의 정보를 읽어드립니다.
 * Created by debop
 */
object NetCdfReader {

  private val log = loggerOf(javaClass)

  @JvmField val CAHCE_EXTENSIONS = arrayOf("gbx9", "ncx3", "idx")

  /**
   * 지정된 경로의 NetCDF 파일을 오픈합니다.

   * @param path 파일의 전체경로
   * *
   * @return [NetcdfFile] 인스턴스
   */
  @JvmStatic fun open(path: String): NetcdfFile = NetcdfFile.open(path)

  /**
   * 지정된 경로의 NetCDF 파일을 오픈합니다.
   *
   * @param path 파일의 전체경로
   * @return {@link NetcdfFile} 인스턴스
   */
  @JvmStatic fun open(path: String,
                      bufferSize: Int = -1,
                      cancelTask: CancelTask? = null,
                      iospMessage: Any? = null): NetcdfFile
      = NetcdfFile.open(path, bufferSize, cancelTask, iospMessage)

  /**
   * 지정된 경로의 NetCDF 파일을 오픈하고, 데이터를 메모리에 모두 로드합니다.
   *
   * @param filename 파일의 전체경로
   * @return {@link NetcdfFile} 인스턴스
   */
  @JvmStatic fun openInMemory(filename: String): NetcdfFile = NetcdfFile.openInMemory(filename)

  /**
   * 지정된 경로의 NetCDF 파일을 오픈하고, 데이터를 메모리에 모두 로드합니다.
   *
   * @param uri 파일의 전체경로
   * @return {@link NetcdfFile} 인스턴스
   */
  @JvmStatic fun openInMemory(uri: URI): NetcdfFile = NetcdfFile.openInMemory(uri)

  @JvmOverloads
  @JvmStatic fun openInMemoery(name: String, data: ByteArray, iospClassName: String? = null): NetcdfFile {
    return NetcdfFile.openInMemory(name, data, iospClassName)
  }

  /**
   * 파일을 NetCdf 형식으로 읽을 수 있는지 여부를 판단한다.
   *
   * @param path 파일 전체 경로
   * @return NetCdf 형식으로 읽을 수 있는지 여부
   */
  @JvmStatic fun canRead(path: String): Boolean = canRead(File(path))

  /**
   * 파일을 NetCdf 형식으로 읽을 수 있는지 여부를 판단한다.
   *
   * @param file 파일 인스턴스
   * @return NetCdf 형식으로 읽을 수 있는지 여부
   */
  @JvmStatic fun canRead(file: File): Boolean {
    return !file.isDirectory &&
           !isNetcdfCacheFile(file) &&
           NetcdfFile.canOpen(file.canonicalPath)
  }

  /**
   * 파일을 NetCdf 형식으로 읽을 수 있는지 여부를 판단한다.
   *
   * @param path 파일 전체 경로
   * @return NetCdf 형식으로 읽을 수 있는지 여부
   */
  @JvmStatic fun isNetcdfCacheFile(path: String): Boolean = isNetcdfCacheFile(File(path))

  /**
   * 파일을 NetCdf 형식으로 읽을 수 있는지 여부를 판단한다.
   *
   * @param file 파일 인스턴스
   * @return NetCdf 형식으로 읽을 수 있는지 여부
   */
  @JvmStatic fun isNetcdfCacheFile(file: File): Boolean {
    if (file.exists() && file.isFile) {
      val filename = file.name.toLowerCase()
      return CAHCE_EXTENSIONS.find { filename.endsWith(it) } != null
    } else {
      return false
    }
  }

  /**
   * NetCDF 파일 내의 지정한 이름의 [Variable] 을 구합니다.
   * @param nc      [NetcdfFile] 인스턴스
   * @param varName 찾고자하는 [Variable]의 명칭
   * @return [Variable] 인스턴스. 해당 이름의 Variable 이 없는 경우에는 null 을 반환한다.
   */
  @JvmStatic fun getVariable(nc: NetcdfFile, varName: String): Variable? {
    for (v in nc.variables) {
      log.trace("varName={}, v={}", varName, v.fullName)
      if (areEquals(v.fullName, varName)) {
        return v
      }
    }
    return null
  }

  /**
   * NetCDF 파일 내의 지정한 접두사로 시작하는 [Variable] 중 첫번째 찾은 변수를 반환합니다.

   * @param nc     [NetcdfFile] 인스턴스
   * *
   * @param prefix 찾고자하는 [Variable]의 접두사
   * *
   * @return [Variable] 인스턴스. 해당 이름의 Variable 이 없는 경우에는 null 을 반환한다.
   */
  @JvmStatic fun getVariableStartsWith(nc: NetcdfFile, prefix: String): Variable? {
    for (v in nc.variables) {
      if (v != null && v.fullName != null && v.fullName.startsWith(prefix)) {
        return v
      }
    }
    return null
  }

  /**
   * NetCDF 파일 내의 지정한 접미사를 가진 [Variable] 중 첫번째 찾은 변수를 반환합니다.
   *
   * @param nc     [NetcdfFile] 인스턴스
   * @param surfix 찾고자하는 [Variable]의 접미사
   * @return [Variable] 인스턴스. 해당 이름의 Variable 이 없는 경우에는 null 을 반환한다.
   */
  @JvmStatic fun getVariableEndsWith(nc: NetcdfFile, surfix: String): Variable? {
    for (v in nc.variables) {
      if (v != null && v.fullName != null && v.fullName.endsWith(surfix)) {
        return v
      }
    }
    return null
  }

  /**
   * NetCDF 파일 내의 지정한 검색어를 가진 [Variable] 중 첫번째 찾은 변수를 반환합니다.
   *
   * @param nc          [NetcdfFile] 인스턴스
   * @param nameToMatch 찾고자하는 [Variable]의 이름
   * @return [Variable] 인스턴스. 해당 이름의 Variable 이 없는 경우에는 null 을 반환한다.
   */
  @JvmStatic fun getVariableContains(nc: NetcdfFile, nameToMatch: String): Variable? {
    for (v in nc.variables) {
      if (v != null && v.fullName != null && v.fullName.contains(nameToMatch)) {
        return v
      }
    }
    return null
  }

  /**
   * [Variable]이 가진 Data 값이 스칼라 형식인지 여부
   * @param v [Variable] 인스턴스
   * @return Variable이 나타내는 데이터 값이 스칼라 값인지 여부
   */
  @JvmStatic fun isScalar(v: Variable): Boolean = getValueSize(v) == 1

  /**
   * [Variable]이 가진 데이터의 크기 (모든 Dimension 크기)
   * @param v [Variable] 인스턴스
   * @return 데이터의 전체 크기
   */
  @JvmStatic fun getValueSize(v: Variable): Int {
    var size = 1
    for (shape in v.shape) {
      size *= shape
    }
    return size
  }

  /**
   * [Variable] 의 값을 Integer 수형으로 읽는다.
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Integer 수형으로 반환한다. 없으면 null을 반환한다.
   */
  @JvmStatic fun readInt(v: Variable): Int = readInt(v, 0)

  /**
   * [Variable] 의 값을 Integer 수형으로 읽는다.
   * @param v  [Variable] 인스턴스
   * @param dv 데이터 값이 없거나, 읽는데 실패한 경우 반환할 기본 값
   * @return [Variable]의 데이터 값을 Integer 수형으로 반환한다.
   */
  @JvmStatic fun readInt(v: Variable, dv: Int): Int {
    try {
      return v.readScalarInt()
    } catch (t: Throwable) {
      return dv
    }
  }

  /**
   * [Variable] 의 값을 Long 수형으로 읽는다.
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Long 수형으로 반환한다. 없으면 null을 반환한다.
   */
  @JvmStatic fun readLong(v: Variable): Long = v.readScalarLong()

  /**
   * [Variable] 의 값을 Long 수형으로 읽는다.
   * @param v  [Variable] 인스턴스
   * @param dv 데이터 값이 없거나, 읽는데 실패한 경우 반환할 기본 값
   * @return [Variable]의 데이터 값을 Long 수형으로 반환한다.
   */
  @JvmStatic fun readLong(v: Variable, dv: Long): Long {
    try {
      return v.readScalarLong()
    } catch (t: Throwable) {
      return dv
    }
  }

  /**
   * [Variable] 의 값을 Float 수형으로 읽는다.
   *
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Float 수형으로 반환한다.
   */
  @JvmStatic fun readFloat(v: Variable): Float = v.readScalarFloat()

  /**
   * [Variable] 의 값을 Float 수형으로 읽는다.
   *
   * @param v  [Variable] 인스턴스
   * @param dv 데이터 값이 없거나, 읽는데 실패한 경우 반환할 기본 값
   * @return [Variable]의 데이터 값을 Float 수형으로 반환한다.
   */
  @JvmStatic fun readFloat(v: Variable, dv: Float): Float {
    try {
      return v.readScalarFloat()
    } catch(e: IOException) {
      return dv
    }
  }

  /**
   * [Variable] 의 값을 Double 수형으로 읽는다.
   *
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Double 수형으로 반환한다.
   */
  @JvmStatic fun readDouble(v: Variable): Double = v.readScalarDouble()

  /**
   * [Variable] 의 값을 Double 수형으로 읽는다.
   *
   * @param v  [Variable] 인스턴스
   * @param dv 데이터 값이 없거나, 읽는데 실패한 경우 반환할 기본 값
   * @return [Variable]의 데이터 값을 Double 수형으로 반환한다.
   */
  @JvmStatic fun readDouble(v: Variable, dv: Double): Double {
    try {
      return v.readScalarDouble()
    } catch(e: IOException) {
      return dv
    }
  }

  /**
   * [Variable] 의 값을 Integer 수형의 1차원 배열로 읽습니다.
   *
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Integer 수형의 1차원 배열로 반환한다.
   */
  @JvmStatic fun readIntArray(v: Variable): IntArrayList {

    val length = getValueSize(v)
    val elements = IntArrayList(length)

    val iter = v.read().indexIterator
    while (iter.hasNext()) {
      elements.add(iter.intNext)
    }
    return elements
  }

  /**
   * [Variable] 의 값을 Long 수형의 1차원 배열로 읽습니다.
   *
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Long 수형의 1차원 배열로 반환한다.
   */
  @JvmStatic fun readLongArray(v: Variable): LongArrayList {

    val length = getValueSize(v)
    val elements = LongArrayList(length)

    val iter = v.read().indexIterator
    while (iter.hasNext()) {
      elements.add(iter.longNext)
    }
    return elements
  }

  /**
   * [Variable] 의 값을 Float 수형의 1차원 배열로 읽습니다.
   *
   * @param v [Variable] 인스턴스
   * @return [Variable]의 데이터 값을 Float 수형의 1차원 배열로 반환한다.
   */
  @JvmStatic fun readFloatArray(v: Variable): FloatArrayList {

    val length = getValueSize(v)
    val elements = FloatArrayList(length)

    val iter = v.read().indexIterator
    while (iter.hasNext()) {
      elements.add(iter.floatNext)
    }
    return elements
  }

  /**
   * [Variable] 의 값을 Double 수형의 1차원 배열로 읽습니다.

   * @param v [Variable] 인스턴스
   * *
   * @return [Variable]의 데이터 값을 Double 수형의 1차원 배열로 반환한다.
   * *
   * @see NetCdfReader.read1DJavaArray
   */
  @JvmStatic fun readDoubleArray(v: Variable): DoubleArrayList {

    val length = getValueSize(v)
    val elements = DoubleArrayList(length)

    val iter = v.read().indexIterator
    while (iter.hasNext()) {
      elements.add(iter.doubleNext)
    }
    return elements
  }

  /**
   * Variable의 Dimension 을 판단하여 값을 읽어들여 N 차원의 Java 배열로 반환합니다.
   *
   * // 3차원 데이터인 경우
   * double[][][] matrix = (double[][][])NetCdfReader.readNDJavaArray(v);
   *
   * @param v [Variable] 인스턴스
   * @return N 차원의  Java 배열
   */
  @JvmStatic fun readNDJavaArray(v: Variable): Any {
    return v.read().copyToNDJavaArray()
  }

  /**
   * Variable 의 값들을 모두 읽어, 원하는 요소 수형의 1차원 배열로 반환합니다.
   *
   * double[] elements = (double[])NetCdfReader.read1DJavaArray(v, double.class);
   * @param v           [Variable] 인스턴스
   * @param elementType Java 배열의 수형 (ex. double.class, int.class)
   * @return Java 1차원 배열
   */
  @JvmStatic fun read1DJavaArray(v: Variable, elementType: Class<*>): Any {
    return v.read().get1DJavaArray(elementType)
  }


  /**
   * [NetcdfFile] 의 내부 속성 정보를 로그애 씁니다.
   * @param nc [NetcdfFile] 인스턴스
   */
  @JvmStatic fun displayNetcdfFile(nc: NetcdfFile) {
    for (v in nc.variables) {
      log.debug("variable name={},  dimensions={}, dataType={}", v.fullName, v.dimensions, v.dataType)
      for (dim in v.dimensions) {
        log.debug("dimension = {}, length={}", dim.fullName, dim.length)
      }
    }
  }

  /**
   * [NetcdfFile] 의 내부 속성 정보를 문자열로 반환합니다.

   * @param nc [NetcdfFile] 인스턴스
   * @return NetCDF 파일의 정보
   */
  @JvmStatic fun getInformation(nc: NetcdfFile): String {
    val builder = StringBuilder()

    for (v in nc.variables) {
      builder.append("Variable")
          .append(" name=").append(v.fullName)
          .append(", dimensions=").append(v.dimensions)
          .append(", dataType=").append(v.dataType)
          .append(LINE_SEPARATOR)

      for (dim in v.dimensions) {
        builder.append(TAB)
            .append("dimension name=").append(dim.fullName)
            .append(", length=").append(dim.length)
            .append(LINE_SEPARATOR)
      }
    }

    return builder.toString()
  }
}