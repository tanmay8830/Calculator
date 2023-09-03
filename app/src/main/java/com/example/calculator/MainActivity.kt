package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private var currentInput = ""
    private var isResultShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.txtview)

        val numberButtons = arrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
        )

        for (buttonID in numberButtons) {
            findViewById<Button>(buttonID).setOnClickListener { onNumberButtonClick(it) }
        }

        val operatorButtons = arrayOf(
            R.id.buttondivide, R.id.buttonMultiply,
            R.id.buttonsubsract, R.id.buttonplus
        )
        for (buttonID in operatorButtons) {
            findViewById<Button>(buttonID).setOnClickListener { onOperatorButtonClick(it) }
        }

        findViewById<Button>(R.id.buttonBraces).setOnClickListener { onBracesButtonClick() }
        findViewById<Button>(R.id.buttonmodulus).setOnClickListener { onModulusButtonClick() }
        findViewById<Button>(R.id.buttonbackspace).setOnClickListener { onBackspaceButtonClick() }
        findViewById<Button>(R.id.buttoncancle).setOnClickListener { onClearButtonClick() }
        findViewById<Button>(R.id.buttonequlas).setOnClickListener { onEqualsButtonClick() }
        findViewById<Button>(R.id.buttondot).setOnClickListener { onDotButtonClick() }
    }

    private fun onBracesButtonClick() {
        if (isResultShown) {
            return
        }
        val braces = "()"
        currentInput += braces
        textView.text = currentInput
    }

    private fun onModulusButtonClick() {
        if (isResultShown) {
            return
        }
        val modulus = "%"
        currentInput += modulus
        textView.text = currentInput
    }

    private fun onNumberButtonClick(view: View) {
        if (isResultShown) {
            textView.text = ""
            isResultShown = false
        }
        val number = (view as Button).text
        currentInput += number
        textView.text = currentInput
    }

    private fun onOperatorButtonClick(view: View) {
        if (isResultShown) {
            isResultShown = false
        }
        val operator = (view as Button).text.toString()

        // Special handling for subtraction to avoid conflicts with negative numbers
        if (operator == "-" && currentInput.isEmpty()) {
            currentInput += operator
        } else {
            currentInput += " $operator "
        }

        textView.text = currentInput
    }

    private fun onBackspaceButtonClick() {
        if (isResultShown) {
            return
        }
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length - 1)
            textView.text = currentInput
        }
    }

    private fun onClearButtonClick() {
        currentInput = ""
        textView.text = "0"
        isResultShown = false
    }

    private fun onEqualsButtonClick() {
        if (isResultShown) {
            return
        }
        try {
            val result = evaluateExpression(currentInput)
            textView.text = result.toString()
            currentInput = result.toString()
            isResultShown = true
        } catch (e: Exception) {
            textView.text = "Error"
            currentInput = ""
        }
    }

    private fun onDotButtonClick() {
        if (isResultShown) {
            return
        }
        val dot = "."
        currentInput += dot
        textView.text = currentInput
    }

    private fun evaluateExpression(expression: String): Double {
        try {
            val parts = expression.split(" ").filter { it.isNotBlank() }
            var result = parts[0].toDouble() // Use Double instead of Long

            var i = 1
            while (i < parts.size) {
                val operator = parts[i]
                val nextValue = parts[i + 1].toDoubleOrNull()

                if (nextValue != null) {
                    when (operator) {
                        "+" -> result += nextValue
                        "-" -> result -= nextValue
                        "×" -> result *= nextValue
                        "÷" -> result /= nextValue
                        else -> throw IllegalArgumentException("Invalid operator")
                    }
                    i += 2
                } else {
                    throw IllegalArgumentException("Invalid expression")
                }
            }

            return result
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid expression")
        }
    }
}
