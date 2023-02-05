package com.example.axon.infrastructure.repository;

import com.example.axon.infrastructure.repository.entity.CardHistoryEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("query")
@Repository
public interface CardHistoryRepository extends JpaRepository<CardHistoryEntity, String> {

    Page<CardHistoryEntity> findAllByGiftCardId(String giftCardId, Pageable pageable);
}
