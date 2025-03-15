 package com.graffiti.multipad

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PingApplication

fun main(args: Array<String>) {
    runApplication<PingApplication>(*args)
}
