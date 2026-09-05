package su.yuk1chan.warehousehistory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import su.yuk1chan.warehousehistory.dto.PagedResponse;
import su.yuk1chan.warehousehistory.dto.WarehouseHistoryDTO;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;
import su.yuk1chan.warehousehistory.enums.Event;
import su.yuk1chan.warehousehistory.enums.WarehouseHistorySort;
import su.yuk1chan.warehousehistory.mapper.WarehouseHistoryMapper;
import su.yuk1chan.warehousehistory.repository.WarehouseHistoryRepository;
import su.yuk1chan.warehousehistory.repository.specification.WarehouseHistorySpecification;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseHistoryService {
    private final WarehouseHistoryRepository warehouseHistoryRepository;
    private final WarehouseHistoryMapper warehouseHistoryMapper;


    public PagedResponse<WarehouseHistoryDTO> getHistory(
            Integer page,
            Integer size,
            Event event,
            List<String> color,
            Integer minCottonPart,
            Integer maxCottonPart,
            Integer minQuantity,
            Integer maxQuantity,
            Integer minCurrentQuantity,
            Integer maxCurrentQuantity,
            List<Long> warehouseIdList,
            LocalDate minDate,
            LocalDate maxDate,
            String sort) {
        Specification<WarehouseHistory> warehouseHistorySpecification = Specification.where(
                WarehouseHistorySpecification.eventFilter(event)
                    .and(WarehouseHistorySpecification.cottonPartFilter(minCottonPart, maxCottonPart))
                    .and(WarehouseHistorySpecification.colorFilter(color))
                    .and(WarehouseHistorySpecification.quantityFilter(minQuantity, maxQuantity))
                    .and(WarehouseHistorySpecification.currentQuantityFilter(minCurrentQuantity, maxCurrentQuantity))
                    .and(WarehouseHistorySpecification.warehouseIdFilter(warehouseIdList))
                    .and(WarehouseHistorySpecification.dateFilter(minDate, maxDate))
        );
        WarehouseHistorySort warehouseHistorySort = WarehouseHistorySort.of(sort);
        Sort sortResult = Sort.by(
                warehouseHistorySort.getDirection(),
                warehouseHistorySort.getEntityValue()
        );

        Page<WarehouseHistory> warehouseHistory = warehouseHistoryRepository.findAll(
                warehouseHistorySpecification,
                PageRequest.of(page, size, sortResult)
        );

        return PagedResponse.from(warehouseHistory
                .map(warehouseHistoryMapper::warehouseHistoryToWarehouseHistoryDTO)
        );
    }

    public WarehouseHistory writeHistory(WarehouseHistoryDTO warehouseHistoryDTO) {
        WarehouseHistory warehouseHistory = warehouseHistoryMapper.warehouseHistoryDTOToWarehouseHistory(warehouseHistoryDTO);
        return warehouseHistoryRepository.save(warehouseHistory);
    }
}
