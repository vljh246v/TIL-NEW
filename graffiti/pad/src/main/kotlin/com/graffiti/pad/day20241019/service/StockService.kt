package com.graffiti.pad.day20241019.service

import com.graffiti.pad.day20241019.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockService(
    private val stockRepository: StockRepository,
) {
    fun decreaseStock(
        productId: Long,
        quantity: Long,
    ) {
        val stock = stockRepository.findByProductId(productId)
        stock.quantity =
            stock.quantity?.let {
                if (it - quantity < 0) {
                    throw IllegalArgumentException("Stock is not enough")
                } else {
                    it - quantity
                }
            }
        stockRepository.save(stock)
    }
}
