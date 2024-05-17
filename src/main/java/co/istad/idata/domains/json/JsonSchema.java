package co.istad.idata.domains.json;

import co.istad.idata.domains.api.Api;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "json_schemas")
@Setter
@Getter
@NoArgsConstructor
public class JsonSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Api api;

}
