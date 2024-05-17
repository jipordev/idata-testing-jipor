package co.istad.idata.domains.auth;

import co.istad.idata.domains.Auditable;
import co.istad.idata.domains.api.Api;
import co.istad.idata.domains.dynamic_schema.DynamicSchema;
import co.istad.idata.domains.dynamic_schema.DynamicTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String uuid;

    @OneToMany(mappedBy = "owner")
    List<DynamicSchema> dynamicSchemas;

    @OneToMany(mappedBy = "owner")
    List<DynamicTable> dynamicTables;

    @OneToMany(mappedBy = "owner")
    List<Api> apis;

}
