package debop4k.timeperiod.timelines

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.timeperiod.ITimePeriod
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
class TimeLineMomentCollection : AbstractValueObject(), ITimeLineMomentCollection {

  private val _moments = FastList.newList<ITimeLineMoment>()

  override val size: Int
    get() = _moments.size

  override val isEmpty: Boolean
    get() = _moments.isEmpty

  override fun min(): ITimeLineMoment = _moments.min()

  override fun max(): ITimeLineMoment = _moments.max()

  override fun get(index: Int): ITimeLineMoment {
    return _moments.get(index)
  }

  protected fun addPeriod(moment: DateTime, period: ITimePeriod): Unit {
    var item = find(moment)
    if (item == null) {
      item = TimeLineMoment(moment)
      _moments.add(item)

      _moments.sortThis()
    }
    item.periods.add(period)
  }

  protected fun removePeriod(moment: DateTime, period: ITimePeriod): Unit {
    val item = find(moment)
    if (item != null && item.periods.contains(period)) {
      item.periods.remove(period)
      if (item.periods.isEmpty()) {
        _moments.remove(item)
      }
    }
  }

  override fun add(period: ITimePeriod?) {
    if (period != null) {
      addPeriod(period.start, period)
      addPeriod(period.end, period)
    }
  }

  override fun addAll(periods: Collection<ITimePeriod?>) {
    periods.filter { it != null }.forEach { add(it) }
  }

  override fun remove(period: ITimePeriod?) {
    if (period != null) {
      removePeriod(period.start, period)
      removePeriod(period.end, period)
    }
  }

  override fun find(moment: DateTime): ITimeLineMoment? {
    return _moments.find { it.moment == moment }
  }

  override fun contains(moment: DateTime): Boolean {
    return _moments.anySatisfy { it.moment == moment }
  }

  override fun iterator(): Iterator<ITimeLineMoment> {
    return _moments.iterator()
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("moments", _moments)
  }
}