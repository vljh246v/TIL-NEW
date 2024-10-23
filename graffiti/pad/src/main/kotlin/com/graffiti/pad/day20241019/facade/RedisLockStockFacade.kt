package com.graffiti.pad.day20241019.facade

import org.springframework.stereotype.Service
import com.graffiti.pad.day20241019.repository.RedisLockRepository
import com.graffiti.pad.day20241019.service.StockService

@Service
class RedisLockStockFacade(
    private val redisLockRepository: RedisLockRepository,
    private val stockService: StockService
) {
    fun decreaseStock(id: Long, quantity: Long) {
        while(!redisLockRepository.lock(id)) {
            Thread.sleep(100)
        }
        try {
            stockService.decreaseStock(id, quantity)
        } finally {
            redisLockRepository.unlock(id)
        }
    }

    fun getStockQuantity(id: Long): Long {
        return stockService.getStockQuantity(id)
    }
}