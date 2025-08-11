package com.example.demo.Service;


import com.example.demo.models.Operation;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class OperationsWithKafka {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;








}
