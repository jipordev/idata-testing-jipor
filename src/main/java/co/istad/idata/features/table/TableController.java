package co.istad.idata.features.table;

import co.istad.idata.features.table.dto.TableRequest;
import co.istad.idata.features.table.dto.TableResponse;
import co.istad.idata.features.table.dto.column.ColumnRequest;
import co.istad.idata.features.table.dto.column.UpdateColumnRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tables")
public class TableController {

    private final TableService tableService;

    @GetMapping
    List<TableResponse> findAll(){
        return tableService.getAll();
    }

    @PostMapping
    void createTable(@RequestBody @Valid TableRequest request){
        tableService.createTable(request);
    }

    @PutMapping("/update-column")
    void updateColumn(@RequestBody @Valid UpdateColumnRequest request){
        tableService.updateColumn(request);
    }

}
