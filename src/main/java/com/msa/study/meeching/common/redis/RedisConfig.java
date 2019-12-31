package com.msa.study.meeching.common.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.type.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;

// todo: 레디스 설치 후 풀어야 한다.
@Configuration
@ConfigurationProperties(
        prefix = "spring.redis"
)
@EnableCaching
@EnableRedisHttpSession
@Data
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    public static final String CACHE_NAME = "MsgCache";
    private String host;
    private int port;
    private String password;
    private long timetolive;

    public RedisConfig() {
    }

    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        log.debug("### REDIS :::: host // port //  password :: [{}] // [{}] // [{}] ", new Object[]{this.host, this.port, this.password});
        redisStandaloneConfiguration.setHostName(this.host);
        redisStandaloneConfiguration.setPort(this.port);
        redisStandaloneConfiguration.setPassword(this.password);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean(name = {"redisTemplate"})
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new RedisConfig.JsonRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(this.redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(this.redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisConfig.JsonRedisSerializer())).entryTtl(Duration.ofSeconds(this.timetolive));
        builder.cacheDefaults(configuration).transactionAware();
        return builder.build();
    }

    static class JsonRedisSerializer implements RedisSerializer<Object> {
        private final ObjectMapper om;

        public JsonRedisSerializer() {
            this.om = (new ObjectMapper()).enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }

        public byte[] serialize(Object t) throws SerializationException {
            try {
                return this.om.writeValueAsBytes(t);
            } catch (JsonProcessingException var3) {
                throw new SerializationException(var3.getMessage(), var3);
            }
        }

        public Object deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null) {
                return null;
            } else {
                try {
                    return this.om.readValue(bytes, Object.class);
                } catch (Exception var3) {
                    throw new SerializationException(var3.getMessage(), var3);
                }
            }
        }
    }
}
