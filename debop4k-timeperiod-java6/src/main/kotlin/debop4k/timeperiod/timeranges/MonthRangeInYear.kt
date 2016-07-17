package debop4k.timeperiod.timeranges

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper

/**
 * 1년 내의 Month 단위의 기간을 표현합니다.
 * Created by debop
 */
open class MonthRangeInYear(val startMonthOfYear: Int = 1,
                            val endMonthOfYear: Int = 12) : AbstractValueObject(), Comparable<MonthRangeInYear> {

  init {
    require(startMonthOfYear >= 1)
    require(endMonthOfYear <= 12)
    require(startMonthOfYear <= endMonthOfYear)
  }

  val isSingleMonth: Boolean
    get() = startMonthOfYear == endMonthOfYear

  fun hasInside(monthOfYear: Int): Boolean {
    return startMonthOfYear <= monthOfYear && monthOfYear <= endMonthOfYear
  }

  override fun compareTo(other: MonthRangeInYear): Int {
    return hashCode() - other.hashCode()
  }

  override fun hashCode(): Int {
    return startMonthOfYear * 1000 + endMonthOfYear
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("startMonthOfYear", startMonthOfYear)
        .add("endMonthOfYear", endMonthOfYear)
  }
}