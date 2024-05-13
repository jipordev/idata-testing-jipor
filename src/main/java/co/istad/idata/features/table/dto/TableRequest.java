package co.istad.idata.features.table.dto;

import java.util.List;

public record TableRequest(
        String tableName,
        List<ColumnRequest> columns
) {

}
