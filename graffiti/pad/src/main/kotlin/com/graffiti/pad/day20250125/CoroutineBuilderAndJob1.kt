package com.graffiti.pad.day20250125

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineBuilderAndJob1 {
}

fun main() {
    // runBlocking : 코루틴을 만드는 첫번째 방법
    // 루틴 세계와 코루틴 셰게를 이어준다.
    // 코루틴을 만드는 함수들을 코루틴빌더라고 부름
    // runBlocking 이름에는 Blocking이 들어가있다.
    // 특징으로는 runBlocking인해 만들어진 코루틴과 내부에서 추가적으로 만든 새로운 코루틴이 모두다 종료될때까지 스레드를 블록킹 시킴
    // 완료될때까지 다른 코드를 실행할 수 없다.

    runBlocking {
        printWithThread("START")
        launch {
            delay(2_000L) // yield와 비슷함
            // yield는 아무것도 안하고 다른 코루틴으로 양보하는것
            // delay는 일정 시간동안 기다리고 다른 코루틴으로 양보
            printWithThread("LAUNCH END")
        }
    }

    // 해당 부분으로 runBlocking이 끝날때까지 코드가 넘어오지 않음

    // runBlocking은 스레드를 블록킹하기 때문에 코루틴을 만들고 싶을때마다 호출하면 안되고, 최초 진입 메인함수 또는 테스트코드 시작시 생성해 줘야한다.
    printWithThread("END")
}

// launch : 코루틴을 만드는 두번째 방법
// 반환값이 없는 코루틴을 만드는 코루틴빌더