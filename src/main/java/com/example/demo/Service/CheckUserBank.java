package com.example.demo.Service;


import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import com.example.demo.Dao.CardRepository;
import com.example.demo.models.Card;
import com.example.demo.models.Operation;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CheckUserBank {


    private final CardRepository cardRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final TransactionTemplate transactionTemplate;



    private static final Logger logger = LoggerFactory.getLogger(CheckUserBank.class);

    @Autowired
    public CheckUserBank(CardRepository cardRepository, KafkaTemplate<String, Object> kafkaTemplate, TransactionTemplate transactionTemplate) {
        this.cardRepository = cardRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.transactionTemplate = transactionTemplate;
    }


   // @Transactional("transactionManager")
    public void debitingMoney(String cvv, String number, String name, int amount, SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO, Timestamp timestamp) {

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
                    sendWithOperationKafka(operation);

                    sendMessageDebitingMoney(successDebitingFundsTopicDTO);

                    System.out.println("вызов3");

                    return null;
                } else {
                    logger.error("Карта с номером: {} , name: {} , cvv: {} не найдена ", number, name, cvv);
                    throw new RuntimeException("Карта с введенными вами данными не найдена");
                }

            } catch (Exception e) {
                status.setRollbackOnly();
                System.out.println("Не удалось найти пользователя " + e.getMessage());
                throw new RuntimeException("Не удалось найти пользователя " + e.getMessage());
            }

        });

    }



    private void sendWithOperationKafka(Operation operation) {
        try {
            String messageType = operation.getClass().getSimpleName();
            System.out.println("sendWithOperationKafka");
            ProducerRecord<String, Object> record = new ProducerRecord<>(
                    "queue-for-transfer-operation-topic",
                    String.valueOf(operation.getIdOperation()),
                    operation
            );
            record.headers().add("messageType", messageType.getBytes());

            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);
        }catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке операции в kafka " + e.getMessage());
        }

    }

    private boolean sendMessageDebitingMoney(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO) {

        try {
            String messageType = successDebitingFundsTopicDTO.getClass().getSimpleName();

            System.out.println(messageType + " TYPE");
            ProducerRecord<String, Object> debiting = new ProducerRecord<>(
                    "success-debiting-funds-topic",
                    String.valueOf(successDebitingFundsTopicDTO.getIdOperation()),
                    successDebitingFundsTopicDTO
            );
            debiting.headers().add("messageType", messageType.getBytes());

            SendResult<String, Object> future = kafkaTemplate.send(debiting).get();

            System.out.println("Topic: " + future.getRecordMetadata().topic());
            System.out.println("Partition: " + future.getRecordMetadata().partition());
            System.out.println("Offset: " + future.getRecordMetadata().offset());
            System.out.println("Timestamp: " + future.getRecordMetadata().timestamp());
            System.out.println("PaymentId " + successDebitingFundsTopicDTO.getId());

            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



}
