package io.github.johnchoi96.webservice.configs.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    @Bean
    public Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS);
    }

    @Bean
    public CacheManager cacheManager(final Caffeine<Object, Object> caffeine) {
        final CaffeineCacheManager cacheManager = new CaffeineCacheManager("getRankingForWeek");
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
