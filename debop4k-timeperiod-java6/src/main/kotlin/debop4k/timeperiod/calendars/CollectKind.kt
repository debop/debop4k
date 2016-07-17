package debop4k.timeperiod.calendars

/**
 * Created by debop
 */
enum class CollectKind {

  Year, Month, Day, Hour, Minute;

  val value: Int get() = ordinal

  companion object {
    fun of(value: Int): CollectKind {
      return values()[value]
    }
  }
}