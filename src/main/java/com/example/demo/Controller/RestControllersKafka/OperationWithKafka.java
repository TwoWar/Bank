package com.example.demo.Controller.RestControllersKafka;


import com.example.demo.Service.RedisService;
import com.example.demo.models.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationWithKafka {

    @Autowired
    private RedisService redisService;



    @KafkaListener(topics = "queue-for-transfer-operation-topic" , groupId = "queue-for-transfer-operation")
    public void saveOperationWithRedis(Operation operation) {
        try {
            redisService.saveRedis(operation);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }



}
