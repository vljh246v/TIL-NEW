package com.graffiti.pad

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PadApplication

fun main(args: Array<String>) {
    runApplication<PadApplication>(*args)
}
