/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
@file:JvmName("NetcdfExtensions")

package debop4k.science.netcdf

import ucar.nc2.NetcdfFile

val NETCDF_CAHCE_EXTENSIONS = arrayOf("gbx9", "ncx3", "idx")

fun openNetCdf(path: String): NetcdfFile = NetcdfFile.open(path)


/**
 * Net CDF 파일의 정보를 읽어드립니다.
 * Created by debop
 */
class NetCdfReader {

}