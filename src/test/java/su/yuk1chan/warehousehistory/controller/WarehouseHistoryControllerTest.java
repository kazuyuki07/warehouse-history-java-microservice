package su.yuk1chan.warehousehistory.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import su.yuk1chan.warehousehistory.dto.PagedResponse;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;
import su.yuk1chan.warehousehistory.mapper.WarehouseHistoryMapper;
import tools.jackson.databind.ObjectMapper;
import su.yuk1chan.warehousehistory.dto.WarehouseHistoryDTO;
import su.yuk1chan.warehousehistory.enums.Event;
import su.yuk1chan.warehousehistory.repository.WarehouseHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WarehouseHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WarehouseHistoryRepository warehouseHistoryRepository;

    @Autowired
    private WarehouseHistoryMapper warehouseHistoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void cleanData() {
        warehouseHistoryRepository.deleteAll();
    }

    @Test
    void writeHistorySuccess() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("INCOME"),
                "red",
                32,
                100,
                120,
                1L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.event").value("INCOME"))
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.cottonPart").value(32))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.currentQuantity").value(120))
                .andExpect(jsonPath("$.warehouseId").value(1L));
    }

    @Test
    void writeHistoryEmptyEventFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                null,
                "red",
                32,
                20,
                100,
                1L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Событие не должно быть пустым"));
    }

    @Test
    void writeHistoryEmptyColorFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("OUTCOME"),
                null,
                51,
                37,
                93,
                41L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Цвет не должен быть пустым"));
    }

    @Test
    void writeHistoryEmptyCottonPartFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("OUTCOME"),
                "red",
                null,
                11,
                321,
                92L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Процент хлопка не должен быть пустым")
                );
    }

    @Test
    void writeHistoryEmptyQuantityFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("OUTCOME"),
                "cinnamon",
                20,
                null,
                49,
                54L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Количество не должен быть пустым")
                );
    }

    @Test
    void writeHistoryEmptyWarehouseIdFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("INCOME"),
                "mint",
                84,
                32,
                172,
                null,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("ID склада не должен быть пустым")
                );
    }

    @Test
    void writeHistoryInvalidColorFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("OUTCOME"),
                "color123",
                51,
                32,
                93,
                41L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Неправильный цвет"));
    }

    @Test
    void writeHistoryInvalidBelowZeroCottonPartFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("INCOME"),
                "black",
                -10,
                32,
                81,
                6L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Процент хлопка не может быть отрицательным")
                );
    }

    @Test
    void writeHistoryInvalidAboveHundredCottonPartFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("INCOME"),
                "orange",
                130,
                14,
                95,
                9L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Процент хлопка не должен привышать 100")
                );
    }

    @Test
    void writeHistoryInvalidNegativeQuantityFail() throws Exception {
        WarehouseHistoryDTO warehouseHistoryDTO = new WarehouseHistoryDTO(
                Event.valueOf("INCOME"),
                "blue",
                10,
                -5,
                41,
                27L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/warehouse_history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseHistoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Количество не может быть отрицательным")
                );
    }

    @Test
    void getHistorySuccess() throws Exception {
        WarehouseHistory warehouseHistory = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("red")
                        .cottonPart(77)
                        .quantity(40)
                        .currentQuantity(100)
                        .warehouseId(1L)
                        .date(LocalDateTime.now())
                        .build()
        );

        WarehouseHistoryDTO warehouseHistoryResponse = warehouseHistoryMapper.warehouseHistoryToWarehouseHistoryDTO(warehouseHistory);

        mockMvc.perform(get("/api/warehouse_history")
                .param("event", "INCOME")
                .param("min_date", "01.01.2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].event").value("INCOME"))
                .andExpect(jsonPath("$.content[*].color").value(warehouseHistoryResponse.getColor()));
    }

    @Test
    void getHistoryWithFiltersByEventIncomeAndSort() throws Exception {
        WarehouseHistory warehouseHistory = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("white")
                        .cottonPart(31)
                        .quantity(102)
                        .currentQuantity(302)
                        .warehouseId(5L)
                        .date(LocalDateTime.now())
                        .build()
        );

        warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.OUTCOME)
                        .color("black")
                        .cottonPart(20)
                        .quantity(231)
                        .currentQuantity(30)
                        .warehouseId(2L)
                        .date(LocalDateTime.now())
                        .build()
        );

        PagedResponse<WarehouseHistoryDTO> warehouseHistoryResponse = new PagedResponse<>(
                List.of(
                    new WarehouseHistoryDTO(
                            warehouseHistory.getEvent(),
                            warehouseHistory.getColor(),
                            warehouseHistory.getCottonPart(),
                            warehouseHistory.getQuantity(),
                            warehouseHistory.getCurrentQuantity(),
                            warehouseHistory.getWarehouseId(),
                            warehouseHistory.getDate()
                    )
                ),
                0,
                10,
                1L,
                1
        );

        String jsonResult = mockMvc.perform(get("/api/warehouse_history")
                .param("event", "INCOME")
                .param("color", "red, blue, white")
                .param("min_cotton_part", "10")
                .param("min_quantity", "100")
                .param("warehouse_id_list", "1, 5, 7")
                .param("min_date", "01.01.1970")
                .param("max_date", "01.01.2030")
                .param("sort", "cotton_part"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(jsonResult).isEqualTo(objectMapper.writeValueAsString(warehouseHistoryResponse));
    }

    @Test
    void getHistoryWithFiltersByEventOutcomeAndSort() throws Exception {
        WarehouseHistory warehouseHistory = warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.OUTCOME)
                        .color("black")
                        .cottonPart(20)
                        .quantity(231)
                        .currentQuantity(100)
                        .warehouseId(2L)
                        .date(LocalDateTime.now())
                        .build()
        );

        warehouseHistoryRepository.save(
                WarehouseHistory.builder()
                        .event(Event.INCOME)
                        .color("white")
                        .cottonPart(31)
                        .quantity(102)
                        .currentQuantity(202)
                        .warehouseId(5L)
                        .date(LocalDateTime.now())
                        .build()
        );

        PagedResponse<WarehouseHistoryDTO> warehouseHistoryResponse = new PagedResponse<>(
                List.of(
                        new WarehouseHistoryDTO(
                                warehouseHistory.getEvent(),
                                warehouseHistory.getColor(),
                                warehouseHistory.getCottonPart(),
                                warehouseHistory.getQuantity(),
                                warehouseHistory.getCurrentQuantity(),
                                warehouseHistory.getWarehouseId(),
                                warehouseHistory.getDate()
                        )
                ),
                0,
                10,
                1L,
                1
        );

        String jsonResult = mockMvc.perform(get("/api/warehouse_history")
                        .param("event", "OUTCOME")
                        .param("color", "black, cinnamon")
                        .param("min_cotton_part", "15")
                        .param("max_quantity", "400")
                        .param("warehouse_id_list", "2, 36, 99")
                        .param("min_date", "01.01.1978")
                        .param("max_date", "01.01.2030"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(jsonResult).isEqualTo(objectMapper.writeValueAsString(warehouseHistoryResponse));
    }

    @Test
    void getHistoryEmptySuccess() throws Exception {
        mockMvc.perform(get("/api/warehouse_history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getHistoryWrongEventFail() throws Exception {
        mockMvc.perform(get("/api/warehouse_history")
                .param("event", "UNKNOWN1FOO"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHistoryUnknownSortFail() throws Exception {
        mockMvc.perform(get("/api/warehouse_history")
                .param("event", "OUTCOME")
                .param("min_date", "17.10.2014")
                .param("sort", "unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Вид сортировки не найден"));
    }
}
