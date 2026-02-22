package org.cook.bank_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cook.bank_system.entity.OutboxEventEntity;
import org.cook.bank_system.model.TransactionEvent;
import org.cook.bank_system.repository.OutboxRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayService {

    private final TransfersProducerService producerService;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 10000)
    public void checkTransfers(){
        List<OutboxEventEntity> events = outboxRepository.findByProcessedFalse();

        if(events.isEmpty())
            return;

        log.info("Data is sending");

        for (OutboxEventEntity event: events){
            try{
                TransactionEvent transactionEvent = objectMapper.readValue(event.getPayload(), TransactionEvent.class);

                producerService.send(transactionEvent);
                event.setProcessed(true);

                outboxRepository.save(event);

                log.info("Event {} sent to kafka", event.getId());
            }catch (Exception e){
                log.error("Failed to process outbox event {} : {}", event.getId(), e.getMessage());
            }
        }
    }
}

//Something important, test for branches
//Something important, test2 for branches
