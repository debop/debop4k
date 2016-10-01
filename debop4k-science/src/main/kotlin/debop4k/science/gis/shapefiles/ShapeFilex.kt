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

@file:JvmName("ShapeFilex")

package debop4k.science.gis.shapefiles

import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.GeometryFactory
import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.science.gis.coords.BoundingBox
import org.eclipse.collections.impl.list.mutable.FastList
import org.geotools.data.shapefile.dbf.DbaseFileReader
import org.geotools.data.shapefile.files.ShpFiles
import org.geotools.data.shapefile.shp.ShapefileHeader
import org.geotools.data.shapefile.shp.ShapefileReader
import java.io.File
import java.nio.charset.Charset

private val log = loggerOf("ShapeFilex")

fun shapeHeaderOf(header: ShapefileHeader): ShapeHeader {
  val size = header.fileLength
  val version = header.version
  val shapeType = header.shapeType
  val bounds = BoundingBox.of(header.minX(), header.minY(), header.maxX(), header.maxY())

  return ShapeHeader(size, version, shapeType, bounds)
}

/** Shapefile 로부터 [ShapeRecord] 를 빌드합니다 */
fun shapeRecordOf(record: ShapefileReader.Record): ShapeRecord {
  return ShapeRecord(record.number,
                     record.type,
                     BoundingBox.of(record.minX, record.minY, record.maxX, record.maxY),
                     record.shape() as Geometry)
}

/**
 * Shpae 파일을 읽어 [Shape] 인스턴스를 빌드합니다.
 */
fun loadShape(file: File): Shape {
  val shpFiles = ShpFiles(file)

  val gf = GeometryFactory()
  var reader: ShapefileReader? = uninitialized()

  try {
    reader = ShapefileReader(shpFiles, false, false, gf)
    val header = loadShapeHeader(reader)
    val records = loadShapeRecords(reader)
    val attrs = loadShapeAttributes(shpFiles)
    return Shape(header, records, attrs)
  } finally {
    if (reader != null)
      reader.close()
  }
}

private fun loadShapeHeader(reader: ShapefileReader): ShapeHeader {
  return shapeHeaderOf(reader.header)
}

private fun loadShapeRecords(reader: ShapefileReader): FastList<ShapeRecord> {
  log.trace("Shape 파일에서 Shape 정보를 로드합니다...")

  val records = FastList.newList<ShapeRecord>()
  while (reader.hasNext()) {
    records.add(shapeRecordOf(reader.nextRecord()))
  }
  log.debug("로드된 Shape Record Count={}", records.size)

  return records
}

private fun loadShapeAttributes(shpFiles: ShpFiles, cs: Charset = Charsets.UTF_8): FastList<ShapeAttribute> {
  log.trace("Shape 파일의 특성정보를 로드합니다...")

  val reader = DbaseFileReader(shpFiles, false, cs)
  val attrs = FastList.newList<ShapeAttribute>()

  try {
    // Header 정보 로드
    val header = reader.header
    val fieldCount = header.numFields

    val headerNames = FastList.newList<String>().apply {
      (0 until fieldCount).forEach {
        add(header.getFieldName(it))
      }
    }
    // 레코드 정보 로드
    while (reader.hasNext()) {
      val entry = reader.readEntry()
      val attr = ShapeAttribute().apply {
        for (i in entry.indices) {
          addField(headerNames[i], entry[i])
        }
      }
      attrs.add(attr)
    }
    log.trace("로드된 특성정보:{}", attrs)
  } finally {
    reader.close()
  }
  return attrs
}

