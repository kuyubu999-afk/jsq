package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private var input = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResult = findViewById(R.id.tvResult)

        val btnIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv,
            R.id.btnDot, R.id.btnClear, R.id.btnEqual
        )
        for (id in btnIds) {
            findViewById<Button>(id).setOnClickListener {
                onButtonClick((it as Button).text.toString())
            }
        }
    }

    private fun onButtonClick(text: String) {
        when (text) {
            "C" -> input = ""
            "=" -> calc()
            else -> input += text
        }
        tvResult.text = input
    }

    private fun calc() {
        try {
            val res = eval(input)
            input = res.toString()
        } catch (e: Exception) {
            input = "Error"
        }
        tvResult.text = input
    }

    private fun eval(expr: String): Double {
        return object : Any() {
            fun parse(): Double = parseSum()
            var pos = 0
            fun peek() = if (pos < expr.length) expr[pos] else 0
            fun next() = expr[pos++]
            fun parseSum(): Double {
                var v = parseMul()
                while (true) {
                    when (peek()) {
                        '+' -> { next(); v += parseMul() }
                        '-' -> { next(); v -= parseMul() }
                        else -> return v
                    }
                }
            }
            fun parseMul(): Double {
                var v = parseNum()
                while (true) {
                    when (peek()) {
                        '*' -> { next(); v *= parseNum() }
                        '/' -> { next(); v /= parseNum() }
                        else -> return v
                    }
                }
            }
            fun parseNum(): Double {
                val sb = StringBuilder()
                while (peek().isDigit() || peek() == '.') sb.append(next())
                return sb.toString().toDouble()
            }
        }.parse()
    }
}
