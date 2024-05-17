package co.istad.idata.generated_classes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(
    name = "students",
    schema = "jipor_1"
)
public class Student {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  @Column(
      name = "id"
  )
  private Long id;

  @Column(
      name = "name"
  )
  private String name;

  @Column(
      name = "age"
  )
  private Integer age;

  @Column(
      name = "phone"
  )
  private String phone;
}
