/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

package debop4k.core.utils

import debop4k.core.EMPTY_STRING
import debop4k.core.unique
import java.util.*
import java.util.regex.*

const val ASCII_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

/**
 * Hashids developed to generate short hashes from numbers (like YouTube).
 * Can be used as forgotten password hashes, invitation codes, store shard numbers.
 * This is implementation of http://hashids.org
 *
 * @author debop sunghyouk.bae@gmail.com
 */
class Hashids(salt: String = "", length: Int = 0, alphabet: String = ASCII_CHARS) {

  private val min: Int = 16
  private val sepsDiv: Double = 3.5
  private val guardDiv: Int = 12

  private var seps: String = "cfhistuCFHISTU"
  private var guards: String? = null

  private var salt: String
  private var length: Int
  private var alphabet: String

  init {
    this.salt = salt
    this.length = if (length > 0) length else 0
    this.alphabet = alphabet.unique()

    if (this.alphabet.length < min) {
      throw IllegalArgumentException("Alphabet must contains at least $min unique characters")
    }

    // seps should contains only characters present in alphabet;
    // alphabet should not contains seps
//    val sepsLength = seps.length - 1
    for ((index, ch) in seps.withIndex()) {
      val position = this.alphabet.indexOf(ch)
      if (position == -1) {
        seps = seps.substring(0, index) + " " + seps.substring(index + 1)
      } else {
        this.alphabet = this.alphabet.substring(0, position) + " " + this.alphabet.substring(index + 1)
      }
    }

    this.alphabet = this.alphabet.replace("\\s+", "")
    seps = seps.replace("\\s+", "")

    seps = consistentShuffle(seps, this.salt)

    if ((seps.isEmpty()) || ((this.alphabet.length / seps.length) > sepsDiv)) {
      var sepsCount = getCount(this.alphabet.length, sepsDiv)

      if (sepsCount == 1) sepsCount++

      if (sepsCount > seps.length) {
        val diff = sepsCount - seps.length
        seps += this.alphabet.substring(0, diff)
        this.alphabet = this.alphabet.substring(diff)
      } else {
        seps = seps.substring(0, sepsCount)
      }
    }

    this.alphabet = this.consistentShuffle(this.alphabet, this.salt)

    val guardCount = getCount(this.alphabet.length, guardDiv)

    if (this.alphabet.length < 3) {
      guards = seps.substring(0, guardCount)
      seps = seps.substring(guardCount)
    } else {
      guards = this.alphabet.substring(0, guardCount)
      this.alphabet = this.alphabet.substring(guardCount)
    }
  }


  /**
   * Encrypt numbers to string
   * @param numbers the numbers to encrypt
   * @return The encrypt string
   */
  fun encode(vararg numbers: Long): String {
    if (numbers.size == 0)
      return EMPTY_STRING

    numbers.find { it > 9007199254740992L }?.let {
      throw IllegalArgumentException("Number can not be greater than 9007199254740992L")
    }

    var numberHashInt: Int = 0
    for (i in numbers.indices)
      numberHashInt += (numbers[i] % (i + 100)).toInt()

    var alphabet = this.alphabet
    val retInt = alphabet.toCharArray()[numberHashInt % alphabet.length]

    var num: Long
    var sepsIndex: Int
    var guardIndex: Int
    var buffer: String
    var retString = retInt + ""
    var guard: Char

    for (i in numbers.indices) {
      num = numbers[i]
      buffer = retInt + salt + alphabet

      alphabet = consistentShuffle(alphabet, buffer.substring(0, alphabet.length))
      val last = hash(num, alphabet)

      retString += last

      if (i + 1 < numbers.size) {
        num %= (last.toCharArray()[0] + i).toLong()
        sepsIndex = (num % seps.length).toInt()
        retString += seps.toCharArray()[sepsIndex]
      }
    }

    if (retString.length < length) {
      guardIndex = (numberHashInt + retString.toCharArray()[0].toInt()) % guards!!.length
      guard = guards!!.toCharArray()[guardIndex]

      retString = guard + retString

      if (retString.length < length) {
        guardIndex = (numberHashInt + retString.toCharArray()[2].toInt()) % guards!!.length
        guard = guards!!.toCharArray()[guardIndex]

        retString += guard
      }
    }

    val halfLength = alphabet.length / 2
    while (retString.length < length) {
      alphabet = consistentShuffle(alphabet, alphabet)
      retString = alphabet.substring(halfLength) + retString + alphabet.substring(0, halfLength)
      val excess = retString.length - length
      if (excess > 0) {
        val position = excess / 2
        retString = retString.substring(position, position + length)
      }
    }

    return retString
  }

