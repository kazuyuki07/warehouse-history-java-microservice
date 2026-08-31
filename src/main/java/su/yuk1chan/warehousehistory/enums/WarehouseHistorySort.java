package su.yuk1chan.warehousehistory.enums;


import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

@Getter
public enum WarehouseHistorySort {
    COTTON_PART_ASC("cotton_part", "cottonPart"),
    COTTON_PART_DESC("-cotton_part", "cottonPart"),
    QUANTITY_ASC("quantity", "quantity"),
    QUANTITY_DESC("-quantity", "quantity"),
    WAREHOUSE_ID_ASC("warehouse_id", "warehouseId"),
    WAREHOUSE_ID_DESC("-warehouse_id", "warehouseId"),
    DATE_ASC("date", "date"),
    DATE_DESC("-date", "date");

    private final String requestValue;
    private final String entityValue;
    private final Sort.Direction direction;

    WarehouseHistorySort(String requestValue, String entityValue) {
        this.requestValue = requestValue;
        this.entityValue = entityValue;
        this.direction = requestValue.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public static WarehouseHistorySort of(String requestValue) {
        return Stream.of(WarehouseHistorySort.values())
                .filter(e -> e.requestValue.equals(requestValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Вид сортировки не найден"));
    }
}
