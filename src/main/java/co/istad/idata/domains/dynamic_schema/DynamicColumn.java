package co.istad.idata.domains.dynamic_schema;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dynamic_columns", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class DynamicColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_primary_key")
    private Boolean primaryKey;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "type")
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    private DynamicTable dynamicTable;

    public boolean isPrimaryKey() {
        return primaryKey != null && primaryKey;
    }
}
