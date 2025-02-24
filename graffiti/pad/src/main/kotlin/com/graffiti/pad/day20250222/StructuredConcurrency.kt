package com.graffiti.pad.day20250222

import com.graffiti.pad.day20250125.printWithThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * 코루틴 상태가 completing / completed 로 나뉜 이유는 자식 코루틴을 기다려야 하기 때문
 * completing 에서 자식 코루틴에 예외가 발생하면 cancelling 상태로 되돌아 가기 위해
 * 부모-자식 관계의 코루틴이 한 몸 처럼 움직이는것 : Structured Concurrency
 * Structured Concurrency는 수많은 코루틴이 유실되거나 누수되지 않도록 보장한다.
 * Structured Concurrency는 코드 내의 에러가 유실되지 않고 적절히 보고될 수 있도록 보장한다.
 *
 * 자식 코루틴에서 예외가 발생할 경우 Structured Concurrency에 의해 부모 코루틴이 취소되고, 부모 코루틴의 다른 자식 코루틴들도 취소된다.
 * 자식 코루틴에서 예외가 발생하지 않더라도, 부모 코루틴이 취소되면, 자식 코루틴들이 취소된다.
 * 다만, CancellationException은 정상적인 취소로 간주하기 때문에 부모 코루틴에게 전파되지 않고, 부모 코루틴의 다른 자식 코루틴을 취소시키지도 않는다.
 *
 */

fun example1(): Unit = runBlocking {
    launch {
        delay(500L)
        printWithThread("A")
    }

    launch {
        delay(600L)
        throw IllegalArgumentException("코루틴 실패")
    }
}
