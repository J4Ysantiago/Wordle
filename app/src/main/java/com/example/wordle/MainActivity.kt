package com.example.wordle

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var targetWord: String
    private var guessCount = 0
    private val maxGuesses = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val guessInput = findViewById<EditText>(R.id.guessInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val boardLayout = findViewById<LinearLayout>(R.id.linearContainer)
        val answerText = findViewById<TextView>(R.id.answerText)

        // Pick a random word
        targetWord = FourLetterWordList.getRandomFourLetterWord()

        resetButton.visibility = Button.GONE

        submitButton.setOnClickListener {
            val guess = guessInput.text.toString().uppercase()

            if (guess.length != 4) {
                Toast.makeText(this, "Enter a 4-letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guessCount++
            val feedback = checkGuess(guess, targetWord)

            // Create a horizontal row that contains the letters and the hint
            val rowLayout = LinearLayout(this)
            rowLayout.orientation = LinearLayout.HORIZONTAL
            rowLayout.gravity = Gravity.CENTER_VERTICAL

            // LinearLayout for the guess letters
            val lettersLayout = LinearLayout(this)
            lettersLayout.orientation = LinearLayout.HORIZONTAL

            // String for hint
            val hintBuilder = StringBuilder()

            for (i in guess.indices) {
                val letterBox = TextView(this)
                letterBox.text = guess[i].toString()
                letterBox.textSize = 24f
                letterBox.setPadding(24, 24, 24, 24)
                letterBox.setTextColor(Color.WHITE)

                when (feedback[i]) {
                    'O' -> {
                        letterBox.setBackgroundColor(Color.GREEN)
                        hintBuilder.append("Letter ${i + 1} correct ")
                    }
                    '+' -> letterBox.setBackgroundColor(Color.YELLOW)
                    'X' -> letterBox.setBackgroundColor(Color.DKGRAY)
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 8, 8, 8)
                letterBox.layoutParams = params

                lettersLayout.addView(letterBox)
            }

            // TextView for hint next to the row
            val hintText = TextView(this)
            hintText.text = hintBuilder.toString()
            hintText.setPadding(16, 0, 0, 0)
            hintText.textSize = 16f
            hintText.setTextColor(Color.BLUE)

            rowLayout.addView(lettersLayout)
            rowLayout.addView(hintText)

            boardLayout.addView(rowLayout)

            // Clear input
            guessInput.text.clear()

            // End condition
            if (guess == targetWord || guessCount >= maxGuesses) {
                submitButton.isEnabled = false
                resetButton.visibility = Button.VISIBLE
                answerText.text = "The word was: $targetWord"
            }
        }

        resetButton.setOnClickListener {
            targetWord = FourLetterWordList.getRandomFourLetterWord()
            guessCount = 0
            boardLayout.removeAllViews()
            answerText.text = ""
            submitButton.isEnabled = true
            resetButton.visibility = Button.GONE
        }

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Returns 'O' = correct spot, '+' = correct letter wrong spot, 'X' = not in word
    private fun checkGuess(guess: String, target: String): String {
        var result = ""
        for (i in guess.indices) {
            result += if (guess[i] == target[i]) {
                "O"
            } else if (guess[i] in target) {
                "+"
            } else {
                "X"
            }
        }
        return result
    }
}

