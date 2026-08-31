package su.yuk1chan.warehousehistory.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;
import su.yuk1chan.warehousehistory.enums.Event;

import java.time.LocalDate;
import java.util.List;

public class WarehouseHistorySpecification {
    public static Specification<WarehouseHistory> eventFilter(Event event) {
        return (root, _, builder) ->
                event == null ? null : builder.equal(root.get("event"), event);
    }

    public static Specification<WarehouseHistory> colorFilter(List<String> color) {
        return (root, _, _) ->
                (color == null || color.isEmpty()) ? null : root.get("color").in(color);
    }

    public static Specification<WarehouseHistory> cottonPartFilter(Integer minCottonPart, Integer maxCottonPart) {
        return (root, _, builder) -> {
            if (minCottonPart == null && maxCottonPart == null) {
                return null;
            }

            if (minCottonPart == null) {
                return builder.lessThanOrEqualTo(root.get("cottonPart"), maxCottonPart);
            }

            if (maxCottonPart == null) {
                return builder.greaterThanOrEqualTo(root.get("cottonPart"), minCottonPart);
            }
            return builder.between(root.get("cottonPart"), minCottonPart, maxCottonPart);
        };
    }

    public static Specification<WarehouseHistory> quantityFilter(Integer minQuantity, Integer maxQuantity) {
        return (root, _, builder) -> {
            if (minQuantity == null && maxQuantity == null) {
                return null;
            }

            if (minQuantity == null) {
                return builder.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
            }

            if (maxQuantity == null) {
                return builder.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
            }

            return builder.between(root.get("quantity"), minQuantity, maxQuantity);
        };
    }

    public static Specification<WarehouseHistory> currentQuantityFilter(
            Integer minCurrentQuantity,
            Integer maxCurrentQuantity) {

        return (root, _, builder) -> {
            if (minCurrentQuantity == null && maxCurrentQuantity == null) {
                return null;
            }

            if (minCurrentQuantity == null) {
                return builder.lessThanOrEqualTo(root.get("currentQuantity"), maxCurrentQuantity);
            }

            if (maxCurrentQuantity == null) {
                return builder.greaterThanOrEqualTo(root.get("currentQuantity"), minCurrentQuantity);
            }

            return builder.between(root.get("currentQuantity"), minCurrentQuantity, maxCurrentQuantity);
        };
    }

    public static Specification<WarehouseHistory> warehouseIdFilter(List<Long> warehouseIdList) {
        return (root, _, _) ->
                warehouseIdList == null ? null : root.get("warehouseId").in(warehouseIdList);
    }

    public static Specification<WarehouseHistory> dateFilter(LocalDate earlier, LocalDate later) {
        return (root, _, builder) -> {
            if (earlier == null && later == null) {
                return null;
            }

            if (earlier == null) {
                return builder.lessThanOrEqualTo(root.get("date"), later);
            }

            if (later == null) {
                return builder.greaterThanOrEqualTo(root.get("date"), earlier);
            }

            return builder.between(root.get("date"), earlier, later);
        };
    }
}