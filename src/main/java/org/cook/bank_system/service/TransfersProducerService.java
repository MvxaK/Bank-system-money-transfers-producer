package org.cook.bank_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cook.bank_system.model.TransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransfersProducerService {

    private final KafkaTemplate<Long, TransactionEvent> kafkaTemplate;
    private static final String topic = "money-transfers";

    public void send(TransactionEvent event){
        kafkaTemplate.send(topic, event.getTransactionId(), event);
    }

}
