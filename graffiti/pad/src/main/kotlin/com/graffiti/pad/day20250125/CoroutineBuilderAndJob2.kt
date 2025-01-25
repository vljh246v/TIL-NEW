package com.graffiti.pad.day20250125

import kotlin.system.measureTimeMillis
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineBuilderAndJob2 {
}


fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { apiCall1() }
        val job2 = async { apiCall2() }
        printWithThread("Result : ${job1.await() + job2.await()}")
    }
    printWithThread("소요시간 : $time ms")
    // async의 최대 장점은 callback을 활용하지 않고 동기방식으로 코드를 작성할 수 있다.
}

suspend fun apiCall1(): Int {
    delay(1_000L)
    return 1
}

suspend fun apiCall2(): Int {
    delay(1_000L)
    return 2
}

fun example5(): Unit = runBlocking {
    // async는 주어진 함수의 실행 결과를 반환할 수 있음
    val job = async {
        3 + 5
    }

    // async로 인해 만들어진 코루틴을 제엉할 수 있는 Deferred<T> 객체를 전달받음
    // Deferred는 job을 상속받기 때문에 동일한 기능이 있고
    // async에만 존재하는 await가 존재

    val number = job.await()

    // await은 async의 결괄르 가지고 올 수 있음
    // async를 활용하면 여러 api를 실행시킬 수 있음
}

fun example4(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1")

    }
    job1.join()
    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 2")
    }

    // 2초가 걸리는것이 아닌 거의 동시에 종료
    // job1이 delay 되는 동안 job2가 실행됨
    // join을 사용하면 코루틴1이 끝날때까지 기다렸다가 그후 코루틴2를 실행
    // join을 쓰면 코루틴이 완전히 종료될때까지 기다린다.
    // start() : 시작신호
    // cancle() : 취소신호
    // join() : 코루틴이 완료될 때까지 대기
}

fun example3(): Unit = runBlocking {
    val job = launch {
        (1..5).forEach {
            printWithThread(it)
            delay(500L)
        }
    }

    delay(1_000L)
    job.cancel()
    // job 취소 가능
}

fun example2() : Unit = runBlocking {
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