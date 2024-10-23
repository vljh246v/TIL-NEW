package com.graffiti.pad.day20241019.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import com.graffiti.pad.day20241019.domain.Stock

interface LockRepository: JpaRepository<Stock, Long> {
    @Query("SELECT get_lock(:key, 3000)", nativeQuery = true)
    fun getLock(key: String)

    @Query("SELECT release_lock(: key)", nativeQuery = true)
    fun releaseLock(key: String)
}