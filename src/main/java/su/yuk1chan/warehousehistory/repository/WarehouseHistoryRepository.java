package su.yuk1chan.warehousehistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import su.yuk1chan.warehousehistory.entity.WarehouseHistory;

import java.util.List;

public interface WarehouseHistoryRepository extends JpaRepository<WarehouseHistory, Long>, JpaSpecificationExecutor<WarehouseHistory> {
    @Query(value = """
        SELECT wh.id
        FROM WarehouseHistory wh
        WHERE (YEAR(wh.date) + :deadlineByYear) <= :currentYear
    """)
    List<Long> getIdListByDate(@Param("deadlineByYear") Integer deadlineByYear, @Param("currentYear") Integer currentYear);
}
