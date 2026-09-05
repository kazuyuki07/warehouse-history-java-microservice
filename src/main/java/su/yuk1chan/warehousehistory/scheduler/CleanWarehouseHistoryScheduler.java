package su.yuk1chan.warehousehistory.scheduler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import su.yuk1chan.warehousehistory.repository.WarehouseHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@EnableScheduling
@NoArgsConstructor
@Component
public class CleanWarehouseHistoryScheduler {
    @Autowired
    private WarehouseHistoryRepository warehouseHistoryRepository;

    @Value("${spring.schedule.deadline-by-year}")
    private Integer deadlineByYear;

    @Scheduled(cron = "${spring.schedule.cron}")
    private void clearDate() {
        log.info("Очистка WarehouseHistory deadlineByYear: {}", deadlineByYear);
        List<Long> idListByDate = warehouseHistoryRepository.getIdListByDate(
                deadlineByYear,
                LocalDateTime.now().getYear()
        );
        if (idListByDate.isEmpty()) {
            return;
        }

        warehouseHistoryRepository.deleteAllById(idListByDate);
    }
}
