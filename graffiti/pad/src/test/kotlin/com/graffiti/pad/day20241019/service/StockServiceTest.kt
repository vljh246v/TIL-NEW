package com.graffiti.pad.day20241019.service

import com.graffiti.pad.day20241019.domain.Stock
import com.graffiti.pad.day20241019.repository.StockRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class StockServiceTest {

    @Mock
    private lateinit var stockRepository: StockRepository

    @InjectMocks
    private lateinit var stockService: StockService

    @Test
    fun `decreaseStock should decrease stock quantity`() {
        val stock = Stock().apply {
            quantity = 10
        }
        `when`(stockRepository.findByProductId(1L)).thenReturn(stock)

        stockService.decreaseStock(1L, 5L)

        verify(stockRepository).save(stock)
        assert(stock.quantity == 5L)
    }

    @Test
    fun `decreaseStock should throw exception when stock is not enough`() {
        val stock = Stock().apply {
            quantity = 3
        }
        `when`(stockRepository.findByProductId(1L)).thenReturn(stock)

        assertThrows(IllegalArgumentException::class.java) {
            stockService.decreaseStock(1L, 5L)
        }
    }
}