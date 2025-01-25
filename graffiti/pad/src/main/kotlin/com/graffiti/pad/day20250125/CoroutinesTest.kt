package com.graffiti.pad.day20250125

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

class CoroutinesTest {
}


fun main(): Unit = runBlocking {
    // runBlocking을 시작한 순간 코루틴 세상으로 진입
    // 새로운 코루틴 생성됨
    printWithThread("Start")
    launch { // 반환값이 없는 코루틴을 만듬
        // runBlocking에 의한 전체 코루틴, lanuch 에 의한 newRoutine을 부르는 코루틴

        newRoutine()
    }
    yield() // 지금 코루틴을 중단하고 다른 코루틴이 실행되도록 스레드를 양보함
    printWithThread("End")
}

// suspend 다른 suspend 함수를 호출하는 명령어
suspend fun newRoutine() {
    val num1 = 1
    val num2 = 2
    yield()
    printWithThread ("${num1 + num2}")
}

fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}

// 루틴
// * 시작되면 끝날 때 가지 멈추지 않음
// * 한번 끝나면 루틴 내의 ㅈ어보가 사라짐

// 코루틴
// 중단되었다가 재개될 수 있음
// 중단되더라도 루틴 내의 정보가 사라지지 않음
