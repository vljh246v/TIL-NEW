package com.demo.cookbook.ch04

import java.math.BigInteger

class Ch04 {
}

// 4-1
fun sum(vararg nums: Int) = nums.fold(0) { acc, n -> acc + n }

// 4-3
fun sumWithTrace(vararg nums: Int) =
    nums.fold(0) { acc, n ->
        println("acc = $acc, n = $n")
        acc + n

    }

// 4-4
fun factorialFold(n: Long): BigInteger =
    when (n) {
        0L, 1L -> BigInteger.ONE
        else -> (2..n).fold(BigInteger.ONE) { acc, i ->
            acc * BigInteger.valueOf(i)
        }
    }

// 4-5
fun fibonacciFold(n: Int) =
    (2 until n).fold(1 to 1) { (prev, curr), _ ->
        curr to (prev + curr)
    }.second


// 4-7
fun sumReduce(vararg numbs: Int) =
    numbs.reduce { acc, i -> acc + i }