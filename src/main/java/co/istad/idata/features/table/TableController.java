package co.istad.idata.features.table;

import co.istad.idata.features.table.dto.TableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tables")
public class TableController {

    private final TableService tableService;

    @PostMapping
    void createTable(@RequestBody TableRequest request){
        tableService.createTable(request);
    }

}
