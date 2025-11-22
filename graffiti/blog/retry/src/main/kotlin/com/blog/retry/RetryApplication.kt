package com.blog.retry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@EnableFeignClients
@SpringBootApplication
class RetryApplication

fun main(args: Array<String>) {
    runApplication<RetryApplication>(*args)
}
