package org.cook.bank_system.repository;

import org.cook.bank_system.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEventEntity, Long> {

    List<OutboxEventEntity> findByProcessedFalse();


}
