package com.graffiti.pad.day20250221

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/*
코루틴이 취소에 협조하는 방법
* kotlinx.coroutines 패키지의 suspend 함수 호출
* isActive로 CancelationException을 던지기
*/
fun main(): Unit = runBlocking {
    val job = launch(Dispatchers.Default) {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()

        while (isActive && i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++}번째 출력")
                nextPrintTime += 1_000L
            }

//            if (isActive) {
//                throw CancellationException()
//            }
        }
    }

    delay(100L)
    job.cancel()
}

fun example1(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 2")
    }

    delay(100)
    job1.cancel()
}


fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}
