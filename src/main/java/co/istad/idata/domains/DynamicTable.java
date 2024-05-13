package co.istad.idata.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "dynamic_tables")
@Getter
@Setter
@NoArgsConstructor
public class DynamicTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableName;
    @ManyToOne
    private User owner;
    private LocalDateTime createdAt;
    @OneToMany
    private List<DynamicColumn> columns;

}
