package com.freesoft.arrowplayground

import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import java.security.SecureRandom

private val random = SecureRandom()

fun parseInt(input: String?) = Try { input?.toInt() }

fun main() {

    println("What is your name?")
    val name = readLine()
    println("Hello, $name, welcome to the game")
    var exec = true
    while (exec) {
        val number = random.nextInt(5) + 1
        println("Dear $name, please guess a number from 1 to 5: ")
        val guess = parseInt(readLine())
        when (guess) {
            is Failure -> println("You did not enter a number!")
            is Success<Int?> -> {
                if (guess.value == number) println("You guessed right, $name")
                else println("You guessed wrong, $name! The number was $number")
            }
        }
        println("Do you want to continue, $name?")
        when (readLine()) {
            "y" -> exec = true
            "n" -> exec = false
        }
    }
    println("Hello world!")
}