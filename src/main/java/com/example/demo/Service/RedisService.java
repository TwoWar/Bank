package com.example.demo.Service;

import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import com.example.demo.models.Operation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private RedisTemplate<String, Object> redisTemplate;


    public void saveRedis(Operation operation) {
        try {
            redisTemplate.opsForValue().set(String.valueOf(operation.getIdOperation()), operation);
        }catch (Exception e){
            throw new RuntimeException("Ошибка сохранения операции в redis " + operation.getIdOperation() + " " + e.getMessage());
        }

    }


}
