package com.example.demo.Service;

import cn.ipokerface.snowflake.SnowflakeIdGenerator;
import com.example.demo.Interface.GenerateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateIdOperationService implements GenerateId {

@Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public Long generateIdOperation() {

        return snowflakeIdGenerator.nextId();

    }
}
