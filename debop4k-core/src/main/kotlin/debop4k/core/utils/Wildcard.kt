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

package debop4k.core.utils

/**
 * Checks whether a string or path matches a given wildcard pattern.
 * Possible patterns allow to match single characters ('?') or any count of
 * characters ('*'). Wildcard characters can be escaped (by an '\').
 * When matching path, deep tree wildcard also can be used ('**').
 * <p>
 * This method uses recursive matching, as in linux or windows. regexp works the same.
 * This method is very fast, comparing to similar implementations.
 */
object Wildcard {

  /**
   * Checks whether a string matches a given wildcard pattern.
   * @param string  input string
   * @param pattern  pattern to match
   * @return      `true` if string matches the pattern, otherwise `false`
   */
  @JvmStatic
  fun match(string: CharSequence, pattern: CharSequence): Boolean {
    return match(string, pattern, 0, 0)
  }

  /**
   * Checks if two strings are equals or if they [.match].
   * Useful for cases when matching a lot of equal strings and speed is important.
   */
  fun equalsOrMatch(string: CharSequence, pattern: CharSequence): Boolean {
    if (string == pattern) {
      return true
    }
    return match(string, pattern, 0, 0)
  }

  /**
   * Internal matching recursive function.
   */
  private fun match(string: CharSequence, pattern: CharSequence, sNdx: Int = 0, pNdx: Int = 0): Boolean {
    var sNdx = sNdx
    var pNdx = pNdx
    val pLen = pattern.length
    if (pLen == 1) {
      if (pattern[0] == '*') {     // speed-up
        return true
      }
    }
    val sLen = string.length
    var nextIsNotWildcard = false

    while (true) {

      // check if end of string and/or pattern occurred
      if (sNdx >= sLen) {    // end of string still may have pending '*' in pattern
        while (pNdx < pLen && pattern[pNdx] == '*') {
          pNdx++
        }
        return pNdx >= pLen
      }
      if (pNdx >= pLen) {          // end of pattern, but not end of the string
        return false
      }
      val p = pattern[pNdx]    // pattern char

      // perform logic
      if (!nextIsNotWildcard) {

        if (p == '\\') {
          pNdx++
          nextIsNotWildcard = true
          continue
        }
        if (p == '?') {
          sNdx++
          pNdx++
          continue
        }
        if (p == '*') {
          var pNext: Char = 0.toChar()            // next pattern char
          if (pNdx + 1 < pLen) {
            pNext = pattern[pNdx + 1]
          }
          if (pNext == '*') {          // double '*' have the same effect as one '*'
            pNdx++
            continue
          }
          var i: Int
          pNdx++

          // find recursively if there is any substring from the end of the
          // line that matches the rest of the pattern !!!
          i = string.length
          while (i >= sNdx) {
            if (match(string, pattern, i, pNdx)) {
              return true
            }
            i--
          }
          return false
        }
      } else {
        nextIsNotWildcard = false
      }

      // check if pattern char and string char are equals
      if (p != string[sNdx]) {
        return false
      }

      // everything matches for now, continue
      sNdx++
      pNdx++
    }
  }


  // ---------------------------------------------------------------- utilities

  /**
   * Matches string to at least one pattern.
   * Returns index of matched pattern, or `-1` otherwise.
   * @see .match
   */
  fun matchOne(src: String, patterns: List<String>): Int {
    for (i in patterns.indices) {
      if (match(src, patterns[i])) {
        return i
      }
    }
    return -1
  }

  /**
   * Matches path to at least one pattern.
   * Returns index of matched pattern or `-1` otherwise.
   * @see .matchPath
   */
  fun matchPathOne(path: String, patterns: List<String>): Int {
    for (i in patterns.indices) {
      if (matchPath(path, patterns[i])) {
        return i
      }
    }
    return -1
  }

  // ---------------------------------------------------------------- path

  private val PATH_MATCH = "**"
  private val PATH_SEPARATORS = "/\\"

  /**
   * Matches path against pattern using *, ? and ** wildcards.
   * Both path and the pattern are tokenized on path separators (both \ and /).
   * '**' represents deep tree wildcard, as in Ant.
   */
  fun matchPath(path: String, pattern: String): Boolean {
    val pathElements = path.splits(PATH_SEPARATORS)
    val patternElements = pattern.splits(PATH_SEPARATORS)
    return matchTokens(pathElements, patternElements)
  }

  /**
   * Match tokenized string and pattern.
   */
  private fun matchTokens(tokens: List<String>, patterns: List<String>): Boolean {
    var patNdxStart = 0
    var patNdxEnd = patterns.size - 1
    var tokNdxStart = 0
    var tokNdxEnd = tokens.size - 1

    while (patNdxStart <= patNdxEnd && tokNdxStart <= tokNdxEnd) {  // find first **
      val patDir = patterns[patNdxStart]
      if (patDir == PATH_MATCH) {
        break
      }
      if (!match(tokens[tokNdxStart], patDir)) {
        return false
      }
      patNdxStart++
      tokNdxStart++
    }
    if (tokNdxStart > tokNdxEnd) {
      for (i in patNdxStart..patNdxEnd) {  // string is finished
        if (patterns[i] != PATH_MATCH) {
          return false
        }
      }
      return true
    }
    if (patNdxStart > patNdxEnd) {
      return false  // string is not finished, but pattern is
    }

    while (patNdxStart <= patNdxEnd && tokNdxStart <= tokNdxEnd) {  // to the last **
      val patDir = patterns[patNdxEnd]
      if (patDir == PATH_MATCH) {
        break
      }
      if (!match(tokens[tokNdxEnd], patDir)) {
        return false
      }
      patNdxEnd--
      tokNdxEnd--
    }
    if (tokNdxStart > tokNdxEnd) {
      for (i in patNdxStart..patNdxEnd) {  // string is finished
        if (patterns[i] != PATH_MATCH) {
          return false
        }
      }
      return true
    }

    while (patNdxStart != patNdxEnd && tokNdxStart <= tokNdxEnd) {
      var patIdxTmp = -1
      for (i in patNdxStart + 1..patNdxEnd) {
        if (patterns[i] == PATH_MATCH) {
          patIdxTmp = i
          break
        }
      }
      if (patIdxTmp == patNdxStart + 1) {
        patNdxStart++  // skip **/** situation
        continue
      }
      // find the pattern between padIdxStart & padIdxTmp in str between strIdxStart & strIdxEnd
      val patLength = patIdxTmp - patNdxStart - 1
      val strLength = tokNdxEnd - tokNdxStart + 1
      var ndx = -1
      strLoop@ for (i in 0..strLength - patLength) {
        for (j in 0..patLength - 1) {
          val subPat = patterns[patNdxStart + j + 1]
          val subStr = tokens[tokNdxStart + i + j]
          if (!match(subStr, subPat)) {
            continue@strLoop
          }
        }

        ndx = tokNdxStart + i
        break
      }

      if (ndx == -1) {
        return false
      }

      patNdxStart = patIdxTmp
      tokNdxStart = ndx + patLength
    }

    for (i in patNdxStart..patNdxEnd) {
      if (patterns[i] != PATH_MATCH) {
        return false
      }
    }

    return true
  }
}