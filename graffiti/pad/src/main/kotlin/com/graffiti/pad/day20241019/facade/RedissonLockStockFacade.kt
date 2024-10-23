package com.graffiti.pad.day20241019.facade

import java.util.concurrent.TimeUnit
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import com.graffiti.pad.day20241019.service.StockService

@Service
class RedissonLockStockFacade(
    private val redissonClient: RedissonClient,
    private val stockService: StockService
) {
    fun decreaseStock(id: Long, quantity: Long) {
        val lock = redissonClient.getLock(id.toString())

        try {
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)
            if (!available) {
                throw IllegalStateException("Failed to acquire lock")
            }
            stockService.decreaseStock(id, quantity)
        } finally {
            lock.unlock()
        }
    }

    fun getStockQuantity(id: Long): Long {
        return stockService.getStockQuantity(id)
    }
}