package com.graffiti.pad.day20250222

import java.util.concurrent.CompletableFuture
import com.graffiti.pad.day20250221.printWithThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun example24(): Unit = runBlocking {
    val result1: Deferred<Int> = async {
        call1()
    }
    // 이 코드는 Deferred에 의존하게 됨
    // 다른 비동기 라이브러리를 갈아 끼울때 라이브러리 변경에 대한 여파가 미침

    val result2 = async {
        call2(result1.await())
    }

    printWithThread(result2.await())
}


fun main(): Unit = runBlocking {
    val result1 = call1()
    val result2 = call2(result1)

    printWithThread(result2)
}

// suspend function은 여러 비동기 라이브러리를 사용할 수 있도록 도와준다.

suspend fun call2(num: Int): Int {
    return CompletableFuture.supplyAsync {
        Thread.sleep(1_000L) // 별도 스레드에서 실행됨
        num * 2
    }.await()
}

suspend fun call1(): Int {
    return CoroutineScope(Dispatchers.Default).async {
        Thread.sleep(1_000L) // Dispatchers.Default에서 실행됨
        100
    }.await()
}

interface AsyncCaller {
    suspend fun call()
}

class AsyncCallerImpl : AsyncCaller {
    override suspend fun call() {
        delay(1_000L)
        printWithThread("call")
    }
}


fun example23(): Unit = runBlocking {
    launch {
        a()
        b()
    }

    launch {
        c()
    }
}

suspend fun a() {
    printWithThread("A")
}

suspend fun b() {
    printWithThread("B")
}

suspend fun c() {
    printWithThread("C")
}


/*

* suspending function
    * suspend가 붙은 함수
    * 다른 suspend를 붙은 함수를 호출할 수 있다.
    * 코ㅜ틴이 중지 되었다가 재개 '될 수 있는' 지점
*/

fun example22(): Unit = runBlocking {
    launch {
        // launch 의 block이 suspending lambda이기 때문에 delay 함수 호출 가능
        delay(1_000L)
    }
}