package co.istad.idata.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dynamic_columns")
@Getter
@Setter
@NoArgsConstructor
public class DynamicColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_primary_key")
    private Boolean primaryKey;

    private String columnName;

    private String type;

    @ManyToOne
    private DynamicTable dynamicTable;

    public boolean isPrimaryKey() {
        return primaryKey != null && primaryKey;
    }
}

