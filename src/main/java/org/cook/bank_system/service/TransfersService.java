package org.cook.bank_system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.bank_system.entity.AccountEntity;
import org.cook.bank_system.entity.OutboxEventEntity;
import org.cook.bank_system.model.TransactionEvent;
import org.cook.bank_system.repository.AccountRepository;
import org.cook.bank_system.repository.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransfersService {

    private final AccountRepository accountRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount){
        AccountEntity fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Sender account not found with id -> " + fromAccountId));

        AccountEntity toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver account not found with id -> " + toAccountId));

        if(fromAccount.getBalance().compareTo(amount) < 0){
            throw new IllegalStateException("Not enough balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        OutboxEventEntity outboxEvent = new OutboxEventEntity();

        outboxEvent.setAggregateId(fromAccountId.toString());
        outboxEvent.setType("MONEY_TRANSFER");
        outboxEvent.setProcessed(false);
        outboxEvent.setCreatedAt(LocalDateTime.now());

        outboxEvent = outboxRepository.save(outboxEvent);

        TransactionEvent transactionEvent = TransactionEvent.newBuilder()
                .setTransactionId(outboxEvent.getId())
                .setFromAccountId(fromAccountId)
                .setToAccountId(toAccountId)
                .setAmount(amount.doubleValue())
                .setCurrency("KZT")
                .setTimestamp(System.currentTimeMillis())
                .build();

        try {
            outboxEvent.setPayload(objectMapper.writeValueAsString(transactionEvent));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error with json processing");
        }

        outboxRepository.save(outboxEvent);
    }

}
