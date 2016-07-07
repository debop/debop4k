package debop4k.core.asyncs

import org.joda.time.Duration
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author sunghyouk.bae@gmail.com
 */
object Asyncs {

  val DEFAULT_EXECUTOR: ExecutorService by lazy { Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2) }
  val EMPTY_RUNNABLE: Runnable = Runnable { }
  val duration: Duration = Duration.standardMinutes(15)

  fun <T> future(result: T, task: () -> Unit): Future<T> {
    return DEFAULT_EXECUTOR.submit(task, result)
  }

  fun <T> future(task: () -> T): Future<T> {
    return DEFAULT_EXECUTOR.submit(task)
  }

  fun <T> futureAll(calls: Collection<Callable<T>>): List<Future<T>> {
    return DEFAULT_EXECUTOR.invokeAll(calls)
  }
}

