package debop4k.core.json.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Person {

  private String degree;

  public Student(String name, int age, String degree) {
    super(name, age);
    this.degree = degree;
  }

}
