package com.example.demo.GenerateCard;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


@Component
public class Generate {





    public String GenerateNumber(){
        //16
        StringBuilder builderNumber = new StringBuilder();
        ThreadLocalRandom.current().ints(1000,9999).distinct().limit(4).forEach(builderNumber::append);
        return builderNumber.toString();
    }
    public String GenerateCVV(){
        StringBuilder builderCVV = new StringBuilder();
        ThreadLocalRandom.current().ints(100,999).limit(1).forEach(builderCVV::append);
        return builderCVV.toString();
    }











}
