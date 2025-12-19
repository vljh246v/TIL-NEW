package com.graffiti.pad.day20251210

class Solution {
    fun solution(myString: String, pat: String): Int {

        var answer = myString.map { it ->
            if(it.toString() == "A") {
                "B"
            } else {
                "A"
            }
        }.joinToString("")
            .contains(pat)

        return if(answer) 1 else 0
    }
}


fun main() {
    val sol = Solution()
    val result1 = sol.solution("ABBAA", "AABB")
    println(result1) // Expected output: 1

    val result2 = sol.solution("ABAB", "ABAB")
    println(result2) // Expected output: 0
}