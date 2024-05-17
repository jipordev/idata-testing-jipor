package co.istad.idata.features.table;

import co.istad.idata.features.table.dto.TableRequest;
import co.istad.idata.features.table.dto.TableResponse;
import co.istad.idata.features.table.dto.column.UpdateColumnRequest;

import java.util.List;

public interface TableService {

    void createTable(TableRequest tableRequest);
    List<TableResponse> getAll();
    void updateColumn(UpdateColumnRequest request);

}
