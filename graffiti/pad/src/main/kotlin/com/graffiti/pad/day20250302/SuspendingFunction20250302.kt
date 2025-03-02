package com.graffiti.pad.day20250302

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}

fun example20250302_2(): Unit = runBlocking {
    printWithThread("Start")
    printWithThread("Result : ${calculateResult()}")
    printWithThread("End")
}

fun example20250302_1(): Unit = runBlocking {
    printWithThread("Start")
    printWithThread("Result : ${calculateResult()}")
    printWithThread("End")
}

suspend fun calculateResult(): Int = withContext(Dispatchers.Default) {
    val num1 = async(Dispatchers.Default) {
        delay(1_000L)
        printWithThread("num1")
        10
    }

    val num2 = async {
        delay(1_000L)
        printWithThread("num2")
        20
    }

    num1.await() + num2.await()
}