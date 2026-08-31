package su.yuk1chan.warehousehistory.entity;

import jakarta.persistence.*;
import lombok.*;
import su.yuk1chan.warehousehistory.enums.Event;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "warehouse_history")
public class WarehouseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Event event;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, name = "cotton_part")
    private Integer cottonPart;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "current_quantity")
    private Integer currentQuantity;

    @Column(nullable = false, name = "warehouse_id")
    private Long warehouseId;

    @Column(nullable = false)
    private LocalDateTime date;
}
