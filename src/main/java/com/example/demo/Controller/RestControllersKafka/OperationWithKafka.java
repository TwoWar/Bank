package com.example.demo.Controller.RestControllersKafka;


import com.example.demo.Service.RedisService;
import com.example.demo.models.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationWithKafka {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;




    private String determiningMessageType(ConsumerRecord<String,String> record){
        System.out.println("DeterminingMessageType");
        try {

            return new String(record.headers().lastHeader("messageType").value());

        }catch (Exception e){

            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "queue-for-transfer-operation-topic" , groupId = "queue-for-transfer-operation")
    public void saveOperationWithRedis(ConsumerRecord<String,String> record) {

            try {
                String messageType = determiningMessageType(record);

                String message = record.value();
Operation operation = new Operation();
                if (messageType.equals("Operation")) {
                    operation =  objectMapper.readValue(message, Operation.class);
                }
            System.out.println("saveOperationWithRedis");
            redisService.saveRedis(operation);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }



}
