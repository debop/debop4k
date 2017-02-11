package debop4k.data.kundera.examples.model

import debop4k.core.uninitialized
import javax.persistence.*

/**
 * Person
 * @author sunghyouk.bae@gmail.com
 */
@Entity
@Table(name = "Person")
class Person {

  @Id
  @Column(name = "PersonId")
  var id: String? = uninitialized()

  @Column(name = "PersonName")
  var name: String? = uninitialized()

  @Column(name = "PersonAge")
  var age: Int? = uninitialized()

}