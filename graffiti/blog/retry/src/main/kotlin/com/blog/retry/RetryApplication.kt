package com.blog.retry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class RetryApplication

fun main(args: Array<String>) {
    runApplication<RetryApplication>(*args)
}
