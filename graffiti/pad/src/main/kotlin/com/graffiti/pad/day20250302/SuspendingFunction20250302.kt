package com.graffiti.pad.day20250302

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


fun example20250302_1(): Unit = runBlocking {
    printWithThread("Start")
    printWithThread("Result : ${calculateResult()}")
    printWithThread("End")
}

fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}


suspend fun calculateResult(): Int = coroutineScope {
    val num1 = async {
        delay(1_000L)
        10
    }

    val num2 = async {
        delay(1_000L)
        20
    }

    num1.await() + num2.await()
}