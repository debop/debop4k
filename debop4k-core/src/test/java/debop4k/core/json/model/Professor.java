package debop4k.core.json.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends Person {

  String spec;

  public Professor(String name, int age, String spec) {
    super(name, age);
    this.spec = spec;
  }

}
