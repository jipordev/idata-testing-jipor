package co.istad.idata.domains.json;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "json_datas")
@Setter
@Getter
@NoArgsConstructor
public class JsonData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    private MyJson jsonProperty;


}
