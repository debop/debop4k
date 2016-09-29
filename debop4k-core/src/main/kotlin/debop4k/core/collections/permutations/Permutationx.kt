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

package debop4k.core.collections.permutations


val NIL: Permutation<Any?> get() = Nil.instance()

fun <E> emptyPermutation(): Permutation<E> = Nil.instance()

fun <E> permutationOf(element: E): Permutation<E>
    = cons(element, emptyPermutation())

fun <E> permutationOf(element: E, tailFunc: () -> Permutation<E>): Permutation<E>
    = cons(element, tailFunc)

fun <E> permutationOf(element1: E, element2: E): Permutation<E>
    = cons(element1, permutationOf(element2))

fun <E> permutationOf(element1: E, element2: E, tailFunc: () -> Permutation<E>): Permutation<E>
    = cons(element1, permutationOf(element2, tailFunc))

fun <E> permutationOf(element1: E, element2: E, element3: E): Permutation<E>
    = cons(element1, permutationOf(element2, element3))

fun <E> permutationOf(element1: E, element2: E, element3: E, tailFunc: () -> Permutation<E>): Permutation<E>
    = cons(element1, permutationOf(element2, element3, tailFunc))

fun <E> permutationOf(vararg elements: E): Permutation<E> = permutationOf(elements.iterator())
fun <E> permutationOf(elements: Iterable<E>): Permutation<E> = permutationOf(elements.iterator())
fun <E> permutationOf(iterator: Iterator<E>): Permutation<E> {
  return if (iterator.hasNext()) {
    cons(iterator.next(), { permutationOf(iterator) })
  } else {
    emptyPermutation()
  }
}

fun <E> Iterator<E>.toPermutation(): Permutation<E> = permutationOf(this)
fun <E> Iterable<E>.toPermutation(): Permutation<E> = permutationOf(this.iterator())
fun <E> Sequence<E>.toPermutation(): Permutation<E> = permutationOf(this.iterator())

fun <E> concat(elements: Iterable<E>, tailFunc: () -> Permutation<E>): Permutation<E> {
  return concat(elements.iterator(), tailFunc)
}

fun <E> concat(elements: Iterable<E>, tail: Permutation<E>): Permutation<E> {
  return concat(elements.iterator(), tail)
}

fun <E> concat(iterator: Iterator<E>, tailFunc: () -> Permutation<E>): Permutation<E> {
  return if (iterator.hasNext()) {
    concatNonEmptyIterator(iterator, tailFunc)
  } else {
    tailFunc()
  }
}

fun <E> concat(iterator: Iterator<E>, tail: Permutation<E>): Permutation<E> {
  return if (iterator.hasNext()) {
    concatNonEmptyIterator(iterator, tail)
  } else {
    tail
  }
}

private fun <E> concatNonEmptyIterator(iterator: Iterator<E>, tail: Permutation<E>): Permutation<E> {
  val next = iterator.next()
  return if (iterator.hasNext()) {
    cons(next, concatNonEmptyIterator(iterator, tail))
  } else {
    cons(next, tail)
  }
}

private fun <E> concatNonEmptyIterator(iterator: Iterator<E>, tailFunc: () -> Permutation<E>): Permutation<E> {
  val next = iterator.next()
  return if (iterator.hasNext()) {
    cons(next, concatNonEmptyIterator(iterator, tailFunc))
  } else {
    cons(next, tailFunc)
  }
}


fun <E> cons(head: E, tailFunc: () -> Permutation<E>): Permutation<E> {
  return Cons(head, tailFunc)
}

fun <E> cons(head: E, tail: Permutation<E>): Permutation<E> {
  return FixedCons(head, tail)
}

fun <E> iterate(initial: E, func: (E) -> E): Permutation<E> {
  return Cons(initial, { iterate(func.invoke(initial), func) })
}

fun <E> tabulate(start: Int, generator: (Int) -> E): Permutation<E> {
  return cons(generator(start)) { tabulate(start + 1, generator) }
}

fun <E> continually(generator: () -> E): Permutation<E> {
  return cons(generator.invoke()) { continually(generator) }
}

fun <E> continually(cycle: Iterable<E>): Permutation<E> {
  return if (!cycle.iterator().hasNext()) {
    emptyPermutation()
  } else {
    continuallyUnsafe(cycle)
  }
}

fun <E> continuallyUnsafe(cycle: Iterable<E>): Permutation<E> = concat(cycle) { continually(cycle) }
fun <E> continually(value: E): Permutation<E> = cons(value) { continually(value) }

@JvmOverloads
fun numbers(start: Int, step: Int = 1): Permutation<Int>
    = cons(start) { numbers(start + step, step) }

@JvmOverloads
fun numbers(start: Long, step: Long = 1L): Permutation<Long>
    = cons(start) { numbers(start + step, step) }

@JvmOverloads
fun numbers(start: Float, step: Float = 1.0F): Permutation<Float>
    = cons(start) { numbers(start + step, step) }

@JvmOverloads
fun numbers(start: Double, step: Double = 1.0): Permutation<Double>
    = cons(start) { numbers(start + step, step) }
