package su.yuk1chan.warehousehistory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import su.yuk1chan.warehousehistory.dto.PagedResponse;
import su.yuk1chan.warehousehistory.dto.WarehouseHistoryDTO;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;
import su.yuk1chan.warehousehistory.enums.Event;
import su.yuk1chan.warehousehistory.service.WarehouseHistoryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouse_history")
public class WarehouseHistoryController {
    private final WarehouseHistoryService warehouseHistoryService;

    @GetMapping()
    public PagedResponse<WarehouseHistoryDTO> getHistory(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "-date") String sort,
            @RequestParam(required = false) Event event,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false, name = "min_cotton_part") Integer minCottonPart,
            @RequestParam(required = false, name = "max_cotton_part") Integer maxCottonPart,
            @RequestParam(required = false, name = "min_quantity") Integer minQuantity,
            @RequestParam(required = false, name = "max_quantity") Integer maxQuantity,
            @RequestParam(required = false, name = "min_current_quantity") Integer minCurrentQuantity,
            @RequestParam(required = false, name = "max_current_quantity") Integer maxCurrentQuantity,
            @RequestParam(required = false, name = "warehouse_id_list") List<Long> warehouseIdList,
            @RequestParam(required = false, name = "min_date") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate minDate,
            @RequestParam(required = false, name = "max_date") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate maxDate) {
        return warehouseHistoryService.getHistory(
                page,
                size,
                event,
                color,
                minCottonPart,
                maxCottonPart,
                minQuantity,
                maxQuantity,
                minCurrentQuantity,
                maxCurrentQuantity,
                warehouseIdList,
                minDate,
                maxDate,
                sort
        );
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseHistoryDTO writeHistory(@Valid @RequestBody WarehouseHistoryDTO warehouseHistoryDTO) {
        WarehouseHistory warehouseHistory = warehouseHistoryService.writeHistory(warehouseHistoryDTO);
        return new WarehouseHistoryDTO(
                warehouseHistory.getEvent(),
                warehouseHistory.getColor(),
                warehouseHistory.getCottonPart(),
                warehouseHistory.getQuantity(),
                warehouseHistory.getCurrentQuantity(),
                warehouseHistory.getWarehouseId(),
                warehouseHistory.getDate()
        );
    }
}
