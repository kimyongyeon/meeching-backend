package com.msa.study.meeching.common.redis;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

@Component
public class RedisCrudRepository {

    public RedisCrudRepository() {
    }

    public static String get(final String key, RedisTemplate<String, Object> redisTemplate) {
        return (String)redisTemplate.execute((RedisCallback<String>) (con) -> {
            byte[] value = con.get(key.getBytes());
            return value != null ? new String(value) : null;
        });
    }

    public static void set(final String key, final String value, RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.execute((RedisCallback<Object>) (con) -> {
            con.set(key.getBytes(), value.getBytes());
            return null;
        });
    }

    public static void set(final String key, final String value, final long time, RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.execute((RedisCallback<Object>) (con) -> {
            con.set(key.getBytes(), value.getBytes(), Expiration.milliseconds(time), RedisStringCommands.SetOption.UPSERT);
            return null;
        });
    }
}
