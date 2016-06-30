package debop4k.core.collections

import debop4k.core.AbstractCoreTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.test.fail

class CollectionExtensionsTest : AbstractCoreTest() {

  @Test fun convertToListOfGroupsWithoutConsumingGroup() {
    run {
      val listOfGroups: List<List<Int>> = (1 .. 10).asSequence().batch(2).toList()
      assertThat(listOfGroups).hasSize(5)
      assertThat(listOfGroups[0]).isEqualTo(listOf(1, 2))
      assertThat(listOfGroups[1]).isEqualTo(listOf(3, 4))
      assertThat(listOfGroups[2]).isEqualTo(listOf(5, 6))
      assertThat(listOfGroups[3]).isEqualTo(listOf(7, 8))
      assertThat(listOfGroups[4]).isEqualTo(listOf(9, 10))
    }
    run {
      val listOfGroups: List<List<Int>> = (1 .. 10).asIterable().batch(3).toList()
      assertThat(listOfGroups).hasSize(4)
      assertThat(listOfGroups[0]).isEqualTo(listOf(1, 2, 3))
      assertThat(listOfGroups[1]).isEqualTo(listOf(4, 5, 6))
      assertThat(listOfGroups[2]).isEqualTo(listOf(7, 8, 9))
      assertThat(listOfGroups[3]).isEqualTo(listOf(10))
    }
  }

  @Test fun lazyBatchSequenceWithoutConsumingGroup() {
    run {
      var count = 0
      val listOfGroups = (1 .. 10).lazyBatch(2) { group: Sequence<Int> ->
        count++
        if (count == 5) {
          assertThat(group.toList()).isEqualTo(listOf(9, 10))
        }
      }
      assertThat(count).isEqualTo(5)
    }

    run {
      var count = 0
      val listOfGroups = (1 .. 10).lazyBatch(3) { group: Sequence<Int> ->
        count++
        if (count == 4) {
          assertThat(group.toList()).isEqualTo(listOf(10))
        }
      }
      assertThat(count).isEqualTo(4)
    }
  }

  @Test fun lazyBatchSequenceValidatingGroups() {
    val expectedGroups = fastListOf(intArrayListOf(1, 2),
                                    intArrayListOf(3, 4),
                                    intArrayListOf(5, 6),
                                    intArrayListOf(7, 8),
                                    intArrayListOf(9, 10))
    run {
      var count = 0
      val listOfGroups = (1 .. 10).lazyBatch(2) { group: Sequence<Int> ->
        val groupAsIntArrayList = group.toIntArrayList()
        assertThat(groupAsIntArrayList).isEqualTo(expectedGroups[count++])
      }
    }

    run {
      var count = 0
      val listOfGroups = (1 .. 10).lazyBatch(2) { group: Sequence<Int> ->
        val groupAsIntArrayList = group.toIntArrayList()
        assertThat(groupAsIntArrayList).isEqualTo(expectedGroups[count++])
      }
    }
  }

  @Test fun specificCase() {
    run {
      val listOfGroups: List<List<Int>> = (1 .. 10).batch(3).toList()
      assertThat(listOfGroups).hasSize(4)
      assertThat(listOfGroups[0]).isEqualTo(listOf(1, 2, 3))
      assertThat(listOfGroups[1]).isEqualTo(listOf(4, 5, 6))
      assertThat(listOfGroups[2]).isEqualTo(listOf(7, 8, 9))
      assertThat(listOfGroups[3]).isEqualTo(listOf(10))
    }
    run {
      val listOfGroups: List<List<Int>> = (1 .. 10).batch(3).toList()
      assertThat(listOfGroups).hasSize(4)
      assertThat(listOfGroups[0]).isEqualTo(listOf(1, 2, 3))
      assertThat(listOfGroups[1]).isEqualTo(listOf(4, 5, 6))
      assertThat(listOfGroups[2]).isEqualTo(listOf(7, 8, 9))
      assertThat(listOfGroups[3]).isEqualTo(listOf(10))
    }
  }

  private fun testRunAsStream(testList: List<Int>, batchSize: Int, expectedGroups: Int) {
    run {
      var groupCount = 0
      var items = arrayListOf<Int>()

      testList.asSequence().batch(batchSize).forEach { group ->
        groupCount++
        group.forEach { item -> items.add(item) }
      }
      assertThat(groupCount).isEqualTo(expectedGroups)
      assertThat(testList).isEqualTo(items)
    }
    run {
      var groupCount = 0
      var items = arrayListOf<Int>()

      testList.batch(batchSize).forEach { group ->
        groupCount++
        group.forEach { item -> items.add(item) }
      }
      assertThat(groupCount).isEqualTo(expectedGroups)
      assertThat(testList).isEqualTo(items)
    }
  }

  @Test fun groupsOfExactSize() {
    testRunAsStream((1 .. 15).toList(), 5, 3)
  }

  @Test fun groupsOfOddSize() {
    testRunAsStream((1 .. 18).toList(), 5, 4)
  }

  @Test fun groupsOfLessThanBatchSize() {
    testRunAsStream(listOf(1, 2, 3), 5, 1)
  }

  @Test fun groupsOfSize1() {
    testRunAsStream(listOf(1, 2, 3), 1, 3)
  }

  @Test fun groupOfSize0() {
    val testList = listOf(1, 2, 3)

    assertThat(testList.asSequence().batch(0).toList().size).isZero()
    assertThat(testList.batch(0).toList().size).isZero()
    assertThat(testList.asSequence().batch(-1).toList().size).isZero()
    assertThat(testList.batch(-1).toList().size).isZero()
  }

  @Test fun emptySequence() {
    listOf<Int>().asSequence().batch(1).forEach { group -> fail() }

    listOf<Int>().batch(1).forEach { group -> fail() }
  }
}