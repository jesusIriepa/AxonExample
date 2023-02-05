package com.example.axon.infrastructure.repository;

import com.example.axon.infrastructure.repository.entity.CardSummaryEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("query")
@Repository
public interface CardSummaryRepository extends JpaRepository<CardSummaryEntity, String> {
}
