package com.graffiti.pad.day20241019.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version

@Entity
open class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    open var productId: Long? = null
    open var quantity: Long? = null
    @Version
    open var version: Long? = null
}
