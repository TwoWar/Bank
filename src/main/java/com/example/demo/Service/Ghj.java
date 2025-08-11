package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Ghj {

    @Autowired
    private GenerateIdOperationService generateIdOperationService;

    public Long test(){

        return generateIdOperationService.generateIdOperation();

    }


}
