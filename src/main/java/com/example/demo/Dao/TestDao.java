package com.example.demo.Dao;


import com.example.demo.models.Card;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TestDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JobLauncher jobLauncher;

    @Transactional
    public void generateCards(List<Card> cards) {
        int batchSize = 20;  // Размер пакета для вставки
        for (int i = 0; i < cards.size(); i++) {
            entityManager.merge(cards.get(i));

            // Каждые 20 вставок, выполняем flush и clear.
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush(); // Отправляем текущий пакет в БД
                entityManager.clear(); // Очищаем контекст управления для освобождения памяти
                System.out.println("Inserted batch up to the index: " + i);
            }
        }
        entityManager.flush(); // Если остались записи в контексте, отправляем их
        entityManager.clear(); // Очищаем контекст
        System.out.println("Final flush for remaining records");
    }



}
