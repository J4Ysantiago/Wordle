package com.example.wordle

object FourLetterWordList {
    private val words = listOf("STAR", "GAME", "WORD", "CODE", "JAVA", "TEST")

    fun getRandomFourLetterWord(): String {
        return words.random()
    }
}
