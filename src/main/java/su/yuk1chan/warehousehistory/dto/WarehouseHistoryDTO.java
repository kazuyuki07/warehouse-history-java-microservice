package su.yuk1chan.warehousehistory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import su.yuk1chan.warehousehistory.enums.Event;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseHistoryDTO {
    @NotNull(message = "Событие не должно быть пустым")
    private Event event;

    @NotBlank(message = "Цвет не должен быть пустым")
    @Pattern(regexp = "[a-zA-z]+", message = "Неправильный цвет")
    private String color;

    @NotNull(message = "Процент хлопка не должен быть пустым")
    @Min(value = 0, message = "Процент хлопка не может быть отрицательным")
    @Max(value = 100, message = "Процент хлопка не должен привышать 100")
    private Integer cottonPart;

    @Min(value = 0, message = "Количество не может быть отрицательным")
    @NotNull(message = "Количество не должен быть пустым")
    private Integer quantity;

    @Min(value = 0, message = "Текущее количество не может быть отрицательным")
    @NotNull(message = "Текущее количество не должен быть пустым")
    private Integer currentQuantity;

    @NotNull(message = "ID склада не должен быть пустым")
    private Long warehouseId;

    @NotNull(message = "Дата не должна быть пустой")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;

    @Override
    public String toString() {
        return "WarehouseHistoryDTO{" +
                "event=" + event +
                ", color='" + color + '\'' +
                ", cottonPart=" + cottonPart +
                ", quantity=" + quantity +
                ", currentQuantity=" + currentQuantity +
                ", warehouseId=" + warehouseId +
                ", date=" + date +
                '}';
    }
}
