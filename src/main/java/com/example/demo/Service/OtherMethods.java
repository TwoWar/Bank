package com.example.demo.Service;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class OtherMethods {

    public Timestamp generateDate(){
        return Timestamp.from(Instant.now());
    }



}
