package com.example.demo.Dao;

import com.example.demo.models.Outbox.Outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox,Long> {

    @Query("SELECT o FROM Outbox o WHERE o.id > :lastProcessedId ORDER BY o.id ASC")
    List<Outbox> findByIdGreaterThan(@Param("lastProcessedId") Long lastProcessedId, Pageable pageable);
}
