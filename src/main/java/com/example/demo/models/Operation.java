package com.example.demo.models;


import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Operation {

    public Operation() {

    }

    public Operation(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO) {
        this.idOperation = successDebitingFundsTopicDTO.getIdOperation();
        this.money= successDebitingFundsTopicDTO.getMoney();
        this.userId=successDebitingFundsTopicDTO.getId();
    }

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id_operation")
    private Long idOperation;

    private int money;

    private Timestamp date;



}
