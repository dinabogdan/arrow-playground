package com.freesoft.arrowplayground

import arrow.core.Try
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.instances.io.monad.binding
import arrow.effects.instances.io.monad.monad
import arrow.effects.liftIO
import java.security.SecureRandom

private val random = SecureRandom()

private fun IO<String?>.safeToInt() = Try { this.map { it?.toInt() } }

fun main() {
    mainIO().unsafeRunSync()
}

private fun mainIO(): IO<Unit> = binding {
    putStrLn("What is your name?").bind()
    val name = readStrLn().bind()
    putStrLn("Hello, $name, welcome to the game").bind()
    gameLoop(name).bind()
}

private fun gameLoop(name: String?): IO<Unit> = IO.monad().binding {
    putStrLn("Dear $name, please guess a number from 1 to 5: ").bind()
    readStrLn().safeToInt().fold(
            { putStrLn("You did not enter a number!").bind() },
            {
                val number = nextInt(5).map { it + 1 }.bind()
                if (it.bind() == number) putStrLn("You guessed right, $name")
                else putStrLn("You guessed wrong, $name! The number was $number").bind()
            }
    )
    checkContinue(name).map {
        (if (it) gameLoop(name) else Unit.liftIO())
    }.flatten().bind()
}.fix()

private fun checkContinue(name: String?): IO<Boolean> = IO.monad().binding {
    putStrLn("Do you want to continue, $name?").bind()
    (readStrLn()).map { it?.toLowerCase() }
            .map {
                when (it) {
                    "y" -> true.liftIO()
                    "n" -> false.liftIO()
                    else -> checkContinue(name)
                }
            }.flatten()
            .bind()
}.fix()

private fun nextInt(upper: Int): IO<Int> = IO { random.nextInt() }
private fun putStrLn(line: String): IO<Unit> = IO { println(line) }
private fun readStrLn(): IO<String?> = IO { readLine() }