package com.graffiti.pad.day20241019.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.graffiti.pad.day20241019.domain.Stock
import com.graffiti.pad.day20241019.repository.StockRepository


@SpringBootTest
class IntegrationStockServiceTest {
    @Autowired
    private lateinit var stockService: StockService
    @Autowired
    private lateinit var stockRepository: StockRepository


    @BeforeEach
    fun before() {
        val stock = Stock()
            .apply {
                productId = 1L
                quantity = 100L
            }
        stockRepository.saveAndFlush(stock)
    }

    @AfterEach
    fun after() {
        stockRepository.deleteAll()
    }

    @Test
    fun `decreaseStock`() {
        stockService.decreaseStock(1L, 1L)
        val stock = stockService.getStock(1L)
        assertThat(stock).isEqualTo(99L)
    }
}