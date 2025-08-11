package com.example.demo.Interface;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventTransfer {

    Event convertMessage(ConsumerRecord<String,Object> record);




}
