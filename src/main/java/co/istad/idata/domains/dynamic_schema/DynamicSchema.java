package co.istad.idata.domains.dynamic_schema;


import co.istad.idata.domains.auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dynamic_schemas", schema = "public")
@Setter
@Getter
@NoArgsConstructor
public class DynamicSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schemaName;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "schema", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DynamicTable> tables;

}

