package com.example.demo;


import com.example.demo.Dao.TestDao;
import com.example.demo.models.Card;
import com.github.javafaker.Faker;


import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.List;


@Service
public class Methods {

    @Autowired
    private TestDao testDao;



    public void GenerateUser(){
        int batchSize=20;
        List<Card> cards = new ArrayList<>();
        for(int i=0;i<200;i++) {
            Faker faker = new Faker();
            Card card = new Card();
            card.setNumber(String.valueOf(faker.number().randomNumber(16, true)));
            card.setName(faker.name().fullName());
            card.setCvv("222");
            card.setMoney(1000000);
            card.setName_bank("YmpaLympa");
            cards.add(card);
            if(i > 0 && i % batchSize == 0){
                testDao.generateCards(cards);
                cards.clear();
            }
        }


    }


}
