package com.graffiti.pad.day20250221

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun example7(): Unit = runBlocking {
    val job = async(SupervisorJob()) {
        // SupervisorJob() 부모 자식 관계지만 에러가 출력되지 않음
        throw IllegalArgumentException()
    }
    delay(1_000L)
    job.await()
}


fun example6(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).async {
        throw IllegalArgumentException()
    }

    delay(1_000L)
    job.await()
}


fun example5(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).launch {
        throw IllegalArgumentException()
    }

    delay(1_000L)
}

fun example4(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    val job2 = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 2")
    }
}


fun example3(): Unit = runBlocking {
    val job = launch {
        try {
            delay(1_000L)
        } catch (e: CancellationException) {
            // 아무것도 안함
        }
        printWithThread("delay에 의해 취소되지 않았다")
    }

    delay(100)
    printWithThread("취소 시작")
    job.cancel()
}


/*
코루틴이 취소에 협조하는 방법
* kotlinx.coroutines 패키지의 suspend 함수 호출
* isActive로 CancelationException을 던지기
*/

fun example2(): Unit = runBlocking {
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
