package com.hanghae.final_project.global.config.redis;

import com.hanghae.final_project.api.chat.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.infra.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Configuration
public class RedisConfig {
    public static final String CHAT_TOPIC = "ChatTopic";

    @Value("${spring.redis.host}")
    private String redisHostName;
    @Value("${spring.redis.port}")
    private int redisPort;

    //redis pub/sub 사용할 때의 chatting Topic
    @Bean
    public ChannelTopic channelTopic(){
        return new ChannelTopic(CHAT_TOPIC);
    }
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber redisSubscriber){

        return new MessageListenerAdapter(redisSubscriber,"sendMessage");
    }
    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHostName, redisPort);
        return lettuceConnectionFactory;
    }
    /*
     * redis pub/sub 메시지를 처리하는 listener 설정
     * */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter,channelTopic);

        return container;
    }

    /*
     * 어플리케이션에서 사용할 redisTempalte 설정
     * */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return  redisTemplate;
    }

    @Bean
    public RedisTemplate<String , String> roomRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String ,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate = new RedisTemplate<>();
        chatRedisTemplate.setConnectionFactory(connectionFactory);
        chatRedisTemplate.setKeySerializer(new StringRedisSerializer());
        chatRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageSaveDto.class));
        return chatRedisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(10));


        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();

        return redisCacheManager;
    }
}