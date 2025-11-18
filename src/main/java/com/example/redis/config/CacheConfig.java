package com.example.redis.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

	// RedisCacheManager 상위에 CacheManager가 존재
	@Bean
	public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		// 설정 구성을 먼저 진행한다.
		// 레디스를 이용해서 스프링 캐시를 사용할 때 Redis 관련 설정을 모아두는 클래스
		RedisCacheConfiguration configuration = RedisCacheConfiguration
			.defaultCacheConfig()
			// null을 캐싱하는지
			.disableCachingNullValues()
			// 기본 캐시 유지 시간 (TTL, Time To Live)
			.entryTtl(Duration.ofSeconds(120))
			// 캐시를 구분하는 접두사 설정
			.computePrefixWith(CacheKeyPrefix.simple())
			// 캐시에 저장할 값을 어떻게 직렬화/역직렬화 할 것인지 나타내는 설
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
			);

		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(configuration)
			// .withInitialCacheConfigurations() // Map<cacheName, RedisCacheConfiguration>
			.build();
	}
}
