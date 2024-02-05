package com.demo.cookbook.ch05

class Ch05


// 5-2
val strings = arrayOf("this", "is", "an", "array", "of", "strings")

// 5-3
val nullStringArray = arrayOfNulls<String>(5)

// 5-4
val squares = Array(5) { i -> (i * i).toString() }

// 5-9
var numList = mutableListOf(3, 1, 4, 1, 5, 9)
var numSet = mutableSetOf(3, 1, 4, 1, 5, 9)
var map = mutableMapOf(1 to "one", 2 to "two", 3 to "three")
