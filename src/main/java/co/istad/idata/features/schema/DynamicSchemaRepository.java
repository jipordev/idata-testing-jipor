package co.istad.idata.features.schema;

import co.istad.idata.domains.dynamic_schema.DynamicSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DynamicSchemaRepository extends JpaRepository<DynamicSchema, Long> {
    List<DynamicSchema> findByCreatedAtBefore(LocalDateTime cutoff);

    Optional<DynamicSchema> findBySchemaName(String schemaName);
}
