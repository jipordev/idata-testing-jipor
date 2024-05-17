package co.istad.idata.features.table;

import co.istad.idata.domains.dynamic_schema.DynamicTable;
import co.istad.idata.domains.dynamic_schema.DynamicSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TableRepository extends JpaRepository<DynamicTable, Long> {
    // Corrected method to use DynamicSchema
    Optional<DynamicTable> findByTableNameAndSchema(String tableName, DynamicSchema schema);

    DynamicTable findByTableName(String s);

    Optional<DynamicTable> findByTableNameAndSchema_SchemaName(String s, String schemaName);
}
