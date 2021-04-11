package com.ismummy.cardvalidator.repositories;

import com.ismummy.cardvalidator.models.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT record.cardNumber AS cardNumber, COUNT(record.cardNumber) AS count FROM Card record GROUP BY record.cardNumber")
    Page<Map<String, Object>> getValidCardCount(Pageable pageable);
}
