package com.herringbone.voicecommandcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.R.layout
import android.app.Activity
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import android.speech.RecognizerIntent
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable


class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener  {

    override fun onInit(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var firstNumTextView: TextView
    lateinit var secondNumTextView: TextView
    lateinit var operatorTextView: TextView
    lateinit var resultTextView: TextView

    lateinit var goButton: Button

    lateinit var textToSpeech: TextToSpeech

    private var FIRST_NUMBER: Int = 0
    private var SECOND_NUMBER: Int = 0
    private var OPERATOR: Char = ' '
    private var RESULT: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textToSpeech = TextToSpeech(this, this)


        firstNumTextView = findViewById(R.id.firstNumTextView)
        secondNumTextView = findViewById(R.id.secondNumTextView)
        operatorTextView = findViewById(R.id.operatorTextView)
        resultTextView = findViewById(R.id.resultTextView)
        goButton = findViewById(R.id.goButton)

        firstNumTextView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                startActivityForResult(intent, 10)
            }
        })

        secondNumTextView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                startActivityForResult(intent, 20)
            }
        })

        operatorTextView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

            }
        })

        goButton.setOnClickListener(View.OnClickListener {
            RESULT = performCalculations()
//            resultTextView.setText(String.valueOf(RESULT))
            resultTextView.setText(RESULT)
//            textToSpeech.speak(String.valueOf(RESULT), TextToSpeech.QUEUE_ADD, null)
            textToSpeech.speak(RESULT.toString(), TextToSpeech.QUEUE_ADD, null)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            var intFound:Int
            when (requestCode) {
                10 -> {
                    var intFound =
                        getNumberFromResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))
                    if (intFound != -1) {
                        FIRST_NUMBER = intFound
                        firstNumTextView.setText(intFound.toString())
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sorry, I didn't catch that! Please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                20 -> {
                    intFound =
                        getNumberFromResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))
                    if (intFound != -1) {
                        SECOND_NUMBER = intFound
                        secondNumTextView.setText(intFound.toString())
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sorry, I didn't catch that! Please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                30 -> {
                    val operatorFound =
                        getOperatorFromResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))
                    if (operatorFound != '0') {
                        OPERATOR = operatorFound
                        operatorTextView.setText(operatorFound.toString())
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sorry, I didn't catch that! Please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "Failed to recognize speech!", Toast.LENGTH_LONG)
                .show()
        }
    }

    // method to loop through results trying to find a number
    private fun getNumberFromResult(results: ArrayList<String>): Int {
        for (str in results) {
            if (getIntNumberFromText(str) !== -1) {
                return getIntNumberFromText(str)
            }
        }
        return -1
    }

    // method to loop through results trying to find an operator
    private fun getOperatorFromResult(results: ArrayList<String>): Char {
        for (str in results) {
            if (getCharOperatorFromText(str) != '0') {
                return getCharOperatorFromText(str)
            }
        }
        return '0'
    }

    // method to convert string number to integer
    private fun getIntNumberFromText(strNum: String): Int {
        when (strNum) {
            "zero" -> return 0
            "one" -> return 1
            "two" -> return 2
            "three" -> return 3
            "four" -> return 4
            "five" -> return 5
            "six" -> return 6
            "seven" -> return 7
            "eight" -> return 8
            "nine" -> return 9
        }
        return -1
    }

    // method to convert string operator to char
    private fun getCharOperatorFromText(strOper: String): Char {
        when (strOper) {
            "plus", "add" -> return '+'
            "minus", "subtract" -> return '-'
            "times", "multiply" -> return '*'
            "divided by", "divide" -> return '/'
            "power", "raised to" -> return '^'
        }
        return '0'
    }

    private fun performCalculations(): Int {
        when (OPERATOR) {
            '+' -> return FIRST_NUMBER + SECOND_NUMBER
            '-' -> return FIRST_NUMBER - SECOND_NUMBER
            '*' -> return FIRST_NUMBER * SECOND_NUMBER
            '/' -> return FIRST_NUMBER / SECOND_NUMBER
            '^' -> return FIRST_NUMBER xor SECOND_NUMBER
        }
        return -999
    }
}
