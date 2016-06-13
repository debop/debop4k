package debop4k.examples.operators

import io.kotlintest.matchers.be
import io.kotlintest.specs.FunSpec
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.reflect.KProperty

/**
 * Delegated Properties : 속성 접근 로직을 재사용하기
 */
class DelegateExample : FunSpec() {

  init {

    test("Lazy Initialization and by backed field") {

      val p = Person("debop")
      p.emails should be != null
      p.emails should be != null
      p.emails should be != null
    }

    test("Lazy Initialization and by lazy()") {

      val p2 = Person2("debop")
      p2.emails should be != null
      p2.emails should be != null
      p2.emails should be != null
    }


    test("Person 3 : Delegate with PropertyChangeSupport") {
      val p = Person3("Debop", 49, 1200)
      p.addPropertyChangeListener(PropertyChangeListener { event ->
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
      })

      p.age = 50
      p.salary = 1500
    }

    test("Person 4 : Delegate with ObservableProperty") {
      val p = Person4("Debop", 49, 1200)
      p.addPropertyChangeListener(PropertyChangeListener { event ->
        println("Property with Observable ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
      })

      p.age = 50
      p.salary = 1500
    }

    test("Person5 : with Reflect") {
      val p = Person5("debop", 55, 5500)
      p.addPropertyChangeListener(PropertyChangeListener { event ->
        println("Property with Reflect ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
      })
      p.age = 555
      p.salary = 323423432
    }

    test("Store Property Values in a map") {

      class Person {
        private val _attrs = hashMapOf<String, String>()

        fun setAttribute(name: String, value: String) {
          _attrs[name] = value
        }

        fun setAttribute(map: Map<String, String>) {
          _attrs.putAll(map)
        }

        val name: String
          get() = _attrs["name"]!!
      }

      val p = Person()
      p.setAttribute(mapOf("name" to "Debop", "company" to "KESTI"))

      p.name shouldBe "Debop"
    }
  }
}

data class Email(val address: String)

class Person(val name: String) {

  @Volatile private var _emails: List<Email>? = null

  val emails: List<Email>
    get() {
      if (_emails == null) {
        _emails = loadEmails(this)
      }
      return _emails!!
    }
}

fun loadEmails(person: Person): List<Email> {
  println("Load emails for ${person.name}")
  return listOf(Email("a"), Email("b"))
}

class Person2(val name: String) {
  val emails: List<Email> by lazy { loadEmails2(this) }
}

fun loadEmails2(person: Person2): List<Email> {
  println("Load emails for ${person.name}")
  return listOf(Email("a"), Email("b"))
}

open class PropertyChangeAware {

  protected val changeSupport = PropertyChangeSupport(this)

  fun addPropertyChangeListener(listener: PropertyChangeListener) {
    changeSupport.addPropertyChangeListener(listener)
  }

  fun removePropertyChangeListener(listener: PropertyChangeListener) {
    changeSupport.removePropertyChangeListener(listener)
  }
}

class Person3(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

  var age: Int = age
    set(newValue) {
      val oldValue = field
      field = newValue
      changeSupport.firePropertyChange("age", oldValue, newValue)
    }

  var salary: Int = salary
    set(newValue) {
      val oldValue = field
      field = newValue
      changeSupport.firePropertyChange("salary", oldValue, newValue)
    }
}

class ObservableProperty(val propName: String, var propValue: Int, val changeSupport: PropertyChangeSupport) {
  fun getValue(): Int = propValue
  fun setValue(newValue: Int) {
    val oldValue = propValue
    propValue = newValue
    changeSupport.firePropertyChange(propName, oldValue, newValue)
  }
}

class Person4(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

  val _age = ObservableProperty("age", age, changeSupport)
  val _salary = ObservableProperty("salary", salary, changeSupport)

  var age: Int
    get() = _age.propValue
    set(value) {
      _age.setValue(value)
    }

  var salary: Int
    get() = _salary.propValue
    set(value) {
      _salary.setValue(value)
    }
}

class ObservableProperty2(var propValue: Int, val changeSupport: PropertyChangeSupport) {

  operator fun getValue(p: Person5, prop: KProperty<*>): Int = propValue

  operator fun setValue(p: Person5, prop: KProperty<*>, newValue: Int) {
    val oldValue = propValue
    propValue = newValue
    changeSupport.firePropertyChange(prop.name, oldValue, newValue)
  }
}

class Person5(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

  var age: Int by ObservableProperty2(age, changeSupport)
  var salary: Int by ObservableProperty2(salary, changeSupport)

}

class User(id: EntityID<Int>) : Entity<Int>(id) {
  var name: String by Users.name
  var age: Int by Users.age
}

object Users : IdTable<Int>("Users") {
  override val id: Column<EntityID<Int>>
    get() = integer("id").entityId()

  val name: Column<String> = varchar("name", 50).index()
  val age: Column<Int> = integer("age")
}