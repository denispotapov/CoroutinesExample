package com.example.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Clicked!")
            fakeApiRequest()
        }
    }
    private fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1 = async {
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    getResultFromApi()
                }.await()
                val result2= async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    try {
                        getResult2FromApi(result1)
                    } catch (e: CancellationException) {
                        e.message
                    }

                }.await()

                println("debug: got result2: $result2")
            }
            println("debug: total time elapsed: $executionTime")
        }
    }
    private fun setNewText(input: String) {
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResultFromApi(): String {
        logThread("getResultFromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(result1: String): String {
        logThread("getResult2FromApi")
        delay(1700)
        if (result1 == "Result #1") {
            return RESULT_2
        }
        throw CancellationException("Result #1 was incorrect")
    }

    private fun logThread(methodName: String) {
        println("debug $methodName: ${Thread.currentThread().name}")
    }
}
