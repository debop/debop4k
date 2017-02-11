package debop4k.data.kundera.examples.model.collection

import com.google.common.collect.Sets
import com.impetus.kundera.index.Index
import com.impetus.kundera.index.IndexCollection
import debop4k.core.uninitialized
import javax.persistence.*

/**
 * BlogPost
 * @author debop
 * @since 2017. 2. 12.
 */
@Entity
@Table(name = "blog_posts_kotlin")
@IndexCollection(columns = arrayOf(Index(name = "body"), Index(name = "tags")))
class BlogPost {

  @Id
  @Column(name = "post_id")
  var postId: Int? = uninitialized()

  @Column(name = "body")
  var body: String? = uninitialized()

  @Column(name = "tags")
  var tags: java.util.HashSet<String>? = Sets.newHashSet()

  fun addTag(tag: String) {
    tags?.add(tag)
  }
}