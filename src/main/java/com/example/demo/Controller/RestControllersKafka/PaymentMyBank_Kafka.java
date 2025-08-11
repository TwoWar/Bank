package com.example.demo.Controller.RestControllersKafka;


import com.example.demo.Interface.Event;
import com.example.demo.Service.CheckUserBank;
import com.example.demo.Service.EventConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment/pay")
public class PaymentMyBank_Kafka {

    @Autowired
    private EventConverter eventConverter;

    @Autowired
    private CheckUserBank checkUserBank;




    @KafkaListener(topics = "transfer-money-from-mybank-topic",groupId = "payment-money-from-mybank")
    public void paymentFromMyBank(ConsumerRecord<String,Object> record){

        Event event = eventConverter.convertMessage(record);

     //   boolean check = checkUserBank.checkUser(event);





    }




}
