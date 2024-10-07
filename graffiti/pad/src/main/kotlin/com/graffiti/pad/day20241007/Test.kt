package com.graffiti.pad.day20241007

class Test {

    fun myAnimals(animals: List<Animal>) {
        println(animals)
    }

    fun copyFromTo(from: Array<out Animal>, to: Array<in Animal>) {
        for(i in from.indices) {
            to[i] = from[i]
        }
    }

    fun main() {
//        val cats: List<Cat> = listOf(Cat(), Cat())
//        myAnimals(cats)

//        val animals: Array<Animal> = arrayOf(Animal(), Animal())
        val any: Array<Any> = arrayOf(Any(), Any())
        val cats: Array<Cat> = arrayOf(Cat(), Cat())

        copyFromTo(cats, any)
    }
}



open class Animal
class Cat : Animal()
class Dog : Animal()


val animal: Animal = Cat()

val cats: Array<Cat> = arrayOf(Cat(), Cat())
//val animals: Array<Animal> = cats


sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    object Empty: Result<Nothing>()

    companion object {
        fun <T> successOrEmpty(list: List<T>) : Result<List<T>> {
            return if(list.isEmpty()) {
                Empty
            } else {
                Success(list)
            }
        }
    }
}