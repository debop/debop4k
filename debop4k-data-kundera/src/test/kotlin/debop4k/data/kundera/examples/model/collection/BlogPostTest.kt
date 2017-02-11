package debop4k.data.kundera.examples.model.collection

import debop4k.core.uninitialized
import debop4k.data.kundera.examples.AbstractKunderaTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import javax.persistence.*

/**
 * BlogPostTest
 * @author debop
 * @since 2017. 2. 12.
 */
class BlogPostTest : AbstractKunderaTest() {

  var emf: EntityManagerFactory = uninitialized()
  var em: EntityManager = uninitialized()

  @Before
  fun setup() {
    emf = Persistence.createEntityManagerFactory("cassandra")
    em = emf.createEntityManager()
  }

  @After
  fun cleanup() {
    em.close()
    emf.close()
  }

  @Test
  fun saveBlogPost() {
    val post = BlogPost().apply {
      postId = 1
      body = "kundera with cassandra"
      addTag("nosql")
      addTag("cassandra")
      addTag("kundera")
    }

    em.transaction.begin()
    em.persist(post)
    em.transaction.commit()

    em.clear()
    val post1 = em.find(BlogPost::class.java, 1)
    assertThat(post1).isNotNull()
    assertThat(post1.tags).contains("nosql", "cassandra")

    val q = em.createQuery("select p from BlogPost p")
    val allPosts = q.resultList as List<BlogPost>
    assertThat(allPosts).isNotNull
    assertThat(allPosts.size).isGreaterThan(0)
    allPosts.forEach { println("post=$it, tags=${it.tags}") }

    val qt = em.createQuery("select p from BlogPost p where p.tags = :tags")
    qt.setParameter("tags", "nosql")
    val posts = qt.resultList
    assertThat(posts).isNotNull
    assertThat(posts).hasSize(1)
    posts.forEach { println("post=$it") }
  }
}