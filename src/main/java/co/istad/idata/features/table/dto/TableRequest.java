package co.istad.idata.features.table.dto;

import co.istad.idata.features.table.dto.column.ColumnRequest;

import java.util.List;

public record TableRequest(
        String tableName,
        List<ColumnRequest> columns,
        Long userId
) {

}
