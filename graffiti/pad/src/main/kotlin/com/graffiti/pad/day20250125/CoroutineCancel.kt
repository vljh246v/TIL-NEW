package com.graffiti.pad.day20250125

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineCancel {
}


// 코루틴을 적절히 취소해 필요하지 않는 코루틴을 적절히 취소해 주어야 한다.
// 만약 필요하지 않는 코루틴을 취소하지 않으면 계속 사용됨

// cancle() 함수를 사용하여 코루틴을 취소할 수 있다. 하지만 취소 대상이 되는 코루틴도 협조해 주어야 한다.



fun main(): Unit = runBlocking {
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

    // 어덯게 취소에 협조했을까?
    // 취소에 협조하는 방법1
    // delay() / yield() 같은 suspend 함수 사용
    // delay() / yield() 을 사용하는 코루틴은 이 함수를 사용할때 자동으로 취소 여부를 확인해서 취소에 협조하도록 되어 있다.
}