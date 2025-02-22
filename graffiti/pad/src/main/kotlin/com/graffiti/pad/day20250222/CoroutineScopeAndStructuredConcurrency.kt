package com.graffiti.pad.day20250222

import com.graffiti.pad.day20250221.printWithThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/*
* launch / async는 CoroutineScope의 확장함수이다.
* runBlocking이 코루틴과 루틴의 세계를 이어주며 CoroutineScope을 제공해주었다.
* 우리가 직접 CoroutineScope을 만든다면 runBlocking이 필요하지 않다.
*/

/*
* CoroutineScope의 주요 역할
    * CoroutineContext라는 데이터를 보관하는 것
* CoroutineContext란?
    * 코루틴과 관련된 여러가지 데이터를 갖고 있다.
        * CoroutineExceptionHandler, 코루틴 이름, 코루틴 그 자체, CoroutineDispatcher 등등
* Dispatchers
    * 코루틴이 어떤 스레드에 배정될지를 관리하는 역할
*/

suspend fun main() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    job.join()
}