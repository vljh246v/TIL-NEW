package com.blog.retry.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "chaosClient", url = "http://localhost:8080/chaos")
interface ChaosClient {
    @GetMapping("/flaky")
    fun callFlakyEndpoint(): String
}