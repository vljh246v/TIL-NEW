package com.graffiti.pad.day20241019.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.graffiti.pad.day20241019.repository.LockRepository
import com.graffiti.pad.day20241019.service.StockService

@Service
class NamedLockStockFacade(
    private val lockRepository: LockRepository,
    private val stockService: StockService
) {

    @Transactional
    fun decreaseStock(id: Long, quantity: Long) {
        try {
            lockRepository.getLock(id.toString())
            stockService.decreaseStock(id, quantity)
        } finally {
            lockRepository.releaseLock(id.toString())
        }
    }

    fun getStockQuantity(id: Long): Long {
        return stockService.getStockQuantity(id)
    }
}