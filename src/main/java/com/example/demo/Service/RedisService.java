package com.example.demo.Service;

import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private RedisTemplate<String, Object> redisTemplate;


    public void saveRedis(SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO) {
        try {
            redisTemplate.opsForValue().set(String.valueOf(successDebitingFundsTopicDTO.getIdOperation()), successDebitingFundsTopicDTO);
        }catch (Exception e){
            throw new RuntimeException("Ошибка сохранения операции в redis " + successDebitingFundsTopicDTO.getIdOperation() + " " + e.getMessage());
        }

    }


}
