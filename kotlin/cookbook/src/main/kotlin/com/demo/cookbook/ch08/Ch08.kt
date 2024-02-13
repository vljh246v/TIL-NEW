package com.demo.cookbook.ch08

fun main() {
    println(ultimateAnswer)
    println(ultimateAnswer)
}

// 8-1
interface Dialable {
    fun dial(number: String): String
}

class Phone : Dialable {
    override fun dial(number: String): String {
        return "Dialing $number..."
    }
}

interface Snappable {
    fun takePicture(): String
}

class Camera : Snappable {
    override fun takePicture(): String {
        return "Taking picture..."
    }
}

// 8-2
class SmartPhone(
    private val phone: Dialable = Phone(),
    private val camera: Snappable = Camera()
) : Dialable by phone, Snappable by camera


// 8-6

val ultimateAnswer: Int by lazy {
    println("computing the answer")
    42
}