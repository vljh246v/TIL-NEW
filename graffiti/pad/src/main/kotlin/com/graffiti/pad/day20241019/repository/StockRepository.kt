package com.graffiti.pad.day20241019.repository

import com.graffiti.pad.day20241019.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByProductId(productId: Long): Stock
}
