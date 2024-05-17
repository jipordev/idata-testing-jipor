package co.istad.idata.features.table.dto;

import co.istad.idata.features.schema.dto.SchemaResponse;
import co.istad.idata.features.user.dto.UserResponse;

import java.time.LocalDateTime;

public record TableResponse(
        String tableName,
        String createdAt,
        UserResponse owner,
        SchemaResponse schema
) {
}
