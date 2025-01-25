package com.graffiti.pad.day20250125

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineBuilderAndJob2 {
}

fun main() : Unit = runBlocking {
        val job = launch(start = CoroutineStart.LAZY) {
            printWithThread("Hello launch")
        }

        delay(1_000L)
        job.start()

        // launch 코루틴 빌더 옵션으로 start에 CoroutineStart.LAZY 옵션을 주면
        // 우리가 코루틴을 제어하는 변수에 start함수를 호출해줘야만 실행된다.
        // job에는 시작뿐만 아니라 취소/대기 하는 기능도 가능

    }

fun example1(){
 runBlocking{
     launch {
         printWithThread("Hello launch")
     }
     // job은 launch 에 의한 코루틴 결과물이 아님
     // launch가 만들어낸 코루틴을 제어할 수 있는 객체
     // 제어할수있다는 뜻은 시작/취소/종료 할때까지 기다릴 수있다.
 }
}