package debop4k.timeperiod.calendars

/**
 * Created by debop
 */
enum class SeekDirection(val value: Int) {

  /** 미래로 (시간 값을 증가 시키는 방향) */
  Forward(1),

  /** 과거로 (시간 값을 감소 시키는 방향) */
  Backward(-1);

  companion object {
    fun of(value: Int): SeekDirection {
      return if (value > 0) Forward else Backward
    }
  }
}