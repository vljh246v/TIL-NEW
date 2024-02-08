package com.demo.cookbook.ch06


fun main() {

    // 6-1 bad case
    (100 until 200).map { it * 2 }
        .filter { it % 3 == 0 }
        .first()

    // 6-2
    (100 until 200).map { it * 2 }
        .first { it % 3 == 0 }


    // 6-3
    (100 until 2_000_000).asSequence()
        .map {
            println("doubling $it")
            it * 2
        }
        .filter {
            println("filtering $it")
            it % 3 == 0
        }
        .first()
}