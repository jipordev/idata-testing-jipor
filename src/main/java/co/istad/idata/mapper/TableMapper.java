package co.istad.idata.mapper;

import co.istad.idata.domains.DynamicColumn;
import co.istad.idata.features.table.dto.ColumnRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {

    List<DynamicColumn> fromColumnRequest(List<ColumnRequest> columnRequests);

}