  /**
   * Decrypt string to numbers
   *
   * @param hash the encrypt string
   * @return Decrypted numbers
   */
  fun decode(hash: String): LongArray {
    if (hash == "")
      return longArrayOf()

    var alphabet = alphabet
    val retArray = ArrayList<Long>()

    var i = 0
    val regexp = "[$guards]"
    var hashBreakdown = hash.replace(regexp, " ")
    var hashArray = hashBreakdown.split(" ")

    if (hashArray.size == 3 || hashArray.size == 2) {
      i = 1
    }

    hashBreakdown = hashArray[i]

    val lottery = hashBreakdown.toCharArray()[0]

    hashBreakdown = hashBreakdown.substring(1)
    hashBreakdown = hashBreakdown.replace("[$seps]", " ")
    hashArray = hashBreakdown.split(" ")

//    var buffer: String
    for (subHash in hashArray) {
      val buffer = lottery + salt + alphabet
      alphabet = consistentShuffle(alphabet, buffer.substring(0, alphabet.length))
      retArray.add(unhash(subHash, alphabet))
    }

    var arr = LongArray(retArray.size)
    for (index in retArray.indices) {
      arr[index] = retArray.get(index)
    }

    if (encode(*arr) != hash) {
      arr = LongArray(0)
    }

    return arr
  }

  /**
   * Encrypt hexa to string
   *
   * @param hexa the hexa to encrypt
   * @return The encrypt string
   */
  fun encodeHex(hexa: String): String {
    if (!hexa.matches(Regex.fromLiteral("^[0-9a-fA-F]+$")))
      return EMPTY_STRING

    val matched = ArrayList<Long>()
    val matcher = Pattern.compile("[\\w\\W]{1,12}").matcher(hexa)

    while (matcher.find())
      matched.add(java.lang.Long.parseLong("1" + matcher.group(), 16))

    val result = LongArray(matched.size)
    for (i in matched.indices) result[i] = matched.get(i)

    return encode(*result)
  }

  /**
   * Decrypt string to numbers
   *
   * @param hash the encrypt string
   * @return Decrypted numbers
   */
  fun decodeHex(hash: String): String {
    val sb = StringBuilder()
    val numbers = decode(hash)
    for (number in numbers) {
      sb.append(java.lang.Long.toHexString(number).substring(1))
    }
    return sb.toString()
  }

  private fun getCount(length: Int, div: Double): Int = Math.ceil(length.toDouble() / div).toInt()
  private fun getCount(length: Int, div: Int): Int = getCount(length, div.toDouble())

  private fun consistentShuffle(alphabet: String, salt: String): String {
    if (salt.length <= 0)
      return alphabet

    var shuffled = alphabet

    val saltArray = salt.toCharArray()
    val saltLength = salt.length

    var i = shuffled.length - 1
    var v = 0
    var p = 0

    while (i > 0) {
      v %= saltLength
      val integer = saltArray[v].toInt()
      p += integer
      val j = (integer + v + p) % i

      val temp = shuffled[j]
      shuffled = shuffled.substring(0, j) + shuffled[i] + shuffled.substring(j + 1)
      shuffled = shuffled.substring(0, i) + temp + shuffled.substring(i + 1)

      i--
      v++
    }

    return shuffled
  }

  private fun hash(input: Long, alphabet: String): String {
    var current = input
    var hash = ""
    val length = alphabet.length
    val array = alphabet.toCharArray()

    do {
      hash = array[(current % length.toLong()).toInt()] + hash
      current /= length
    } while (current > 0)

    return hash
  }

  private fun unhash(input: String, alphabet: String): Long {
    var number: Long = 0
    val inputArray = input.toCharArray()
    val length = input.length - 1

    for (i in 0..length) {
      val position = alphabet.indexOf(inputArray[i]).toLong()
      number += (position.toDouble() * Math.pow(alphabet.length.toDouble(), (input.length - i - 1).toDouble())).toLong()
    }

    return number
  }

}