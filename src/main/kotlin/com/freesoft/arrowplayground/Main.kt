package com.freesoft.arrowplayground

import arrow.core.Try
import arrow.effects.IO
import java.security.SecureRandom

private val random = SecureRandom()

fun parseInt(input: String?) = Try { input?.toInt() }

private fun String.safeToInt() = Try { this.toInt() }

fun main() {

    println("What is your name?")
    val name = readLine()
    println("Hello, $name, welcome to the game")
    var exec = true
    while (exec) {
        val number = random.nextInt(5) + 1
        println("Dear $name, please guess a number from 1 to 5: ")
        evaluate(number, name)
        checkContinue(name)
    }
}

private fun evaluate(number: Int, name: String?) {
    (readLine() as String)
            .safeToInt()
            .fold(
                    { println("You did not enter a number!") },
                    {
                        if (it == number) println("You guessed right, $name")
                        else println("You guessed wrong, $name! The number was $number")
                    }
            )
}

private fun checkContinue(name: String?): Boolean {
    println("Do you want to continue, $name?")
    return (readLine() as String).transform { it.toLowerCase() }
            .transform {
                when (it) {
                    "y" -> true
                    "n" -> false
                    else -> checkContinue(name)
                }
            }
}

private fun <T> String.transform(f: (String) -> T) = f(this)

private fun putStrLn(line: String): IO<Unit> = IO { println(line) }
private fun readStrLn(): IO<String?> = IO { readLine() }