package com.graffiti.pad

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

fun main(): Unit {
    q6()
}

fun q5(): Unit = runBlocking {

    val job1 = launch {
        delay(3_000L)
        println("Job 1")
    }
    delay(2_000L)
    job1.cancel()
}

fun q6(): Unit = runBlocking {

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: ${throwable.message}")
    }

    supervisorScope {
        launch(exceptionHandler) {
            throw IllegalArgumentException("Something went wrong!")
        }
    }
}


fun q3(): Unit = runBlocking {
    val job1 = async {
        10
    }

    val job2 = async {
        20
    }
    println(job1.await() + job2.await())
}

fun q4(): Unit = runBlocking {
    val result = withContext(Dispatchers.IO) {
        delay(1_000L)
        "Background Work Done"
    }
    delay(100)
    println(result)
}


fun q1(): Unit = runBlocking {
    launch {
        println("Hello, Coroutine!")
    }
    delay(100)
}

fun q2(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        println("First Coroutine")
    }
    val job2 = launch {
        delay(2_000L)
        println("Second Coroutine")
    }

    job1.join()
    job2.join()
    println("Done")
}