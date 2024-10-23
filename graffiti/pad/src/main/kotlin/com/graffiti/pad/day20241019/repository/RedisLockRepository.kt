package com.graffiti.pad.day20241019.repository

import java.time.Duration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisLockRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun lock(key: Long): Boolean {
       return redisTemplate
            .opsForValue()
            .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000L)) ?: false
    }

    fun unlock(key: Long): Boolean {
        return redisTemplate.delete(generateKey(key))
    }

    fun generateKey(key: Long): String {
        return key.toString()
    }
}