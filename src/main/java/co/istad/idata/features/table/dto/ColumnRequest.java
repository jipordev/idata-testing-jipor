package co.istad.idata.features.table.dto;

public record ColumnRequest(
        String columnName,
        String type,
        boolean primaryKey
) {
    // Constructors, methods, etc. can be added as needed
}
