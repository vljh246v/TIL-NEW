package com.graffiti.pad.day20250222

import com.graffiti.pad.day20250221.printWithThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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