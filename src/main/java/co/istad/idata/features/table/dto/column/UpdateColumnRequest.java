package co.istad.idata.features.table.dto.column;

public record UpdateColumnRequest(
        String tableName,
        String oldColumnName,
        ColumnRequest newColumnDetails,
        Long userId
) {
}

