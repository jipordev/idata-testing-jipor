package co.istad.idata.domains.dynamic_schema;

import co.istad.idata.domains.auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "dynamic_tables", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class DynamicTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableName;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "dynamicTable",cascade = CascadeType.ALL)
    private List<DynamicColumn> columns;

    @ManyToOne
    @JoinColumn(name = "schema_id")
    private DynamicSchema schema;

    @ManyToOne
    private User owner;

}
