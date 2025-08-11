package com.example.demo.Service;


import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import com.example.demo.Dao.CardRepository;
import com.example.demo.models.Card;
import com.example.demo.models.Operation;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;



import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CheckUserBank {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private BeanFactory beanFactory;

    private static final Logger logger = LoggerFactory.getLogger(CheckUserBank.class);


    public void debitingMoney(String cvv, String number, String name, int amount, SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO, Timestamp timestamp) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(beanFactory.getBean("kafkaTransactionManager", KafkaTransactionManager.class));
        transactionTemplate.execute(status -> {
        System.out.println("debiting money");
            System.out.println("вызов0");
        try {
            Optional<Card> checkCard = cardRepository.findCardByCvvAndNumberAndName(cvv, number, name);

            if (checkCard.isPresent()) {
                Card card = checkCard.get(); //Важно создать именно новый объект на основе ответа от бд
                System.out.println("вызов1");
                if (checkCard.get().getMoney() >= amount) {
                    card.setMoney(checkCard.get().getMoney() - amount);
                    cardRepository.save(card);
                }

                Operation operation = new Operation(successDebitingFundsTopicDTO);
                operation.setDate(new Timestamp(timestamp.getTime()));

                System.out.println("вызов2");
                sendWithOperationKafka(operation,status);

                System.out.println("вызов3");
                return null;

            } else {
                logger.error("Карта с номером: {} , name: {} , cvv: {} не найдена ", number, name, cvv);
                throw new RuntimeException("Карта с введенными вами данными не найдена");
            }

        } catch (Exception e) {
            status.setRollbackOnly(); // Откат транзакции
            System.out.println("Не удалось найти пользователя " + e.getMessage());
            throw new RuntimeException("Не удалось найти пользователя " + e.getMessage());
        }
    });

    }



    private void sendWithOperationKafka(Operation operation, TransactionStatus status) {
        System.out.println("sendWithOperationKafka");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "queue-for-transfer-operation-topic",
                String.valueOf(operation.getIdOperation()),
                operation
        );

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);

        future.thenAccept(result -> {
                    System.out.println("Сообщение успешно отправлено в Kafka: " + operation.getIdOperation() +
                            ", offset=" + result.getRecordMetadata().offset());
                })
                .exceptionally(ex -> {
                    System.err.println("Ошибка при отправке сообщения в Kafka: " + operation.getIdOperation() +
                            ", error=" + ex.getMessage());
                    status.setRollbackOnly(); // Откат транзакции
                    throw new RuntimeException("Ошибка при отправке сообщения в Kafka", ex);
                });
    }



}
