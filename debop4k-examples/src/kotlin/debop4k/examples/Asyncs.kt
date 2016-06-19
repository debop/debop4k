/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
@file:JvmName("asyncs")

package debop4k.examples

import java.util.concurrent.*

public fun Executor.execute(action: () -> Unit) {
  execute { action() }
}

public fun Executor.executeAll(actions: List<() -> Unit>) {
  actions.forEach { action -> execute { action() } }
}

public fun Executor.invoke(action: () -> Unit) {
  execute(run { action })
}