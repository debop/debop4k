/*
 *  Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("Networkx")

package debop4k.core.utils

import debop4k.core.collections.padTo
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * 네트웍 관련 Object
 * @author debop sunghyouk.bae@gmail.com
 */
object Networkx {

  @JvmField val LOCALHOST_NAME: String = "localhost"
  @JvmField val LOCALHOST_IP: String = "127.0.0.1"
  @JvmField val DEFAULT_MASK: String = "255.255.255.0"
  @JvmField val INT_VALUE_127_0_0_1: Int = 0x7F000001

  val localhost: InetAddress get() = InetAddress.getLocalHost()

  @JvmStatic
  fun getLocalHostName(): String {
    try {
      return localhost.getHostName()
    } catch(uhe: UnknownHostException) {
      return uhe.message!!
    }
  }

  @JvmStatic
  fun isIpv4Address(ip: String): Boolean = ipToOptionInt(ip) != null

  /**
   * ip address 가 private 인지 여부
   * 10.*.*.*, 172.16.*.*, 192.168.*.* 인 경우가 private 이다
   */
  @JvmStatic
  fun isPrivateAddress(ip: InetAddress): Boolean {
    return when (ip) {
      is Inet4Address -> {
        val addr = ip.address
        if (addr[0] == 10.toByte()) true // 10/8
        else if (addr[0] == 172.toByte() && (addr[1].toInt() and 0xF0) == 16) true // 172/16
        else if (addr[0] == 192.toByte() && addr[1] == 168.toByte()) true // 192.168/16
        else false
      }
      else -> false
    }
  }

  /**
   * ip v4 문자열을 `Int`로 변환합니다.
   */
  @JvmStatic
  fun ipToInt(ip: String): Int {
    return ipToOptionInt(ip) ?: throw IllegalArgumentException("Invalid IPv4 address: $ip")
  }

  /**
   * ip v4 문자열을 `Option[Int]`로 변환합니다.
   */
  @JvmStatic
  fun ipToOptionInt(ip: String): Int? {
    val dot1 = ip.indexOf('.')
    if (dot1 <= 0) return null

    val dot2 = ip.indexOf('.', dot1 + 1)
    if (dot2 == -1) return null

    val dot3 = ip.indexOf('.', dot2 + 1)
    if (dot3 == -1) return null

    val num1 = ipv4DecimalToInt(ip.substring(0, dot1))
    if (num1 < 0) return null

    val num2 = ipv4DecimalToInt(ip.substring(dot1 + 1, dot2))
    if (num2 < 0) return null

    val num3 = ipv4DecimalToInt(ip.substring(dot2 + 1, dot3))
    if (num3 < 0) return null

    val num4 = ipv4DecimalToInt(ip.substring(dot3 + 1))
    if (num4 < 0) return null

    return (num1 shl 24) or (num2 shl 16) or (num3 shl 8) or num4
  }

  @JvmStatic
  private fun ipv4DecimalToInt(s: String): Int {
    if (s.isNullOrBlank() || s.length > 3) return -1

    var i = 0
    var num = 0
    while (i < s.length) {
      val c = s[i].toChar()
      if (c < '0' || c > '9') return -1
      num = (num * 10) + (c - '0')
      i += 1
    }
    return if (num >= 0 && num <= 255) num else -1
  }

  @JvmStatic
  fun inetAddressToInt(inetAddress: InetAddress): Int {
    when (inetAddress) {
      is Inet4Address -> {
        val addr = inetAddress.address
        return ((addr[0].toInt() and 0xFF) shl 24) or
            ((addr[1].toInt() and 0xFF) shl 16) or
            ((addr[2].toInt() and 0xFF) shl 8) or
            ((addr[3].toInt() and 0xFF))
      }
      else -> throw IllegalArgumentException("non-Inet4Address cannot be converted to an Int")
    }
  }

  /**
   * Converts either a full or partial ip, (e.g.127.0.0.1, 127.0)
   * to it's integer equivalent with mask specified by prefixlen.
   * Assume missing bits are 0s for a partial ip. Result returned as
   * (ip, netMask)
   */
  @JvmStatic
  fun ipToIpBlock(ip: String, prefixLen: Int?): Pair<Int, Int> {
    val arr: List<String> = ip.split('.')

    val pLen =
        if (arr.size != 4 && prefixLen == null) arr.size * 8
        else prefixLen!!

    val netIp = ipToInt(arr.padTo(4, "0").joinToString(separator = "."))
    val mask = (1 shl 31) shr (pLen - 1)

    return Pair(netIp, mask)
  }

  @JvmStatic
  fun cidrToIpBlock(cidr: String): Pair<Int, Int> {
    val arr = cidr.split('/')

    return when (arr.size) {
      1 -> ipToIpBlock(arr[0], null)
      2 -> ipToIpBlock(arr[0], arr[1].toInt())
      else -> throw IllegalArgumentException("Invalid cidr. cidr=$cidr")
    }
  }

  @JvmStatic
  fun isIpInBlock(ip: Int, ipBlock: Pair<Int, Int>): Boolean {
    return when (ipBlock) {
      is Pair<Int, Int> -> (ipBlock.second and ip) == ipBlock.first
      else -> false
    }
  }

  @JvmStatic
  fun isIpInBlocks(ip: Int, ipBlocks: Iterable<Pair<Int, Int>>): Boolean
      = ipBlocks.any { isIpInBlock(ip, it) }

  @JvmStatic
  fun isIpInBlocks(ip: String, ipBlocks: Iterable<Pair<Int, Int>>): Boolean
      = isIpInBlocks(ipToInt(ip), ipBlocks)

  @JvmStatic
  fun isInetAddressInBlocks(inetAddress: InetAddress, ipBlocks: Iterable<Pair<Int, Int>>): Boolean
      = isIpInBlocks(inetAddressToInt(inetAddress), ipBlocks)

}