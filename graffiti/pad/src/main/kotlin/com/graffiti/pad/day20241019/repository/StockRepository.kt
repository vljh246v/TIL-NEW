package com.graffiti.pad.day20241019.repository

import org.hibernate.LockMode
import com.graffiti.pad.day20241019.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import jakarta.persistence.LockModeType

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByProductId(productId: Long): Stock

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.id = :id")
    fun findByIdWithPessimisticLock(id: Long): Stock


    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Stock s WHERE s.id = :id")
    fun findByIdWithOptimisticLock(id: Long): Stock

}
