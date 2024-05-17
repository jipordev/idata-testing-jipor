package co.istad.idata.features.table;

import co.istad.idata.domains.dynamic_schema.DynamicColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<DynamicColumn, Long> {
}
