package su.yuk1chan.warehousehistory.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import su.yuk1chan.warehousehistory.dto.PagedResponse;
import su.yuk1chan.warehousehistory.dto.WarehouseHistoryDTO;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;
import su.yuk1chan.warehousehistory.enums.Event;
import su.yuk1chan.warehousehistory.mapper.WarehouseHistoryMapper;
import su.yuk1chan.warehousehistory.repository.WarehouseHistoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WarehouseHistoryServiceTest {
    @Autowired
    private WarehouseHistoryService warehouseHistoryService;

    @Autowired
    private WarehouseHistoryRepository warehouseHistoryRepository;

    @Autowired
    private WarehouseHistoryMapper warehouseHistoryMapper;


    @AfterEach
    void cleanData() {
        warehouseHistoryRepository.deleteAll();
    }


    @Test
    void writeHistorySuccess() {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.INCOME,
                "black",
                31,
                124,
                200,
                1L,
                LocalDateTime.now()
        );

        WarehouseHistory result = warehouseHistoryService.writeHistory(warehouseHistoryDTO);

        assertThat(result.getEvent()).isEqualTo(warehouseHistoryDTO.getEvent());
        assertThat(result.getColor()).isEqualTo(warehouseHistoryDTO.getColor());
        assertThat(result.getCottonPart()).isEqualTo(warehouseHistoryDTO.getCottonPart());
        assertThat(result.getQuantity()).isEqualTo(warehouseHistoryDTO.getQuantity());
        assertThat(result.getCurrentQuantity()).isEqualTo(warehouseHistoryDTO.getCurrentQuantity());
        assertThat(result.getWarehouseId()).isEqualTo(warehouseHistoryDTO.getWarehouseId());
    }

    @Test
    void getHistorySuccess() {
        WarehouseHistory warehouseHistory = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("red")
                        .cottonPart(20)
                        .quantity(200)
                        .currentQuantity(200)
                        .warehouseId(7L)
                        .date(LocalDateTime.now())
                        .build()
        );
        WarehouseHistoryDTO expected = warehouseHistoryMapper.warehouseHistoryToWarehouseHistoryDTO(warehouseHistory);
        PagedResponse<WarehouseHistoryDTO> result = warehouseHistoryService.getHistory(
                0,
                10,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "-date"
        );

        assertThat(result.getContent().getFirst()).isEqualTo(expected);
    }

    @Test
    void getHistorySuccessWithFiltersAndSort() {
        WarehouseHistory warehouseHistory = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("blue")
                        .cottonPart(31)
                        .quantity(102)
                        .currentQuantity(102)
                        .warehouseId(10L)
                        .date(LocalDateTime.now())
                        .build()
        );
        WarehouseHistoryDTO expected = warehouseHistoryMapper.warehouseHistoryToWarehouseHistoryDTO(warehouseHistory);
        PagedResponse<WarehouseHistoryDTO> result = warehouseHistoryService.getHistory(
                0,
                10,
                Event.INCOME,
                List.of("blue"),
                20,
                50,
                90,
                null,
                null,
                null,
                List.of(10L, 1L, 2L),
                LocalDate.of(2024, 9, 10),
                null,
                "-date"
        );

        assertThat(result.getContent().getFirst()).isEqualTo(expected);
    }

    @Test
    void getHistoryEmptySuccess() {
        PagedResponse<WarehouseHistoryDTO> result = warehouseHistoryService.getHistory(
                0,
                10,
                Event.INCOME,
                List.of("blue"),
                20,
                50,
                90,
                null,
                null,
                null,
                List.of(10L, 1L, 2L),
                LocalDate.of(2024, 9, 10),
                null,
                "-date"
        );

        assertThat(result.getContent()).isEqualTo(List.of());
    }

    @Test
    void getHistoryUnknownSortFail() {
        warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("white")
                        .cottonPart(5)
                        .quantity(300)
                        .currentQuantity(410)
                        .warehouseId(71L)
                        .date(LocalDateTime.now())
                        .build()
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> warehouseHistoryService.getHistory(
                0,
                10,
                Event.INCOME,
                null,
                20,
                50,
                90,
                null,
                null,
                null,
                null,
                LocalDate.of(2024, 9, 10),
                null,
                "unknown_12391dfoo"
        ));

        assertEquals("Вид сортировки не найден", exception.getMessage());
    }

    @Test
    void scheduledClearDate() {
        WarehouseHistory warehouseHistory = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("white")
                        .cottonPart(52)
                        .quantity(51)
                        .currentQuantity(51)
                        .warehouseId(4L)
                        .date(LocalDateTime.of(2010, 10, 7, 12, 40))
                        .build()
        );
        WarehouseHistory warehouseHistory1 = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.OUTCOME)
                        .color("blue")
                        .cottonPart(74)
                        .quantity(300)
                        .currentQuantity(300)
                        .warehouseId(1L)
                        .date(LocalDateTime.of(2020, 5, 18, 10, 0))
                        .build()
        );
        WarehouseHistory warehouseHistory2 = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("red")
                        .cottonPart(20)
                        .quantity(43)
                        .currentQuantity(43)
                        .warehouseId(12L)
                        .date(LocalDateTime.of(2024, 7, 9, 17, 34))
                        .build()
        );

        await()
                .atMost(70, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(warehouseHistoryRepository.findById(warehouseHistory.getId())).isEmpty();
                    assertThat(warehouseHistoryRepository.findById(warehouseHistory1.getId())).isEmpty();
                });

        assertThat(warehouseHistoryRepository.findById(warehouseHistory2.getId())).isPresent();
    }
}
