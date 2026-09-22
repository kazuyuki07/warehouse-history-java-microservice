package su.yuk1chan.warehousehistory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import su.yuk1chan.warehousehistory.enums.Event;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseHistoryDTO {
    private Event event;
    private String color;
    private Integer cottonPart;
    private Integer quantity;
    private Integer currentQuantity;
    private Long warehouseId;

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
