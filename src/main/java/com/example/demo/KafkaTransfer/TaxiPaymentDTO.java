package com.example.demo.KafkaTransfer;

import com.example.demo.Interface.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiPaymentDTO implements Event {

    private String number ;

    private String name;

    private String cvv;

    private  String name_bank;

    private Integer money;

    private Date SendingTime;

}
