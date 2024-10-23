package com.graffiti.pad.day20241019.facade

import org.springframework.stereotype.Service
import com.graffiti.pad.day20241019.service.OptimisticLockStockService

@Service
class OptimisticLockStockFacade(
    private val optimisticLockStockService: OptimisticLockStockService,
) {
    fun decreaseStock(id: Long, quantity: Long, ) {
        while(true) {
            try {
                optimisticLockStockService.decreaseStock(id, quantity)
                break
            } catch (e: Exception) {
                Thread.sleep(50)
            }
        }
    }

    fun getStockQuantity(id: Long): Long {
        return optimisticLockStockService.getStockQuantity(id)
    }
}