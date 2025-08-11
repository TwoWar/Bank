package com.example.demo.Configuration;

import cn.ipokerface.snowflake.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Value("${snowflake.worker.id}")
    private long workerId;

    @Value("${snowflake.datacenter.id}")
    private long datacenterId;

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {

        if (workerId < 0 || workerId > 31) {
            throw new IllegalArgumentException("workerId must be between 0 and 31");
        }
        if (datacenterId < 0 || datacenterId > 31) {
            throw new IllegalArgumentException("datacenterId must be between 0 and 31");
        }
        return new SnowflakeIdGenerator(workerId, datacenterId);

    }
}
