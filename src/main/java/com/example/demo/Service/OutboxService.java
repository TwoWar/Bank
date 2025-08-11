package com.example.demo.Service;

import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import com.example.demo.Dao.OutboxRepository;
import com.example.demo.models.Outbox.Outbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class OutboxService {

    @Autowired
    private OutboxRepository outboxRepository;

    private Long lastProcessedId = 0L;

    @Autowired
    private RedisService redisService;


    public SuccessDebitingFundsTopicDTO transfer(Outbox outbox) {

        SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO = new SuccessDebitingFundsTopicDTO();
        successDebitingFundsTopicDTO.setId(outbox.getUserId());
        successDebitingFundsTopicDTO.setIdOperation(outbox.getIdOperation());
        successDebitingFundsTopicDTO.setMoney(outbox.getMoney());
        return successDebitingFundsTopicDTO;

    }


    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processOutbox() {

        int pageSize = 100;
        Pageable pageable = PageRequest.of(0, pageSize);

        // Получаем события, ID которых больше, чем lastProcessedId, и сортируем по ID
        List<Outbox> events = outboxRepository.findByIdGreaterThan(lastProcessedId,pageable);

        for (Outbox event : events) {
            try {

                SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO = transfer(event);

                redisService.saveRedis(successDebitingFundsTopicDTO);

                // 3. Удаляем событие из outbox
                outboxRepository.delete(event);

                // Обновляем lastProcessedId
                lastProcessedId = event.getId();

            } catch (Exception e) {

                System.err.println("Ошибка при обработке события из Outbox: " + e.getMessage());
            }

        }
    }
}
