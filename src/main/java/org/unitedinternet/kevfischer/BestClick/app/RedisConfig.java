package org.unitedinternet.kevfischer.BestClick.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.unitedinternet.kevfischer.BestClick.model.database.LeaderboardPage;
import org.unitedinternet.kevfischer.BestClick.model.redis.LeaderboardCache;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("bestclick-db.azubi.server.lan", 3400);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, LeaderboardPage> lbTemplate() {
        RedisTemplate<String, LeaderboardPage> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public LeaderboardCache leaderboardCache(){
        return new LeaderboardCache();
    }

}
