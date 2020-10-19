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
                val result1: Deferred<String> = async {
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    getResultFromApi()
                }
                val result2: Deferred<String> = async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    getResult2FromApi()
                }
                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
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

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1700)
        return RESULT_2

    }

    private fun logThread(methodName: String) {
        println("debug $methodName: ${Thread.currentThread().name}")
    }
}

// example of how to use the classic job + launch coroutine pattern
/*
private fun fakeApiRequest() {
    val startTime = System.currentTimeMillis()
    val parentJob = CoroutineScope(IO).launch {
        val job1 = launch {
            val time1 = measureTimeMillis {
                println("debug: launching job1 in thread: ${Thread.currentThread().name}")
                val result1 = getResultFromApi()
                setTextOnMainThread("Got $result1")

            }
            println("debug: completed job1 in $time1 ms")
        }

        val job2 = launch {
            val time2 = measureTimeMillis {
                println("debug: launching job2 in thread: ${Thread.currentThread().name}")
                val result2 = getResult2FromApi()
                setTextOnMainThread("Got $result2")

            }
            println("debug: completed job2 in $time2 ms")
        }
    }
    parentJob.invokeOnCompletion {
        println("debug: total elapsed time: ${System.currentTimeMillis() - startTime}")
    }
}*/
