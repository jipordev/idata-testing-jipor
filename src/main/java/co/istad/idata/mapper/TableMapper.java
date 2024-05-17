package co.istad.idata.mapper;

import co.istad.idata.domains.dynamic_schema.DynamicColumn;
import co.istad.idata.domains.dynamic_schema.DynamicTable;
import co.istad.idata.features.table.dto.column.ColumnRequest;
import co.istad.idata.features.table.dto.TableResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {

    @Mapping(target = "id", ignore = true) // Ignore id if it's not set during mapping
    @Mapping(target = "dynamicTable", ignore = true) // Ignore dynamicTable if it's not set during mapping
    List<DynamicColumn> fromColumnRequest(List<ColumnRequest> columnRequests);

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "schema", source = "schema")
    List<TableResponse> toTableResponse(List<DynamicTable> tables);


}
