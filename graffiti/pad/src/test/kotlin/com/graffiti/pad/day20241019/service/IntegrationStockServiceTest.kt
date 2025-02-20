package com.graffiti.pad.day20241019.service

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.graffiti.pad.day20241019.domain.Stock
import com.graffiti.pad.day20241019.facade.NamedLockStockFacade
import com.graffiti.pad.day20241019.facade.OptimisticLockStockFacade
import com.graffiti.pad.day20241019.facade.RedisLockStockFacade
import com.graffiti.pad.day20241019.facade.RedissonLockStockFacade
import com.graffiti.pad.day20241019.repository.StockRepository


@SpringBootTest
class IntegrationStockServiceTest {
    @Autowired
    private lateinit var stockService: StockService
    @Autowired
    private lateinit var pessimisticLockStockService: PessimisticLockStockService
    @Autowired
    private lateinit var optimisticLockStockFacade: OptimisticLockStockFacade
    @Autowired
    private lateinit var namedLockStockFacade: NamedLockStockFacade
    @Autowired
    private lateinit var redisLockStockFacade: RedisLockStockFacade
    @Autowired
    private lateinit var redissonLockStockFacade: RedissonLockStockFacade
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
        val stock = stockService.getStockQuantity(1L)
        assertThat(stock).isEqualTo(99L)
    }

    @Test
    fun `decreaseStock_withHighVolumeRequests_multithreaded`() {
        // Arrange
        val productId = 1L
        val decreaseQuantity = 1L
        // Act
        val executor = Executors.newFixedThreadPool(5)
        repeat(100) {
            executor.submit {
                stockService.decreaseStock(productId, decreaseQuantity)
            }
        }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        // Assert
        val stock = stockService.getStockQuantity(1L)
        assertThat(stock).isEqualTo(0L)
    }

    @Test
    fun `decreaseStock_withHighVolumeRequests_multithreaded_pessimisticLock`() {
        // Arrange
        val productId = 1L
        val decreaseQuantity = 1L
        // Act
        val executor = Executors.newFixedThreadPool(5)
        repeat(100) {
            executor.submit {
                pessimisticLockStockService.decreaseStock(productId, decreaseQuantity)
            }
        }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        // Assert
        val stock = pessimisticLockStockService .getStockQuantity(1L)
        assertThat(stock).isEqualTo(0L)
    }

    @Test
    fun `decreaseStock_withHighVolumeRequests_multithreaded_optimisticLock`() {
        // Arrange
        val productId = 1L
        val decreaseQuantity = 1L
        // Act
        val executor = Executors.newFixedThreadPool(5)
        repeat(100) {
            executor.submit {
                optimisticLockStockFacade.decreaseStock(productId, decreaseQuantity)
            }
        }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        // Assert
        val stock = optimisticLockStockFacade.getStockQuantity(1L)
        assertThat(stock).isEqualTo(0L)
    }

    @Test
    fun `decreaseStock_withHighVolumeRequests_multithreaded_namedLock`() {
        // Arrange
        val productId = 1L
        val decreaseQuantity = 1L
        // Act
        val executor = Executors.newFixedThreadPool(5)
        repeat(100) {
            executor.submit {
                namedLockStockFacade.decreaseStock(productId, decreaseQuantity)
            }
        }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        // Assert
        val stock = namedLockStockFacade.getStockQuantity(1L)
        assertThat(stock).isEqualTo(0L)
    }

    @Test
    fun `decreaseStock_withHighVolumeRequests_multithreaded_redisLock`() {
        // Arrange
        val productId = 1L
        val decreaseQuantity = 1L
        // Act
        val executor = Executors.newFixedThreadPool(5)
        repeat(100) {
            executor.submit {
                redisLockStockFacade.decreaseStock(productId, decreaseQuantity)
            }
        }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        // Assert
        val stock = redisLockStockFacade.getStockQuantity(1L)
        assertThat(stock).isEqualTo(0L)
    }

    @Test
    fun `decreaseStock_withHighVolumeRequests_multithreaded_redissonLock`() {
        // Arrange
        val productId = 1L
        val decreaseQuantity = 1L
        // Act
        val executor = Executors.newFixedThreadPool(5)
        repeat(100) {
            executor.submit {
                redissonLockStockFacade.decreaseStock(productId, decreaseQuantity)
            }
        }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        // Assert
        val stock = redissonLockStockFacade.getStockQuantity(1L)
        assertThat(stock).isEqualTo(0L)
    }
}