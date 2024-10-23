package com.graffiti.pad.day20241019.service

import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.graffiti.pad.day20241019.repository.StockRepository

@Service
class PessimisticLockStockService (
    private val stockRepository: StockRepository,
) {

    @Transactional
    fun decreaseStock(
        id: Long,
        quantity: Long,
    ) {
        val stock = stockRepository.findByIdWithPessimisticLock(id)
        stock.quantity = stock.quantity?.let {
                if (it - quantity < 0) {
                    throw IllegalArgumentException("Stock is not enough")
                } else {
                    it - quantity
                }
            }
        stockRepository.save(stock)
    }

    fun getStockQuantity(id: Long): Long {
        return stockRepository.findById(id).getOrNull()?.quantity ?: 0
    }
}