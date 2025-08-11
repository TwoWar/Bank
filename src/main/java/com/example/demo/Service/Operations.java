package com.example.demo.Service;


import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import com.example.demo.Dao.OperationSaveOutboxDao;
import com.example.demo.Interface.OperationFromApi;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Service
public class Operations implements OperationFromApi{

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CheckUserBank checkUserBank;

    @Autowired
    private OperationSaveOutboxDao operationSaveOutboxDao;

    @Autowired
    private OtherMethods otherMethods;


    @Override
    public boolean sendMessageDebitingMoney(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO) {

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


    public void operationDebitingMoneyAndSendMessage(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO,
                                                     String cvv, String number, String name){
        try {
            System.out.println("Перед вызовом debitingMoney");
            Timestamp timestamp = otherMethods.generateDate();
            checkUserBank.debitingMoney(cvv, number, name, successDebitingFundsTopicDTO.getMoney(),successDebitingFundsTopicDTO,timestamp);

            System.out.println("Debiting Money success");


            sendMessageDebitingMoney(successDebitingFundsTopicDTO);

            System.out.println("Успех operationDebitingMoneyAndSendMessage");



        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void operationDebitingMoneyAndSaveOnOutbox(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO,String cvv, String number, String name){
        try{

            Timestamp timestamp = otherMethods.generateDate();

            checkUserBank.debitingMoney(cvv,number, name, successDebitingFundsTopicDTO.getMoney(),successDebitingFundsTopicDTO,timestamp);

            operationSaveOutboxDao.saveOperationOutbox(successDebitingFundsTopicDTO,timestamp);

        }catch (Exception e){
            throw new RuntimeException("Ошибка при списании средств " + successDebitingFundsTopicDTO.getIdOperation() + " " + e.getMessage());
        }
    }



}
