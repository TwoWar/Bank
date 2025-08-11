package com.example.demo.Dao;


import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Repository
public class OperationSaveOutboxDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveOperationOutbox(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO, Timestamp date) {
        try {
            entityManager.createNativeQuery("INSERT INTO outbox_operation (id, user_id, id_operation, money, date) values (default,?,?,?,?)")
                    .setParameter(1, successDebitingFundsTopicDTO.getId())
                    .setParameter(2, successDebitingFundsTopicDTO.getIdOperation())
                    .setParameter(3, successDebitingFundsTopicDTO.getMoney())
                    .setParameter(4, date)
                    .executeUpdate();
        }catch (Exception e){
            throw new RuntimeException("Ошибка сохранения операции  " + successDebitingFundsTopicDTO.getIdOperation() + "  в Outbox." + e.getMessage());
        }

    }




}
