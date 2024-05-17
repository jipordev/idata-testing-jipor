package co.istad.idata.features.table.dto.column;

public record ColumnRequest(
        String columnName,
        String type,
        boolean primaryKey
) {
}
