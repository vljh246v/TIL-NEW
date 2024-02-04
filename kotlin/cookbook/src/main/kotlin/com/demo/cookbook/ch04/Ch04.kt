package com.demo.cookbook.ch04

class Ch04 {
}

fun sum(vararg nums: Int) = nums.fold(0) { acc, n -> acc + n }

fun sumWithTrace(vararg nums: Int) =
    nums.fold(0) { acc, n ->
        println("acc = $acc, n = $n")
        acc + n

    }