package com.app.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(2));
        RedisCacheConfiguration taskConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30));
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(defaultCacheConfig).withCacheConfiguration("task",taskConfig).build();
    }
}
