package com.example.demo.Service;


import com.example.demo.Interface.Event;
import com.example.demo.Interface.EventTransfer;
import com.example.demo.KafkaTransfer.TaxiPaymentDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventConverter implements EventTransfer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Event convertMessage(ConsumerRecord<String,Object> record) {
        String messageType = new String(record.headers().lastHeader("messageType").value());

        Object message = record.value();

        try {
            //получаем результат напрямую в нужный тип
            if ("TransferMoneyDTO".equals(messageType)) {
                TaxiPaymentDTO taxiPaymentDTO = objectMapper.convertValue(message, TaxiPaymentDTO.class);
                System.out.println("Received TaxiPaymentDTO: " + taxiPaymentDTO);
                return taxiPaymentDTO;
                //  Дальнейшая обработка taxiPaymentDTO
            } else {
                System.out.println("Unknown message type: " + messageType);
                return null;
            }
        } catch (RuntimeException e) {
            System.err.println("Error processing message: " + e.getMessage());
            throw new RuntimeException("Error processing message: " + e.getMessage());
        }
    }
}
