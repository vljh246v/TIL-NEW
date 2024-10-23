package com.graffiti.pad.day20241019.service

import com.graffiti.pad.day20241019.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(
    private val stockRepository: StockRepository,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun decreaseStock(
        productId: Long,
        quantity: Long,
    ) {
        val stock = stockRepository.findByProductId(productId)
        stock.quantity = stock.quantity?.let {
                if (it - quantity < 0) {
                    throw IllegalArgumentException("Stock is not enough")
                } else {
                    it - quantity
                }
            }
        stockRepository.save(stock)
    }

    @Transactional(readOnly = true)
    fun getStockQuantity(productId: Long): Long {
        return stockRepository.findByProductId(productId).quantity ?: 0
    }
}
