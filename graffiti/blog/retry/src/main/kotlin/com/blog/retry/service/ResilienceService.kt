package com.blog.retry.service

import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import com.blog.retry.client.ChaosClient
import feign.FeignException

@Service
class ResilienceService(
    private val chaosClient: ChaosClient
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Retryable(
        value = [FeignException::class], // 1. Feign 호출 중 에러가 터지면?
        maxAttempts = 3,                 // 2. 최대 3번까지 시도한다 (최초 1회 + 재시도 2회)
        backoff = Backoff(delay = 1000)  // 3. 그냥 하지 말고 1초 쉬었다가 해라 (숨 고르기)
    )
    fun requestToVillain(): String {
        log.info("🚀 외부 API(Chaos) 호출 시도...") // 로그로 재시도 횟수를 확인하기 위함
        return chaosClient.callFlakyEndpoint()
    }
}