package debop4k.data.kundera.examples.cassandra

import debop4k.data.kundera.examples.AbstractKunderaTest
import debop4k.data.kundera.examples.model.Person
import org.junit.Test
import javax.persistence.Persistence

/**
 * CassandraTest
 * @author sunghyouk.bae@gmail.com
 */
class CassandraTest : AbstractKunderaTest() {

  @Test
  fun testEmf() {

    val p = Person().apply {
      id = "2"
      name = "name"
      age = 23
    }

    val emf = Persistence.createEntityManagerFactory("cassandra")
    val em = emf.createEntityManager()

    val tx = em.transaction
    tx.begin()
    em.persist(p)
    tx.commit()

    em.clear()

    val p2 = em.createQuery("select p from Person p", Person::class.java).singleResult
    println("name=${p2.name}")

    em.close()

  }
}