package su.yuk1chan.warehousehistory.mapper;

import org.mapstruct.Mapper;
import su.yuk1chan.warehousehistory.dto.WarehouseHistoryDTO;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface WarehouseHistoryMapper {
    WarehouseHistoryDTO warehouseHistoryToWarehouseHistoryDTO(WarehouseHistory warehouseHistory);
    List<WarehouseHistoryDTO> listWarehouseHistoryToListWarehouseHistoryDTO(List<WarehouseHistory> warehouseHistoryList);
    WarehouseHistory warehouseHistoryDTOToWarehouseHistory(WarehouseHistoryDTO warehouseHistoryDTO);
}
